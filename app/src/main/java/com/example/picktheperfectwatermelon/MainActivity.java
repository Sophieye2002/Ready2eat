package com.example.picktheperfectwatermelon;


import android.content.Intent;


import android.os.Environment;

import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.picktheperfectwatermelon.AudioAnylysis.ReadStandard;
import com.example.picktheperfectwatermelon.loading.BaseActivity;
import com.example.picktheperfectwatermelon.view.AudioView;
import com.zlw.main.recorderlib.RecordManager;
import com.zlw.main.recorderlib.recorder.RecordConfig;
import com.zlw.main.recorderlib.recorder.RecordHelper;
import com.zlw.main.recorderlib.recorder.listener.RecordDataListener;
import com.zlw.main.recorderlib.recorder.listener.RecordFftDataListener;
import com.zlw.main.recorderlib.recorder.listener.RecordResultListener;
import com.zlw.main.recorderlib.recorder.listener.RecordSoundSizeListener;
import com.zlw.main.recorderlib.recorder.listener.RecordStateListener;
import com.zlw.main.recorderlib.utils.Logger;

import java.io.File;
import java.util.Arrays;
import java.util.Locale;

import javax.sql.DataSource;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "yin";
    private static final int END_DELAY = 0;//三秒录音结束
    private static final int END_DATA_ANALYSIS = 1;//数据分析完成

    private Button bt_record;
    private boolean butttonFlag;
    private AudioView audioView;
    private String lastRecordFileName;//上次的录音文件

    final RecordManager recordManager = RecordManager.getInstance();
    int waterCon = 20;


    private Handler uiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case END_DELAY://三秒录音结束
                    recordManager.stop();//停止录音
                    Toast.makeText(MainActivity.this,
                            "Stop recording！The system is conducting data analysis", Toast.LENGTH_SHORT).show();
                    loadData();//显示加载页面
                    new MyThread().start();
                    break;
                case END_DATA_ANALYSIS://对音频文件操作成功
                    showLoadSuccess();
                    butttonFlag = false;//设置停止
                    waterCon = msg.arg1;//取出计算结果
                    bt_record.setVisibility(View.VISIBLE);
                    bt_record.setText("Checking The Result");
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Logger.i(TAG, "onCreate %s", "测试");

        initView();
        initRecord();//录音初始化
    }

    private void initRecord() {

        recordManager.init(App.getInstance(), BuildConfig.DEBUG);
        recordManager.changeFormat(RecordConfig.RecordFormat.WAV);
        String recordDir = String.format(Locale.getDefault(), "%s/Record/com.yin.main/",
                Environment.getExternalStorageDirectory().getAbsolutePath());
        recordManager.changeRecordDir(recordDir);

        //录音状态监听
        recordManager.setRecordStateListener(new RecordStateListener() {
                                                 @Override
                                                 public void onStateChange(RecordHelper.RecordState state) {
                                                     Logger.i(TAG, "onStateChange %s", state.name());
                                                     switch (state) {
                                                         case PAUSE://暂停
                                                             break;
                                                         case IDLE://空闲
                                                             break;
                                                         case RECORDING:
                                                             //Toast.makeText(MainActivity.this, "recording", Toast.LENGTH_SHORT).show();
                                                             break;
                                                         case STOP://停止
                                                             //Toast.makeText(MainActivity.this, "stop recording ", Toast.LENGTH_SHORT).show();
                                                             break;
                                                         case FINISH://结束
                                                             break;
                                                         default:
                                                             break;
                                                     }
                                                 }

                                                 @Override
                                                 public void onError(String error) {
                                                     Logger.i(TAG, "onError %s", error);
                                                 }
                                             }
        );
        //录音结果监听
        recordManager.setRecordResultListener(new RecordResultListener() {
            @Override
            public void onResult(File result) {
                lastRecordFileName = result.getAbsolutePath();
                Logger.i(TAG,"文件名：%s",lastRecordFileName);
                Toast.makeText(MainActivity.this, "录音文件： " + result.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            }
        });
        //音频可视化监听
        recordManager.setRecordFftDataListener(new RecordFftDataListener() {
            @Override
            public void onFftData(byte[] data) {
                audioView.setWaveData(data);
            }
        });

        //声音大小监听
        recordManager.setRecordSoundSizeListener(new RecordSoundSizeListener() {
            @Override
            public void onSoundSize(int soundSize) {

            }
        });
        //音频数据监听
        recordManager.setRecordDataListener(new RecordDataListener() {
            @Override
            public void onData(byte[] data) {

            }
        });
    }

    private void initView() {
        bt_record = (Button) findViewById(R.id.bt_record);
        setMgr(bt_record);
        audioView = (AudioView) findViewById(R.id.audioView);
        bt_record.setOnClickListener(this);
        butttonFlag = true;//设置按钮初始状态
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_record:
                if (butttonFlag) {
                    Toast.makeText(MainActivity.this, "Start recording!Please knock 3 times in 3 seconds.", Toast.LENGTH_SHORT).show();
                    bt_record.setVisibility(View.INVISIBLE);
                    //bt_record.setText("END");
                    recordManager.start();//开始录音
                    //设置三秒定时
                    new delayThread().start();

                } else {
                    butttonFlag = true;
                    bt_record.setText("record");
                    Logger.i(TAG,"计算结果：%s",waterCon);
                    //跳转结果页面
                    Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                    intent.putExtra("waterCon", waterCon);
                    intent.putExtra("sweetness", waterCon);
                    intent.putExtra("texture", (100-waterCon));
                    startActivity(intent);
                    finish();
                }
                break;
                default:
                    break;
        }
    }

    private void loadData() {
        showLoading();
    }

    @Override
    protected void onLoadRetry() {
        loadData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        recordManager.stop();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    //进行耗时线程操作
    class MyThread extends Thread {


        @Override
        public void run() {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //int arg_1 = 20;
            int arg_1 = analysis_lastRecoedFile(lastRecordFileName);

            Message msg = new Message();
            msg.what = END_DATA_ANALYSIS;//数据分析成功
            msg.arg1 = arg_1;//将结果载入
            uiHandler.sendMessage(msg);
        }
        private int analysis_lastRecoedFile(String lastR){
            ReadStandard rs = new ReadStandard(lastR);
            return rs.readFile();
        }
        private void analysis(){
            ReadStandard rs = new ReadStandard("excellent");
            rs.readFile();
            rs.writeFilteredFile("excellent");

            ReadStandard rs_not = new ReadStandard("not_the_best");
            rs_not.readFile();
            //rs_not.writeFile("not_the_best");
            rs_not.writeFilteredFile("not_the_best");

            ReadStandard rs_try = new ReadStandard("try_again");
            rs_try.readFile();
            rs_try.writeFilteredFile("try_again");

        }




    }

    class delayThread extends Thread{
        @Override
        public void run() {
            try {
                Thread.sleep(3000);

                Message msg = new Message();
                msg.what = END_DELAY;//延时3秒结束
                uiHandler.sendMessage(msg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
