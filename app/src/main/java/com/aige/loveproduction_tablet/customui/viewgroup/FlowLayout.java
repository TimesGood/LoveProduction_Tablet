package com.aige.loveproduction_tablet.customui.viewgroup;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义ViewGroup流式布局，当一行不够下一个View的时候自动换行
 */
public class FlowLayout extends ViewGroup {
    private static final String TAG = "FlowLayout";
    private int mHorizontalSpacing = 0;//每个子横向间距
    private int mVerticalSpacing = 0;//每个子纵向间距
    private List<List<View>> allLines;//记录所有行，一行一个List，用于onLayout
    private List<Integer> lineHeights = new ArrayList<>();//记录每一行的行高onLayout

    //new时调用
    public FlowLayout(Context context) {
        super(context);
    }
    //XML布局时调用
    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    //设置主题时调用
    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 度量
     * 参考值不能直接使用，需要根据具体情况
     * 这里面有三个角色：自己、子、父
     * @param widthMeasureSpec 父给自己的参考值width
     * @param heightMeasureSpec 父给自己的参考值height
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /**
         * 度量子
         */
        //获取子数量
        int childCount = getChildCount();
        //保存一行中的所有子View
        List<View> lineView = new ArrayList<>();
        int lineWidthUsed = 0;//记录一行的宽
        int lineHeight = 0;//记录一行的高
        //获取自己的内边距
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        //解析父给予的规范的参考值
        int selfWidth = MeasureSpec.getSize(widthMeasureSpec);
        int selfHeight = MeasureSpec.getSize(heightMeasureSpec);
        //记录循环下来，自己需要多宽多高
        int parenNeedWidth = 0;
        int parenNeedHeight = 0;

        allLines = new ArrayList<>();
        for(int i = 0;i < childCount;i++) {
            View childAt = getChildAt(i);
            LayoutParams layoutParams = childAt.getLayoutParams();
            //把Params转变为MeasureSpec
            int childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec, paddingLeft + paddingRight, layoutParams.width);
            int childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec, paddingTop + paddingBottom, layoutParams.height);
            MeasureSpec.getMode(widthMeasureSpec);
            //得到实际的测量结果，调用measure
            childAt.measure(childWidthMeasureSpec,childHeightMeasureSpec);
            //子View调用onMeasure之后，就能得到实际的宽高
            int measuredWidth = childAt.getMeasuredWidth();
            int measuredHeight = childAt.getMeasuredHeight();
            //如果下一个子与之前记录的宽相加大于父给予的参考宽度，换行
            if(lineWidthUsed + measuredWidth + mHorizontalSpacing > selfWidth) {
                //换行前记录这行的宽高
                parenNeedWidth = Math.max(parenNeedWidth,lineWidthUsed+mHorizontalSpacing);
                parenNeedHeight += lineHeight+mVerticalSpacing;
                //换行前记录这一行的子及这一行的高度,给onLayout
                allLines.add(lineView);
                lineHeights.add(lineHeight);
                //一行的宽高及子归0
                lineView = new ArrayList<>();
                lineWidthUsed = 0;
                lineHeight = 0;
            }
            //如果是最后一行，因没有换行导致没有记录高，需要单独为这一行记录一下
            if(i == childCount-1) {
                //记录最后一行的高就好
                parenNeedHeight += lineHeight+mVerticalSpacing;
                //记录最后一行的子及高度,给onLayout
                allLines.add(lineView);
                lineHeights.add(lineHeight);
            }
            //保存一行中的View
            lineView.add(childAt);
            //一行的宽，每一个子宽+外边距
            lineWidthUsed = lineWidthUsed+measuredWidth + mHorizontalSpacing;
            //一行的高,前一个子高与后一个子高对比，取最高的那个作为这一行的高
            lineHeight = Math.max(lineHeight,measuredHeight);

        }
        /**
         * 度量自己
         */
        //获取父的参考结果
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        //根据参考结果选择父给予的参考值还是自己测量的值
        int realWidth = (widthMode == MeasureSpec.EXACTLY ? selfWidth : parenNeedWidth);
        int realHeight = (heightMode == MeasureSpec.EXACTLY ? selfHeight : parenNeedHeight);
        setMeasuredDimension(realWidth,realHeight);

    }

    //确定子的布局位置
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //获取自己的内边距，就是子第一个定点的坐标
        int paddingLeft = getPaddingLeft()+mHorizontalSpacing;
        int paddingTop = getPaddingTop();
        int size = allLines.size();
        for(int i = 0;i < size;i++) {
            List<View> LineViews = allLines.get(i);
            int lineHeight = lineHeights.get(i);
            for(View view : LineViews) {
                int left = paddingLeft;
                int top = paddingTop;
                int right = left + view.getMeasuredWidth();
                int bottom = top + view.getMeasuredHeight();
                view.layout(left,top,right,bottom);
                //前一个布局完之后，paddingLeft就需要增加，还有间距
                paddingLeft = right + mHorizontalSpacing;
            }
            //一行布局完之后，重置
            paddingLeft = getPaddingLeft() + mHorizontalSpacing;
            paddingTop += lineHeight + mVerticalSpacing;
        }
    }
    //对外，设置间距
    public void setMargin(int marginHorizontal,int marginVertical) {
        mHorizontalSpacing = dp2px(marginHorizontal);
        mVerticalSpacing = dp2px(marginVertical);
    }

    //把dp转为px
    public static int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp, Resources.getSystem().getDisplayMetrics());
    }
}
