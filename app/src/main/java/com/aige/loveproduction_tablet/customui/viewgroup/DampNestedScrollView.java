package com.aige.loveproduction_tablet.customui.viewgroup;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;

import androidx.core.widget.NestedScrollView;

/**
 * 自定义阻尼回弹滑动效果，阻尼相当于是移动速度
 * NestedScrollView是相当于支持嵌套滑动的ScrollView
 * ================================
 */
public class DampNestedScrollView extends NestedScrollView {

    // y方向上当前触摸点的前一次记录位置
    private int previousY = 0;
    // y方向上的触摸点的起始记录位置
    private int startY = 0;
    // y方向上的触摸点当前记录位置
    private int currentY = 0;
    // y方向上两次移动间移动的相对距离
    private int deltaY = 0;

    // 第一个子视图
    private View childView;

    // 用于记录childView的初始位置
    private Rect topRect = new Rect();

    //水平移动搞定距离
    private float moveHeight;

    public DampNestedScrollView(Context context) {
        this(context,null);

    }

    public DampNestedScrollView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DampNestedScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFillViewport(true);
    }

    //获取第一个子视图
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 0) {
            childView = getChildAt(0);
        }
    }
    //处理手指触摸滑动事件
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (null == childView) {
            return super.dispatchTouchEvent(event);
        }
        switch (event.getAction()) {
            //手指按下时
            case MotionEvent.ACTION_DOWN:
                //记录开始位置
                startY = (int) event.getY();
                previousY = startY;
                // 记录子View初始位置
                topRect.set(childView.getLeft(), childView.getTop(), childView.getRight(), childView.getBottom());
                moveHeight = 0;
                break;
            //手指移动时
            case MotionEvent.ACTION_MOVE:
                //记录当前触摸位置
                currentY = (int) event.getY();
                //取得当前触摸位置与上一次触摸位置的距离
                deltaY = currentY - previousY;
                //记录上一次触摸位置
                previousY = currentY;
                //canScrollVertically(1)是否能向上滚动
                //canScrollVertically(-1)是否能向下滚动
                //当有一个方向不能滚动的时候才开始做阻尼
                if(!canScrollVertically(-1)||!canScrollVertically(1)){
                    //移动距离
                    float distance = currentY - startY;
                    //由于上下滑动会出现负数的情况，这里如果是负数转换为正数
                    if (distance < 0) distance *= -1;

                    float damping = 0.5f;//阻尼值
                    float height = getHeight();
                    if (height != 0) {
                        if (distance > height) {
                            //手指滑动距离超过父View高度时，阻尼直接设为0，表示不能滑了
                            damping = 0;
                        } else {
                            //父View高度减去手指滑动距离再除以自己的到阻尼值，达到越滑越慢的效果
                            damping = (height - distance) / height;
                        }
                    }
                    //当前位置与开始位置为负数时，说明手指往上滑,这时阻尼可能会太大，再减一点阻尼
                    if (currentY - startY < 0) {
                        damping = 1 - damping;
                    }

                    //阻力值限制在0.3-0.5之间，平滑过度
                    damping *= 0.25;
                    damping += 0.25;

                    //当前位置与上一位置之间的距离乘阻尼值得到实际移动的距离
                    moveHeight = moveHeight + (deltaY * damping);
                    //然后就可以移动子View，形成阻尼效果了
                    childView.layout(topRect.left, (int) (topRect.top + moveHeight), topRect.right,
                            (int) (topRect.bottom + moveHeight));
                }
                break;
            //手指离开时
            case MotionEvent.ACTION_UP:
                if (!topRect.isEmpty()) {
                    //开始回移动画
                    upDownMoveAnimation();
                    //子控件回到初始位置
                    childView.layout(topRect.left, topRect.top, topRect.right,
                            topRect.bottom);
                }
                //重置一些参数
                startY = 0;
                currentY = 0;
                topRect.setEmpty();
                break;
        }

        return super.dispatchTouchEvent(event);
    }

    // 初始化上下回弹的动画效果
    private void upDownMoveAnimation() {
        TranslateAnimation animation = new TranslateAnimation(0.0f, 0.0f,
                childView.getTop(), topRect.top);
        animation.setDuration(600);
        animation.setFillAfter(true);
        //设置阻尼动画效果
        animation.setInterpolator(new DampInterpolator());
        childView.setAnimation(animation);
    }

    public static class DampInterpolator implements Interpolator {
        @Override
        public float getInterpolation(float input) {
            //input 时间（0~1）
            //返回值是进度（0~1）
            float move = (1 - input);
            //取其次方，实现越来越慢的效果
            return 1 - (float)Math.pow(move,4);
        }
    }
}