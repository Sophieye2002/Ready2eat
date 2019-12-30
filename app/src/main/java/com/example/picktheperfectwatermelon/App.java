package com.example.picktheperfectwatermelon;

import android.app.Application;
import android.util.Log;

import com.billy.android.loading.Gloading;
import com.example.picktheperfectwatermelon.AudioAnylysis.CAlData;
import com.example.picktheperfectwatermelon.loading.MyGloadingAdapter;
import com.zlw.main.recorderlib.utils.Logger;

public class App extends Application {
    private static final String TAG = "yin";
    private static App sInstance;
    //private CAlData myCalData;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        initGloading();

        //    myCalData = new CAlData();
    }

    public static App getInstance() {
        return sInstance;
    }

    public void initGloading() {
        //Init the default loading.xml status view for global usage wrap a
        //      customer Adapter implementation
        Gloading.debug(BuildConfig.DEBUG);
        Gloading.initDefault(new MyGloadingAdapter());
    }

    /**
     *
     * @return public CAlData getMyCalData() {
    return myCalData;
    }

    // private int i = 0;
    public void addMyCalData(byte[] data) {
    if (myCalData == null) {
    myCalData = new CAlData();
    }
    this.myCalData.addDatas(data);
    ////打印数据
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < data.length; i++) {
    if (data[i] != 0) {
    sb.append(data[i] + " ");

    }

    }
    Logger.i(TAG, "addMyCalData: 添加消息" + sb);
    // i++;
    }

    public void clearMyCalData() {
    myCalData = null;
    }
     */


}
