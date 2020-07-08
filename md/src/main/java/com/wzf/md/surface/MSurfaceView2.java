package com.wzf.md.surface;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

public class MSurfaceView2 extends SurfaceView {

    private final int DEFAULT_PAINT_WIDTH = 10;
    private final int DEFAULT_PAINT_COLOR = 0XFFFFFFFF;
    private int currentPaintColor = DEFAULT_PAINT_COLOR;
    private int currentPaintWidth = DEFAULT_PAINT_WIDTH;

    float scaleHeight = 1.0f;

    List<KeyPath> paths = new ArrayList<>();


    SurfaceHolder holder;
    SurfaceHolder.Callback callback;
    public MSurfaceView2(Context context) {
        this(context, null);
    }

    public MSurfaceView2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public void loadHolder() {
        holder = this.getHolder();
        holder.addCallback(callback = new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true){
                            draw(MSurfaceView2.this.holder);
                            try {
                                Thread.sleep(100);
                            } catch (Exception e) {
                            }
                        }
                    }
                }){}.start();

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
    }


    private void draw(SurfaceHolder holder) {
        Canvas canvas = holder.lockCanvas(new Rect(0,0, getWidth(),getHeight()));
//        Canvas canvas = holder.lockCanvas();
        canvas.drawColor(Color.BLACK);
        Paint mPaint = new Paint();
        mPaint.setColor(Color.RED);
        canvas.drawCircle(150, 150, 100, mPaint);
//        for (KeyPath path : paths) {
//            canvas.drawPath(path.path, path.paint);
//        }
        holder.unlockCanvasAndPost(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        Path path = new Path();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                path.moveTo(x, y);
                paths.add(new KeyPath(getPaint(), path));
                break;
            case MotionEvent.ACTION_MOVE:
                path.moveTo(x, y);
                paths.add(new KeyPath(getPaint(), path));
                break;
            case MotionEvent.ACTION_UP:
                path.moveTo(x, y);
                paths.add(new KeyPath(getPaint(), path));
                break;
        }
//        callback.surfaceCreated(holder);
        return super.onTouchEvent(event);
    }

    private Paint getPaint() {
        Paint paint = new Paint();
        paint.setColor(currentPaintColor);
        paint.setStrokeWidth(currentPaintWidth);
        paint.setAntiAlias(true);
        return paint;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = (int) ((float)width * scaleHeight);
        setMeasuredDimension(width, height);
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
