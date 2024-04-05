package com.example.myproject1.GameScreen;

import static com.example.myproject1.GameScreen.GameActivity.paintBrush;
import static com.example.myproject1.GameScreen.GameActivity.path;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class Display extends View
{

    public static ArrayList<Path> pathList = new ArrayList<>();
    public static ArrayList<Integer> colorList = new ArrayList<>();
    public ViewGroup.LayoutParams params;
    public static int currentBrush = Color.BLACK;


    private float mX, mY;
    private static final float TOLERANCE = 5;


    public Display(Context context) {
        super(context);
        init(context);
    }

    public Display(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Display(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context)
    {
        paintBrush.setAntiAlias(true);
        paintBrush.setColor(Color.BLACK);
        paintBrush.setStyle(Paint.Style.STROKE);
        paintBrush.setStrokeCap(Paint.Cap.ROUND);
        paintBrush.setStrokeJoin(Paint.Join.ROUND);
        paintBrush.setStrokeWidth(10f);

        params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }


    // when ACTION_DOWN start touch according to the x,y values
    private void startTouch(float x, float y) {
        path.moveTo(x, y);
        this.mX = x;
        this.mY = y;
    }

    // when ACTION_MOVE move touch according to the x,y values
    private void moveTouch(float x, float y) {
        float dx = Math.abs(x - this.mX);
        float dy = Math.abs(y - this.mY);
        if (dx >= TOLERANCE || dy >= TOLERANCE) {
            path.quadTo(this.mX, this.mY, (x + this.mX) / 2, (y + this.mY) / 2);
            this.mX = x;
            this.mY = y;
        }
    }


    // when ACTION_UP stop touch
    private void upTouch() {
        path.lineTo(this.mX, this.mY);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                startTouch(x, y);
                invalidate();
                break;

            case MotionEvent.ACTION_MOVE:
                moveTouch(x, y);
                pathList.add(path);
                colorList.add(currentBrush);
                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                upTouch();
                break;

        }
        invalidate();
        return true;
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        float x = event.getX();
//        float y = event.getY();
//        switch(event.getAction())
//        {
//            case MotionEvent.ACTION_DOWN:
//                path.moveTo(x, y);
//                invalidate();
//                return true;
//            case MotionEvent.ACTION_MOVE:
//                //path.lineTo(x, y);
//                moveTouch(x, y);
//                pathList.add(path);
//                colorList.add(currentBrush);
//                invalidate();
//                return true;
//            default:
//                return false;
//        }
//    }

    @Override
    protected void onDraw(Canvas canvas)

    {
        for (int i = 0; i < pathList.size(); i++)
        {
            paintBrush.setColor(colorList.get(i));
            canvas.drawPath(pathList.get(i), paintBrush);
            invalidate();
        }
    }
}
