package com.aige.loveproduction_tablet.mvp.ui.dialog;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.StringRes;

import com.aige.loveproduction_tablet.R;
import com.aige.loveproduction_tablet.base.BaseDialog;


//下载进度弹窗
public class DownloadDialog {
    public static final class Builder
            extends CommonDialog.Builder<DownloadDialog.Builder> {
        private ProgressBar download_bar;
        private DownloadDialog.OnListener mListener;
        private TextView download_message;
        public Builder(Context context) {
            super(context);
            setCustomView(R.layout.download_dialog);
            download_bar = findViewById(R.id.download_bar);
            download_message = findViewById(R.id.download_message);
        }
        public Builder setMessage(@StringRes int id) {
            return setMessage(getString(id));
        }
        public Builder setMessage(CharSequence text) {
            download_message.setText(text);
            return this;
        }
        //进度条
        public void setProgress(int progress) {
            download_bar.setProgress(progress);
        }
        public Builder setListener(DownloadDialog.OnListener listener) {
            mListener = listener;
            return this;
        }
        //@SingleClick
        @Override
        public void onClick(View view) {
            int viewId = view.getId();
            if (viewId == R.id.tv_ui_confirm) {
                autoDismiss();
                if (mListener != null) {
                    mListener.onConfirm(getDialog());
                }
            } else if (viewId == R.id.tv_ui_cancel) {
                autoDismiss();
                if (mListener != null) {
                    mListener.onCancel(getDialog());
                }
            }
        }
    }

    public interface OnListener {

        /**
         * 点击确定时回调
         */
        void onConfirm(BaseDialog dialog);

        /**
         * 点击取消时回调
         */
        default void onCancel(BaseDialog dialog) {}
    }
}
