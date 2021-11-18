package com.aige.loveproduction_tablet.mvp.ui.customui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.aige.loveproduction_tablet.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DrawMprView extends View {
    private final Context mContext;
    private RectF mRectF2;
    private RectF mRectF;
    //画笔
    private Paint mPaint;
    //储存主图形的宽高
    private float mWidth = 0f,mHeight = 0f;
    //外面传递的数据
    private Map<String,List<Map<String,Float>>> oldMapData;
    private Map<String,List<Map<String,Float>>> newMapData;
    //缩放比例，当绘制的图形大于当前View的大小时，按比例缩放
    private float scale = 1f;
    //图形初始坐标
    private float centerX = 0f,centerY = 0f;
    private final PointF centerPoint = new PointF();
    //**********************************以下是实现图形移动、缩放功能的属性***********************************
    // 不同状态的表示：
    private static final int NONE = 0;//未接触
    private static final int DRAG = 1;//单指
    private static final int ZOOM = 2;//双指
    private int mode = NONE;
    // 定义第一个按下的点，两只接触点的重点，以及出事的两指按下的距离：
    //图形移动前的坐标
    private final PointF coordinate = new PointF();
    private final PointF startPoint = new PointF();
    private float oriDis = 1f;
    //*****************************************
    private final int MAX_LONG_PRESS_TIME=350;// 长按/双击最长等待时间
    private long oldTime;
    private long newTime = 0;

    public DrawMprView(Context context) {
        this(context,null);
    }

    public DrawMprView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        mContext = context;
    }
    //暴露给外面传入数据
    public void setData(Map<String, List<Map<String,Float>>> map) {
        if(map == null) return;
        oldMapData = map;
        Map<String,Float> cusMap = new HashMap<>();
        cusMap.put("textSize",16f);
        cusMap.put("offsetX",50f);
        cusMap.put("offsetY",12f);
        setCustom("text",cusMap);
        //克隆
        newMapData = new HashMap<>();
        newMapData = cloneData(oldMapData,1f);
        //恢复原始状态
        initProperty();
        //刷新
        invalidate();
    }
    //恢复一些属性
    private void initProperty() {
        scale = 1f;
        isdraw = false;
    }
    /**
     * 项目需求自定义一些属性
     * @param key 自定义的key
     * @param map 数据
     */
    private void setCustom(String key,Map<String,Float> map) {
        List<Map<String,Float>> list = new ArrayList<>();
        list.add(map);
        oldMapData.put(key,list);
    }
    /**
     * 深度克隆+数据调整缩放
     * @param data 克隆的数据
     * @param scale 缩放被克隆的数据
     * @return
     */
    private Map<String,List<Map<String, Float>>> cloneData(Map<String,List<Map<String, Float>>> data,float scale) {
        List<Map<String, Float>> rectangle;
        Map<String,List<Map<String, Float>>> maps = new HashMap<>();
        Map<String,Float> map;
        for (Map.Entry<String, List<Map<String, Float>>> entry : data.entrySet()) {
            rectangle = new ArrayList<>();
            for (Map<String, Float> mapEntry : entry.getValue()) {
                map = new HashMap<>();
                for(Map.Entry<String, Float> entries : mapEntry.entrySet()) {
                    map.put(entries.getKey(),entries.getValue()*scale);
                }
                rectangle.add(map);
            }
            maps.put(entry.getKey(),rectangle);
        }
        return maps;
    }
    /**
     * 初始画笔样式
     */
    public void initPaint() {
        if(mPaint == null) mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.FILL);  //设置画笔模式为填充
        mPaint.setStrokeWidth(10f); //设置画笔宽度
        mPaint.setXfermode(null);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //坐标点
        centerX = w/10f;
        centerY = h/1.2f;
        centerPoint.set(centerX,centerY);
    }
    //测量自己的大小
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height;
        if (widthMode == MeasureSpec.EXACTLY)
        {
            width = widthSize;
        } else
        {

            float textWidth = mRectF.width();
            int desired = (int) (getPaddingLeft() + textWidth + getPaddingRight());
            width = desired;
        }

        if (heightMode == MeasureSpec.EXACTLY)
        {
            height = heightSize;
        } else
        {
            float textHeight = mRectF.height();
            int desired = (int) (getPaddingTop() + textHeight + getPaddingBottom());
            height = desired;
        }
        setMeasuredDimension(width, height);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        parseData(canvas);


    }
    //是否是第一次画的开关
    private boolean isdraw = false;
    //记录初始坐标值
    private List<String> textList = new ArrayList<>();
    private void parseData(Canvas canvas) {
        if(newMapData == null) return;
        List<Map<String, Float>> rectangle = newMapData.get("rectangle");
        List<Map<String, Float>> bohrVert1 = newMapData.get("BohrVert1");
        List<Map<String, Float>> bohrVert2 = newMapData.get("BohrVert2");
        List<Map<String, Float>> bohrHoriz1 = newMapData.get("BohrHoriz1");
        List<Map<String, Float>> cutting1 = newMapData.get("Cutting1");
        List<Map<String, Float>> text = newMapData.get("text");
        Map<String, Float> textMap = text.get(0);
        if(rectangle != null) {
            for(Map<String, Float> map : rectangle) {
                initPaint();
                mPaint.setColor(ContextCompat.getColor(mContext, R.color.draw_brown));
                mWidth = map.get("BSX");
                mHeight = map.get("BSY");
                drawRectangle(canvas,centerPoint.x, centerPoint.y, mWidth, mHeight);
            }
        }
        if(cutting1 != null) {
            int pathCount = 1;
            for(Map<String, Float> map : cutting1) {
                int size = map.size()/2;
                if(size == 2) {
                    initPaint();
                    mPaint.setColor(ContextCompat.getColor(mContext,R.color.draw_green));
                    mPaint.setStrokeWidth(5f);
                    mPaint.setStyle(Paint.Style.STROKE);
                    drawPathQuad(canvas,map);
                }else if(pathCount == cutting1.size()){
                    drawPath(canvas,map,textMap.get("textSize"),textMap.get("offsetX"),textMap.get("offsetY"));
                }
                pathCount++;
            }
        }
        if(bohrHoriz1 != null) {
            for(Map<String, Float> map : bohrHoriz1) {
                initPaint();
                mPaint.setColor(ContextCompat.getColor(mContext, R.color.white));
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setStrokeWidth(1f);
                drawRectangle2(canvas,map.get("XA"),map.get("YA"),map.get("DU"),map.get("TI"));
            }
        }
        if(bohrVert1 != null) {
            int i = 0;
            for(Map<String, Float> map : bohrVert1) {
                initPaint();
                mPaint.setColor(ContextCompat.getColor(mContext,R.color.grey));
                drawArc(canvas,0,360,map.get("DU"),map.get("XA"),map.get("YA"));
                if(!isdraw) {
                    textList.add(i,"x="+map.get("XA")+",y="+map.get("YA"));
                }
                drawText(canvas, textList.get(i), textMap.get("textSize"), map.get("XA"), map.get("YA"), 0f, textMap.get("offsetY"));
                i++;
            }
        }
        if(bohrVert2 != null) {
            for(Map<String, Float> map : bohrVert2) {
                Float ti = map.get("TI");
                Float xa = map.get("XA");
                Float ya = map.get("YA");
                if(ti == null || xa == null || ya == null) break;
                initPaint();
                mPaint.setColor(ContextCompat.getColor(mContext,R.color.grey));
                drawArc(canvas,0,360,ti,xa,ya);
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setStrokeWidth(1f);
                mPaint.setColor(ContextCompat.getColor(mContext,R.color.black));
                drawArc(canvas,45,90,ti,xa,ya);
                drawArc(canvas,225,90,ti,xa,ya);
            }
        }

        isdraw = true;
    }

    /**
     * 画矩形
     * @param x 矩形左下角x坐标
     * @param y 矩形左下角y坐标
     * @param width 矩形宽高
     * @param height 矩形宽高
     */
    private void drawRectangle(Canvas canvas,float x,float y,float width,float height) {
        if(mRectF2 == null) mRectF2 = new RectF();
        PointF xy = getXY(width/2, height/2);
        mRectF2.left = x;
        mRectF2.bottom = y;
        mRectF2.right = xy.x+width/2;
        mRectF2.top = xy.y-height/2;
        canvas.drawRect(mRectF2,mPaint);

    }

    /**
     * 绘制侧钉子
     * @param x 钉子打入点x轴
     * @param y 钉子打入点y轴
     * @param d 钉子直径
     * @param depth 钉子深度
     */
    private void drawRectangle2(Canvas canvas,float x,float y,float d,float depth) {
        if(mRectF2 == null) mRectF2 = new RectF();
        PointF xy = getXY(x, y);
        d /= 2;
        if(x==0) {
            //当钉子在左
            mRectF2.left = xy.x;
            mRectF2.bottom = xy.y-d;
            mRectF2.right = xy.x+depth;
            mRectF2.top = xy.y+d;
        }else if(y == 0) {
            //当钉子在下
            mRectF2.left = xy.x-d;
            mRectF2.bottom = xy.y-depth;
            mRectF2.right = xy.x+d;
            mRectF2.top = xy.y;
        }else if(x == mWidth){
            //当钉子在右
            mRectF2.left = xy.x-depth;
            mRectF2.bottom = xy.y-d;
            mRectF2.right = xy.x;
            mRectF2.top = xy.y+d;
        }else if(y == mHeight) {
            //当钉子在上
            mRectF2.left = xy.x-d;
            mRectF2.bottom = xy.y;
            mRectF2.right = xy.x+d;
            mRectF2.top = xy.y+depth;
        }
        canvas.drawRect(mRectF2,mPaint);
    }

    /**
     * 画弧，正钉子
     * @param startAngle 开始角度
     * @param sweepAngle 画多少度
     * @param d 原点直径
     * @param x 圆点x坐标
     * @param y 圆点y坐标
     */
    private void drawArc(Canvas canvas,float startAngle,float sweepAngle,float d,float x,float y) {
        d /= 2;
        if(mRectF == null) mRectF = new RectF();
        PointF xy = getXY(x, y);
        mRectF.left = xy.x-d;
        mRectF.bottom = xy.y+d;
        mRectF.right = xy.x+d;
        mRectF.top = xy.y-d;
        canvas.drawArc(mRectF,startAngle,sweepAngle,true,mPaint);
    }

    /**
     * 绘制文字
     * @param text 文字
     * @param size 字体大小
     * @param x 文字x轴
     * @param y 文字y轴
     * @param offsetX X轴偏移 正向右偏移，负向左偏移
     * @param offsetY Y轴偏移 正向上偏移，负向下偏移
     */
    private void drawText(Canvas canvas,String text,float size,float x,float y,float offsetX,float offsetY) {
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(1f);
        mPaint.setTextSize(size);//字体大小
        mPaint.setTextAlign(Paint.Align.CENTER);
        //获取文字的高度
        Paint.FontMetrics fontMetrics=mPaint.getFontMetrics();
        float distance=(fontMetrics.bottom - fontMetrics.top)/2 - fontMetrics.bottom;
        PointF xy = getXY(x, y);
        canvas.drawText(text,xy.x+offsetX,xy.y+distance-offsetY,mPaint);
    }

    /**
     * 画直线
     * @param startX 开始X轴
     * @param startY 开始Y轴
     * @param stopX 结束X轴
     * @param stopY 结束Y轴
     */
    private void drawLine(Canvas canvas,float startX,float startY,float stopX,float stopY) {
        PointF startXY = getXY(startX, startY);
        PointF stopXY = getXY(stopX, stopY);
        canvas.drawLine(startXY.x,startXY.y,stopXY.x,stopXY.y,mPaint);
    }

    private final Path mPath = new Path();
    private final List<String> pathList = new ArrayList<>();
    /**
     *画路径
     * @param map 路径数据
     */
    private void drawPath(Canvas canvas,Map<String,Float> map,float size,float offsetX,float offsetY) {
        mPath.reset();
        boolean flag = true;
        for(int i = 0 ; i < map.size()/2;i++) {
            Float x = map.get("X"+i);
            Float y = map.get("Y"+i);
            PointF xy = getXY(x, y);
            if(flag) {
                mPath.moveTo(xy.x,xy.y);
                if(!isdraw) pathList.add(i,"");
                flag = false;
            }else{
                mPath.lineTo(xy.x,xy.y);
                if(!isdraw) pathList.add(i,"x="+x+",y="+y);

            }
            mPaint.setColor(ContextCompat.getColor(mContext,R.color.draw_green));
            //字体位置
            if(x == 0 && y != 0 || (x != 0 && y != 0)) {
                drawText(canvas,pathList.get(i),size,x,y,0,offsetY);
            }
            if(x != 0 && y == 0 || (x == 0 && y == 0)) {
                drawText(canvas,pathList.get(i),size,x,y,0,-offsetY);
            }

        }
        initPaint();
        mPaint.setColor(ContextCompat.getColor(mContext,R.color.draw_green));
        mPaint.setStrokeWidth(5f);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(mPath, mPaint);
    }

    /**
     * 绘制二阶贝塞尔曲线
     * @param map 二阶贝塞尔数据
     */
    private void drawPathQuad(Canvas canvas,Map<String,Float> map) {
        mPath.reset();
        Float x0 = map.get("X0");
        Float y0 = map.get("Y0");
        Float x1 = map.get("X1");
        Float y1 = map.get("Y1");
        float abs = Math.abs(x0 - x1);
        PointF xy0 = getXY(x0, y0);
        PointF xy1 = getXY(x1, y1);
        PointF xy = null;
        if((x0 == 0 && y0 != 0 && x1 != 0 && y1 == 0) || (x1 == 0 && y1 != 0 && x0 != 0 && y0 == 0)) {
            //左下角
            xy = getXY(x0, y0-abs);
        }else if((x0 != 0 && y0 == 0 && x1 != 0 && y1 != 0) || (x1 != 0 && y1 == 0 && x0 != 0 && y0 != 0)) {
            //右下角
            xy = getXY(x0+abs, y0);
        }else if((x0 != 0 && y0 != 0 && x1 != 0 && y1 != 0)) {
            //右上角
            xy = getXY(x0, y0+abs);
        }else {
            //左上角
            xy = getXY(x0-abs, y0);
        }
        mPath.moveTo(xy0.x, xy0.y);
        // 二次贝塞尔曲线
        mPath.quadTo(xy.x, xy.y, xy1.x, xy1.y);
        canvas.drawPath(mPath,mPaint);
    }
    /**
     * 根据传递过来的xy轴坐标，转换为当前画布实际坐标
     */
    private PointF getXY(float x,float y) {
        return new PointF(centerPoint.x+x,centerPoint.y-y);
    }
    @Override
    public boolean performClick() {
        return super.performClick();
    }
    /**
     * 手指触摸移动、缩放
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(oldMapData == null) return false;
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            //单指按下
            case MotionEvent.ACTION_DOWN:
                //获取手指按下的位置
                startPoint.set(event.getX(),event.getY());
                //获取当前图形的坐标
                coordinate.set(centerPoint.x,centerPoint.y);
                oldTime = newTime;
                newTime = System.currentTimeMillis();
                mode = DRAG;
                break;
            // 双指按下
            case MotionEvent.ACTION_POINTER_DOWN:
                //获取两手指之间的距离
                oriDis = distance(event);
                if (oriDis > 10f) {
                    //获取两手指之间的x，y
                    mode = ZOOM;
                }
                //设置前一次缩放之后的原始数据
                oldMapData.putAll(cloneData(oldMapData, scale));
                break;
            //手指放开
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                performClick();
                if(newTime - oldTime < MAX_LONG_PRESS_TIME) {
                    //双击恢复坐标点
                    centerPoint.set(centerX,centerY);
                }
                mode = NONE;
                break;
            case MotionEvent.ACTION_MOVE:
                oldTime = 0;
                if(mode == DRAG) {
                    //更改图形坐标
                    centerPoint.x = coordinate.x+(event.getX()-startPoint.x);
                    centerPoint.y = coordinate.y+(event.getY()-startPoint.y);
                }else if(mode == ZOOM) {
                    //获取新的俩手指距离
                    float newDist = distance(event);
                    if (newDist > 10f) {
                        scale = (newDist / oriDis);
                        newMapData.putAll(cloneData(oldMapData, scale));
                    }
                }
                break;
        }

        invalidate();
        return true;
    }
    // 计算两个触摸点之间的距离
    private float distance(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float)Math.sqrt(x * x + y * y);
    }
    // 计算两个触摸点的中点
    private PointF middle(MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        return new PointF(x / 2, y / 2);
    }
}
