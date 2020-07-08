package com.wzf.md.surface;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

public class MSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable{

    private final int DEFAULT_PAINT_WIDTH = 2;
    private final int DEFAULT_PAINT_COLOR = Color.RED;
    private int currentPaintColor = DEFAULT_PAINT_COLOR;
    private int currentPaintWidth = DEFAULT_PAINT_WIDTH;

    float scaleHeight = 1.0f;

    List<KeyPath> paths = new ArrayList<>();


    SurfaceHolder holder;
    public MSurfaceView(Context context) {
        this(context, null);
    }

    public MSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        holder = this.getHolder();
        holder.addCallback(this);
    }



    private void draw() {
//        Canvas canvas = holder.lockCanvas(new Rect(0,0, getWidth(),getHeight()));
        Canvas canvas = holder.lockCanvas();
        canvas.drawColor(0XFFDFCAB5);
        for (KeyPath path : paths) {
            canvas.drawPath(path.path, path.paint);
        }
        holder.unlockCanvasAndPost(canvas);
    }

    private float mLastX, mLastY;//上次的坐标
//    private boolean isDrawing = false;
    private Path mPath;
    int count = 0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
//                isDrawing = true;
                mLastX = x;
                mLastY = y;
                mPath = new Path();
                count = 0;
                mPath.moveTo(mLastX, mLastY);
                paths.add(new KeyPath( getPaint(), mPath));
                draw();
                break;
            case MotionEvent.ACTION_MOVE:
                count ++;
                Log.i("record : ", x + ":" + y + "->" + count);

                float dx = Math.abs(x - mLastX);
                float dy = Math.abs(y - mLastY);
                if (dx >= 3 || dy >= 3) {
                    mPath.quadTo(mLastX, mLastY, (mLastX + x) / 2, (mLastY + y) / 2);
                }
                mLastX = x;
                mLastY = y;
                draw();
                break;
            case MotionEvent.ACTION_UP:
//                isDrawing = false;
                draw();
                break;
        }
        return true;
    }

    private Paint getPaint() {
        Paint paint = new Paint();
        paint.setColor(currentPaintColor);
        paint.setStrokeWidth(currentPaintWidth);
        paint.setAntiAlias(true);
        //Paint.Style.STROKE 描边
        paint.setStyle(Paint.Style.STROKE);
        // 设置线段连接处样式(结合处为圆弧)
        paint.setStrokeJoin(Paint.Join.ROUND);
        //设置线帽 圆形
        paint.setStrokeCap(Paint.Cap.ROUND);
        return paint;
    }



    @Override
    public void surfaceCreated(SurfaceHolder holder) {
//        isDrawing = true;
        //绘制线程
//        new Thread(this).start();
        draw();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void run() {
//        while (isDrawing){
//            draw();
//        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = (int) ((float)width * scaleHeight);
        setMeasuredDimension(width, height);
    }

    public void changeSize(int size) {
        this.currentPaintWidth = size;
    }

    public void changeColor(int color) {
        this.currentPaintColor = color;
    }

    public void clear() {
        paths.clear();
        draw();
    }

    /**
     * 关键点，包含点，线，Path
     */
    class KeyPath{
        /**
         *  画笔
         */
        Paint paint;

        /**
         * 坐标信息
         */
        Path path;

        public KeyPath(Paint paint, Path path) {
            this.paint = paint;
            this.path = path;
        }
    }


}
