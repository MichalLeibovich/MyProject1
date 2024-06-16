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



    public static ArrayList<Path> pathListUndo = new ArrayList<>();
    public static ArrayList<Integer> colorListUndo = new ArrayList<>();

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
    private void upTouch()
    {
        path.lineTo(this.mX, this.mY);
        pathList.add(path);
        colorList.add(currentBrush);
        pathListUndo.add(path);
        colorListUndo.add(currentBrush);
        path = new Path();
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
                makePathListPathListUndo();
                break;

        }
        invalidate();
        return true;
    }

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


    public void undo() {
        if (!pathListUndo.isEmpty() && !colorListUndo.isEmpty()) {
            pathListUndo.remove(pathListUndo.size() - 1);
            colorListUndo.remove(colorListUndo.size() - 1);
        }

        // Clear current path list and color list
        pathList.clear();
        colorList.clear();

        // Rebuild the path and color lists based on remaining undo lists
        for (int i = 0; i < pathListUndo.size(); i++) {
            pathList.add(pathListUndo.get(i));
            colorList.add(colorListUndo.get(i));
        }
        invalidate();

    }

    public void makePathListPathListUndo()
    {
        // Clear current path list and color list
        pathList.clear();
        colorList.clear();

        // Rebuild the path and color lists based on remaining undo lists
        for (int i = 0; i < pathListUndo.size(); i++) {
            pathList.add(pathListUndo.get(i));
            colorList.add(colorListUndo.get(i));
        }
    }

    public void reset()
    {
        pathList.clear();
        colorList.clear();
        pathListUndo.clear();
        colorListUndo.clear();
        invalidate();
    }

}
