package com.example.azadljy.simulateturnplate.TurnPlate;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.SystemClock;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：Ljy on 2017/2/14.
 * 邮箱：enjoy_azad@sina.com
 */

public class TurnPlateSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    //转盘半径
    private int radius;
    //圆心坐标
    private int centerX;
    private int centerY;
    private Paint paint;
    //盘面画笔
    private Paint arcPaint;
    private SurfaceHolder holder;
    private Canvas canvas;
    private Thread thread;
    private boolean isThredRunning;
    private RectF rectF;
    //开始的角度
    private float startAngle = 0;
    //盘面所占的比例
    private List<Float> proportions;
    //盘面颜色
    private List<Integer> colors;
    public static final float CIRCLE_ANGLE = 360;
    private TurnPlate turnPlate;
    //加速或减速状态
    private static boolean isUpspeed;
    private static int state = -1;
    //加速状态
    public static final int STATE_SPEEDUP = 0;
    //减速状态
    public static final int STATE_SPEEDCUT = 1;
    //停止状态
    public static final int STATE_STOP = -1;
    //状态改变的起始时间
    private long flagTime;

    @IntDef({STATE_SPEEDUP, STATE_SPEEDCUT, STATE_STOP})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {
    }

    public TurnPlateSurfaceView(Context context) {
        super(context);
        init();
    }

    public TurnPlateSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TurnPlateSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.STROKE);
        arcPaint = new Paint();
        arcPaint.setAntiAlias(true);
        arcPaint.setDither(true);
        arcPaint.setStyle(Paint.Style.FILL);
        holder = getHolder();
        holder.addCallback(this);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //计算半径
        radius = Math.min(getMeasuredWidth(), getMeasuredHeight()) / 2 - 10;
        centerX = getMeasuredWidth() / 2;
        centerY = getMeasuredHeight() / 2;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.e("TAG", "run:创建了");
        //模拟转盘
        proportions = new ArrayList<>();
        proportions.add(0.2f);
        proportions.add(0.2f);
        proportions.add(0.2f);
        proportions.add(0.1f);
        proportions.add(0.3f);
        colors = new ArrayList<>();
        colors.add(Color.BLUE);
        colors.add(Color.GREEN);
        colors.add(Color.RED);
        colors.add(Color.YELLOW);
        colors.add(Color.parseColor("#FFAD86"));
        //实例化转盘
        turnPlate = new TurnPlate(0.1f);
        //盘面的范围控制
        rectF = new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
        isThredRunning = true;
        isUpspeed = true;
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.e("TAG", "run:销毁了");
        isThredRunning = false;
    }

    @Override
    public void run() {
        Log.e("TAG", "run:线程执行");
        while (isThredRunning) {
            //保证绘制不低于50毫秒
            long start = System.currentTimeMillis();
            draw();
            long end = System.currentTimeMillis();
            if ((end - start) < 50) {
                //休眠到50毫秒
                SystemClock.sleep(50 - (end - start));
            }
            long end1 = System.currentTimeMillis();
            switch (state) {
                case STATE_STOP:
                    break;
                case STATE_SPEEDUP:
                    if (end1 - flagTime > 0) {
                        startAngle += turnPlate.getAngularSpeed((end1 - flagTime) / 1000f, true);
//                        Log.e("TAG", "run: " + end1 + "-----" + flagTime);
                        Log.e("TAG", "run: " + turnPlate.getAngularSpeed((end1 - flagTime) / 1000f, true));
                    }
                    break;
                case STATE_SPEEDCUT:
                    if (end1 - flagTime > 0) {
                        startAngle += turnPlate.getAngularSpeed((end1 - flagTime) / 1000f, false);
//                        Log.e("TAG", "run: " + end1 + "-----" + flagTime);
//                        Log.e("TAG", "run: " + (end1 - flagTime));
                        Log.e("TAG", "run: " + turnPlate.getAngularSpeed((end1 - flagTime) / 1000f, false));
                    }
                    break;
                default:

            }

//            Log.e("TAG", "run: " + turnPlate.getAngularSpeed((end1 - flagTime) / 1000f, isUpspeed));
//            Log.e("TAG", "run: " + (end1 - start1) / 1000);
        }
    }

    private void draw() {
        try {
            canvas = holder.lockCanvas();
            if (canvas != null) {
                canvas.drawColor(0xffffffff);
                for (int i = 0; i < proportions.size(); i++) {
                    arcPaint.setColor(colors.get(i));
                    canvas.drawArc(rectF, startAngle, CIRCLE_ANGLE * proportions.get(i), true, arcPaint);
                    startAngle += CIRCLE_ANGLE * proportions.get(i);
                }
            }
        } catch (Exception e) {

        } finally {
            if (canvas != null) {
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }

    public boolean isUpspeed() {
        return isUpspeed;
    }

    public void setUpspeed(boolean upspeed) {
        isUpspeed = upspeed;
    }

    public void changeState(@State int state) {
        this.state = state;
        flagTime = System.currentTimeMillis();

    }
}
