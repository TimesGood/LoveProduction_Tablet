package com.aige.loveproduction_tablet.mvp.ui.customui.viewgroup;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.WindowManager;

import androidx.core.content.ContextCompat;

import com.aige.loveproduction_tablet.R;
import com.aige.loveproduction_tablet.util.CommonUtils;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.CameraPreview;
import com.journeyapps.barcodescanner.ViewfinderView;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义二维码扫描框
 */
public class CustomViewFinderView extends ViewfinderView {
    private RectF buttonRect;
    /**
     * 重绘时间间隔
     */
    private final static long CUSTOME_ANIMATION_DELAY = 16;
    /**
     * 边角线颜色
     */
    private final int mLineColor = ContextCompat.getColor(getContext(),R.color.blue);
    /**边角线厚度 (建议使用dp)*/
    private final float mLineDepth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2f, getResources().getDisplayMetrics());
    /**"边角线长度/扫描边框长度"的占比 (比例越大，线越长)*/
    private final float mLineRate = 0.05f;
    //*************************扫描线****************************
    /**扫描线起始位置*/
    private int mScanLinePosition = 0;
    /**扫描线每次重绘的移动距离*/
    private final float mScanLineDy = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3f, getResources().getDisplayMetrics());
    /**线性梯度位置*/
    private final float[] mPositions = {0f, 0.5f, 1f};
    /**扫描线厚度*/
    private final float mScanLineDepth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2f, getResources().getDisplayMetrics());
    //扫描线渐变色
    private final int laserColor_center = ContextCompat.getColor(getContext(),R.color.blue);
    private final int laserColor_light = ContextCompat.getColor(getContext(),R.color.white2);
    private final int[] mScanLineColor = {laserColor_light, laserColor_center, laserColor_light};
    //*************************扫描线****************************

    //扫描框对象
    private CameraPreview cameraPreview;
    public CustomViewFinderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    public void setCameraPreview(CameraPreview view) {
        this.cameraPreview = view;
        view.addStateListener(new CameraPreview.StateListener() {
            @Override
            public void previewSized() {
                refreshSizes();
                invalidate();
            }

            @Override
            public void previewStarted() {

            }

            @Override
            public void previewStopped() {

            }

            @Override
            public void cameraError(Exception error) {

            }

            @Override
            public void cameraClosed() {

            }
        });
    }
    @Override
    protected void refreshSizes() {
        if(cameraPreview == null) {
            return;
        }
        //获取扫描框Rect属性
        Rect framingRect = cameraPreview.getFramingRect();
        Rect previewFramingRect = cameraPreview.getPreviewFramingRect();
        if(framingRect != null && previewFramingRect != null) {
            this.framingRect = framingRect;
            this.previewFramingRect = previewFramingRect;
        }
    }
    @SuppressLint("DrawAllocation")
    @Override
    public void onDraw(Canvas canvas) {
        refreshSizes();
        if (framingRect == null || previewFramingRect == null) {
            return;
        }
        int width = getWidth();
        int height = getHeight();
        if (resultBitmap != null) {
            paint.setColor(resultColor);
        } else {
            paint.setColor(maskColor);
        }
        //绘制扫描框外暗部
        canvas.drawRect(0f, 0f, width, framingRect.top, paint);//上暗部
        canvas.drawRect(0f, (framingRect.bottom + 1), width, height, paint);//下暗部
        canvas.drawRect(0f, framingRect.top, framingRect.left, (framingRect.bottom + 1), paint);//左暗部
        canvas.drawRect((framingRect.right + 1), framingRect.top, width, (framingRect.bottom + 1), paint);//右暗部

        //drawButton(canvas, frame);
        ////绘制4个角
        paint.setColor(mLineColor);
        //左上-横线
        canvas.drawRect(framingRect.left - mLineDepth, framingRect.top - mLineDepth, framingRect.left + framingRect.width() * mLineRate, framingRect.top, paint);
        //左上-纵线
        canvas.drawRect(framingRect.left - mLineDepth, framingRect.top, framingRect.left, framingRect.top + framingRect.height() * mLineRate, paint);
        //右上-横线
        canvas.drawRect(framingRect.right - framingRect.width() * mLineRate, framingRect.top - mLineDepth, framingRect.right + mLineDepth, framingRect.top, paint);
        //右上-纵线
        canvas.drawRect(framingRect.right, framingRect.top - mLineDepth, framingRect.right + mLineDepth, framingRect.top + framingRect.height() * mLineRate, paint);
        //左下-横线
        canvas.drawRect(framingRect.left - mLineDepth, framingRect.bottom, framingRect.left + framingRect.width() * mLineRate, framingRect.bottom + mLineDepth, paint);
        //左下-纵线
        canvas.drawRect(framingRect.left - mLineDepth, framingRect.bottom - framingRect.height() * mLineRate, framingRect.left, framingRect.bottom, paint);
        //右下-横线
        canvas.drawRect(framingRect.right - framingRect.width() * mLineRate, framingRect.bottom, framingRect.right + mLineDepth, framingRect.bottom + mLineDepth, paint);
        //右下-纵线
        canvas.drawRect(framingRect.right, framingRect.bottom - framingRect.height() * mLineRate, framingRect.right + mLineDepth, framingRect.bottom + mLineDepth, paint);
        // 绘制扫描线
        mScanLinePosition += mScanLineDy;
        if (mScanLinePosition > framingRect.height()) {
            mScanLinePosition = 0;
        }
        //添加扫描线颜色线性梯度
        LinearGradient mLinearGradient = new LinearGradient(framingRect.left, framingRect.top + mScanLinePosition, framingRect.right, framingRect.top + mScanLinePosition, mScanLineColor, mPositions, Shader.TileMode.CLAMP);
        paint.setShader(mLinearGradient);//设置着色器
        canvas.drawRect(framingRect.left, (framingRect.top + mScanLinePosition), framingRect.right, framingRect.top + mScanLinePosition + mScanLineDepth, paint);
        paint.setShader(null);//清除着色器
        //执行重绘
        postInvalidateDelayed(CUSTOME_ANIMATION_DELAY, framingRect.left, framingRect.top, framingRect.right, framingRect.bottom);

    }

    //个性化按钮
    private void drawButton(Canvas canvas, Rect mScanRect) {
        Paint buttonPaint = new Paint();
        buttonPaint.setAntiAlias(true);
        buttonPaint.setColor(ContextCompat.getColor(getContext(),R.color.white));
        buttonPaint.setStrokeWidth(1f);
        buttonPaint.setStyle(Paint.Style.STROKE);
        int height = CommonUtils.dp2px(getContext(), 40);
        int left = mScanRect.left + (mScanRect.right - mScanRect.left) / 6;
        int top = mScanRect.bottom + CommonUtils.dp2px(getContext(), 48);
        int right = mScanRect.right - (mScanRect.right - mScanRect.left) / 6;
        int bottom = mScanRect.bottom + CommonUtils.dp2px(getContext(), 48) + height;
        buttonRect = new RectF(left, top, right, bottom);
        canvas.drawRoundRect(buttonRect, CommonUtils.dp2px(getContext(), 20), CommonUtils.dp2px(getContext(), 20), buttonPaint);
        buttonPaint.setColor(ContextCompat.getColor(getContext(),R.color.blue));
        buttonPaint.setTextSize(CommonUtils.dp2px(getContext(), 14));
        buttonPaint.setStyle(Paint.Style.FILL);
        buttonPaint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetricsInt fontMetrics = buttonPaint.getFontMetricsInt();
        int baseLine = (int) ((buttonRect.top + buttonRect.bottom - fontMetrics.top - fontMetrics.bottom) / 2);
        canvas.drawText("个性化需求", buttonRect.centerX(), baseLine, buttonPaint);
    }

}