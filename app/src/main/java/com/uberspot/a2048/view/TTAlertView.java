package com.uberspot.a2048.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.cb.ratingbar.CBRatingBar;
import com.uberspot.a2048.R;
import com.uberspot.a2048.helper.TLog;


/**
 * Created by liu on 2018/12/15.
 */

public class TTAlertView extends Dialog {
    public TTAlertView(@NonNull Context context, int style) {
        super(context, style);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    public static class Builder {

        private View mLayout;

        //        private ImageView mIcon;
        private TextView mTitle;
        private TextView mMessage;
        private TextView tvOK;
        private TextView tvCancel;
        private FrameLayout btnOK;
        private FrameLayout btnCancel;
        private CBRatingBar ratingBar;

        private AlertViewClickListener mClickListener;

        private TTAlertView mDialog;

        public Builder(Context context) {
            mDialog = new TTAlertView(context, R.style.Theme_AppCompat_Dialog);
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //加载布局文件
            mLayout = inflater.inflate(R.layout.simple_alert_view, null, false);
            //添加布局文件到 Dialog
            mDialog.addContentView(mLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            mTitle = (TextView) mLayout.findViewById(R.id.tv_title);
            mMessage = (TextView) mLayout.findViewById(R.id.tv_message);
            tvOK = (TextView) mLayout.findViewById(R.id.tv_ok);
            tvCancel = (TextView) mLayout.findViewById(R.id.tv_cancel);
            btnOK = (FrameLayout) mLayout.findViewById(R.id.btn_ok);
            btnCancel = (FrameLayout) mLayout.findViewById(R.id.btn_cancel);
            ratingBar = (CBRatingBar) mLayout.findViewById(R.id.rating_bar);
        }

        /**
         * 通过 ID 设置 Dialog 图标
         */
//        public Builder setIcon(int resId) {
//            mIcon.setImageResource(resId);
//            return this;
//        }

        /**
         * 用 Bitmap 作为 Dialog 图标
         */
//        public Builder setIcon(Bitmap bitmap) {
//            mIcon.setImageBitmap(bitmap);
//            return this;
//        }

        public Builder setClickListener(AlertViewClickListener listener) {
            this.mClickListener = listener;
            return this;
        }

        public Builder setBtnOK(@NonNull String okString) {
            tvOK.setText(okString);
            return this;
        }

        public Builder setBtnCancel(@NonNull String cancelString) {
            tvCancel.setText(cancelString);
            return this;
        }


        /**
         * 设置 Dialog 标题
         */
        public Builder setTitle(@NonNull String title) {
            mTitle.setText(title);
            mTitle.setVisibility(View.VISIBLE);
            return this;
        }

        /**
         * 设置 Message
         */
        public Builder setMessage(@NonNull String message) {
            mMessage.setText(message);
            return this;
        }

        public TTAlertView create() {
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                    if (mClickListener != null)
                        mClickListener.onButtonClicked(false);
                }
            });
            btnOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                    if (mClickListener != null)
                        mClickListener.onButtonClicked(true);
                }
            });
            ratingBar.setCanTouch(true);
            ratingBar.setPathDataId(R.string.round_star);
            ratingBar.setCoverDir(CBRatingBar.CoverDir.leftToRight);
            ratingBar.setStarSize(80);
            ratingBar.setStarCount(5);
            ratingBar.setStarProgress(100);
//            ratingBar.setStartColor(R.color.tile2048_color);
            ratingBar.setOnStarTouchListener(new CBRatingBar.OnStarTouchListener() {
                @Override
                public void onStarTouch(int touchCount) {
                    TLog.v("onStarTouch:" + touchCount);
                }
            });
            mDialog.setContentView(mLayout);
            mDialog.setCancelable(false);                //用户可以点击后退键关闭 Dialog
            mDialog.setCanceledOnTouchOutside(false);   //用户不可以点击外部来关闭 Dialog
            return mDialog;
        }

        public interface AlertViewClickListener {
            public void onButtonClicked(boolean isBtnOk);
        }
    }

    }
