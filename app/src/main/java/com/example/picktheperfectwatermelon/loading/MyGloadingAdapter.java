package com.example.picktheperfectwatermelon.loading;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.billy.android.loading.Gloading;
import com.example.picktheperfectwatermelon.R;

import static com.billy.android.loading.Gloading.STATUS_LOADING;
import static com.billy.android.loading.Gloading.STATUS_LOAD_FAILED;
import static com.billy.android.loading.Gloading.STATUS_LOAD_SUCCESS;
import static com.example.picktheperfectwatermelon.loading.Util.isNetworkConnected;

public class MyGloadingAdapter implements Gloading.Adapter {
    @Override
    public View getView(Gloading.Holder holder, View convertView, int status) {
        GlobalLoadingStatusView loadingStatusView = null;
        //convertView为可重用的布局
        //Holder中缓存了各状态下对应的View
        //	如果status对应的View为null，则convertView为上一个状态的View
        //	如果上一个状态的View也为null，则convertView为null
        if (convertView != null && convertView instanceof GlobalLoadingStatusView) {
            loadingStatusView = (GlobalLoadingStatusView) convertView;
        }
        if (loadingStatusView == null) {
            loadingStatusView = new GlobalLoadingStatusView(holder.getContext(), holder.getRetryTask());
        }
        loadingStatusView.setStatus(status);
        Object data = holder.getData();
        //show or not show msg view
        boolean hideMsgView = Constants.HIDE_LOADING_STATUS_MSG.equals(data);
        loadingStatusView.setMsgViewVisibility(!hideMsgView);


        return loadingStatusView;
    }
    class GlobalLoadingStatusView extends LinearLayout implements View.OnClickListener {


        private final TextView mTextView;
        private final Runnable mRetryTask;
        private final ImageView mImageView;

        public GlobalLoadingStatusView(Context context, Runnable retryTask) {
            super(context);
            //初始化LoadingView
            //如果需要支持点击重试，在适当的时机给对应的控件添加点击事件
            setOrientation(VERTICAL);
            setGravity(Gravity.CENTER_HORIZONTAL);
            LayoutInflater.from(context).inflate(R.layout.view_global_loading_status, this, true);
            mImageView = findViewById(R.id.image);
            mTextView = findViewById(R.id.text);
            this.mRetryTask = retryTask;
            setBackgroundColor(0x948585);//59
        }

        public void setMsgViewVisibility(boolean visible) {
            mTextView.setVisibility(visible ? VISIBLE : GONE);
        }
        public void setStatus(int status) {
            //设置当前的加载状态：加载中、加载失败、空数据等
            //其中，加载失败可判断当前是否联网，可现实无网络的状态
            //		属于加载失败状态下的一个分支,可自行决定是否实现
            boolean show = true;
            View.OnClickListener onClickListener = null;
            int image = R.drawable.in_load;
            int str = R.string.str_none;
            switch (status) {
                case STATUS_LOAD_SUCCESS: show = false; break;
                case STATUS_LOADING: str = R.string.loading; break;
                case STATUS_LOAD_FAILED:
                    str = R.string.load_failed;
                    image = R.drawable.icon_failed;
                    Boolean networkConn = isNetworkConnected(getContext());
                    if (networkConn != null && !networkConn) {
                        str = R.string.load_failed_no_network;
                        image = R.drawable.icon_no_wifi;
                    }
                    onClickListener = this;
                    break;
                default: break;
            }
            mImageView.setImageResource(image);
            setOnClickListener(onClickListener);
            mTextView.setText(str);
            setVisibility(show ? View.VISIBLE : View.GONE);

        }

        @Override
        public void onClick(View view) {
            if (mRetryTask != null) {

                mRetryTask.run();
            }
        }
    }

}
