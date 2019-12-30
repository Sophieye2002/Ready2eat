package com.example.picktheperfectwatermelon.loading;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.billy.android.loading.Gloading;

public class BaseActivity extends AppCompatActivity {
    protected Gloading.Holder mHolder;



    //设置按钮字体样式
    public void setMgr(Button bt){
        AssetManager mgr = getAssets();
        Typeface helveticaTf = Typeface.createFromAsset(mgr,"fonts/Helvetica.ttf");
        bt.setTypeface(helveticaTf);
    }
    //设置textView字体样式
    public void setMgr(TextView tv){
        AssetManager mgr = getAssets();
        Typeface helveticaTf = Typeface.createFromAsset(mgr,"fonts/Helvetica.ttf");
        tv.setTypeface(helveticaTf);
    }




    /**
     * make a Gloading.Holder wrap with current activity by default
     * override this method in subclass to do special initialization
     */
    protected void initLoadingStatusViewIfNeed() {
        if (mHolder == null) {
            //bind status view to activity root view by default
            mHolder = Gloading.getDefault().wrap(this).withRetry(new Runnable() {
                @Override
                public void run() {
                    onLoadRetry();
                }
            });
        }
    }

    protected void onLoadRetry() {
        // override this method in subclass to do retry task
    }

    public void showLoading() {
        initLoadingStatusViewIfNeed();
        mHolder.showLoading();
    }

    public void showLoadSuccess() {
        initLoadingStatusViewIfNeed();
        mHolder.showLoadSuccess();
    }

    public void showLoadFailed() {
        initLoadingStatusViewIfNeed();
        mHolder.showLoadFailed();
    }

    public void showEmpty() {
        initLoadingStatusViewIfNeed();
        mHolder.showEmpty();
    }

}
