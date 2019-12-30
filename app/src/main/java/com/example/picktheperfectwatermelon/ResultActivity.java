package com.example.picktheperfectwatermelon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.example.picktheperfectwatermelon.loading.BaseActivity;

public class ResultActivity extends BaseActivity {

    private NumberProgressBar result_waterCon_Pro,result_sweetness_Pro,result_texture_Pro;
    private TextView result_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        initView();
        Intent intent = getIntent();
        setData(intent.getIntExtra("waterCon",0),
                intent.getIntExtra("sweetness",0),
                intent.getIntExtra("texture",100));
    }

    private void setData(int waterCon,int sweetness,int texture){
        result_waterCon_Pro.setProgress(waterCon);
        result_sweetness_Pro.setProgress(sweetness);
        result_texture_Pro.setProgress(texture);

        if(0 < waterCon ){
            if(waterCon < 40){
                result_tv.setText("TRY AGAIN :(");
            }else if(waterCon < 70){
                result_tv.setText("NOT THE BEST...");
            }else {
                result_tv.setText("EXCELLENT CHOICE!");
            }
        }

    }

    private void initView(){
        result_waterCon_Pro = (NumberProgressBar)findViewById(R.id.result_waterCon_Pro);
        result_sweetness_Pro = (NumberProgressBar)findViewById(R.id.result_sweetness_Pro);
        result_texture_Pro = (NumberProgressBar)findViewById(R.id.result_texture_Pro);
        result_tv = (TextView)findViewById(R.id.result_tv);
        setMgr(result_tv);//设置结果框字体样式

        result_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ResultActivity.this,MainActivity.class));
                finish();
            }
        });
    }

}
