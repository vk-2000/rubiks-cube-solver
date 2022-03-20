package com.example.rubikscube;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.HashMap;

public class Cube2DView extends View {

    private Paint[] cellPaints = new Paint[6];
    private int dim;
    private RectF[][][] facesRects = new RectF[6][3][3];
    public int selectedColor = -1;
    private RCube rCube;



    public Cube2DView(Context context, int dim) {
        super(context);
        Log.d("TAG", "Cube2DView: ");
        init(null, dim);



    }

    public void setrCube(RCube rCube){
        this.rCube = rCube;
    }


    public void makeMove(String move){
        rCube.makeMove(move);
        invalidate();
        Log.d("TAG", "makeMove: " + move);
        rCube.printFacesColors();
    }
    public void makeMoveReverse(String move){
        rCube.makeMoveReverse(move);
        invalidate();
        Log.d("TAG", "makeMove: " + move);
        rCube.printFacesColors();
    }



    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        int x = (int)event.getX();
        int y = (int)event.getY();
        boolean moving;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                x -= dim;
                y -= dim;
                if(x<0 || y<0) return true;

                int fx = x/(dim*3);
                int fy = y/(dim*3);

                int i = (x - dim*fx*3)/dim;
                int j = (y - dim*fy*3)/dim;
                Log.d("TAG", "onTouchEvent: " + fx + " " + fy + " ");

                if(fx == 1 && fy == 1) changeCellColor(0, j, i);
                else if(fx == 0 && fy == 1) changeCellColor(1, j, i);
                else if(fx == 2 && fy == 1) changeCellColor(2, j, i);
                else if(fx == 3 && fy == 1) changeCellColor(3, j, i);
                else if(fx == 1 && fy == 0) changeCellColor(4, j, i);
                else if(fx == 1 && fy == 2) changeCellColor(5, j, i);

                break;
        }

        return true;
    }

    private void changeCellColor(int face, int x, int y) {
        if(selectedColor == -1){
            return;
        }
        rCube.facesColors[face][x][y] = selectedColor;
        invalidate();
    }


    private void init(@Nullable AttributeSet set, int dim) {

        this.dim = dim;

        cellPaints[0] = new Paint();
        cellPaints[0].setColor(Color.WHITE);

        cellPaints[1] = new Paint();
        cellPaints[1].setColor(Color.rgb(255,128,0));

        cellPaints[2] = new Paint();
        cellPaints[2].setColor(Color.RED);

        cellPaints[3] = new Paint();
        cellPaints[3].setColor(Color.YELLOW);

        cellPaints[4] = new Paint();
        cellPaints[4].setColor(Color.BLUE);

        cellPaints[5] = new Paint();
        cellPaints[5].setColor(Color.GREEN);

        create2DCube();
        invalidate();
    }

    private void create2DCube() {
        createFace(0, 4*dim, 4*dim);
        createFace(1, dim, 4*dim);
        createFace(2, 7*dim, 4*dim);
        createFace(3, 10*dim, 4*dim);
        createFace(4, 4*dim, dim);
        createFace(5, 4*dim, 7*dim);

    }
    private void createFace(int face, int left, int top){
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                facesRects[face][i][j] = new RectF(left + (j*dim), top + (i*dim), left + (j*dim) + dim, top + (i*dim) + dim);

            }
        }
    }

    public Cube2DView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Cube2DView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public Cube2DView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    public void onDraw(Canvas canvas) {

        for(int face=0; face<6;face++){
            for(int i=0;i<3;i++){
                for(int j=0;j<3;j++){
                    canvas.drawRoundRect(facesRects[face][i][j],10,10, cellPaints[rCube.facesColors[face][i][j]]);
                }
            }
        }

    }
}
