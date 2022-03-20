package com.example.rubikscube;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class CubeFaceView extends View {
    public int dim;
    public int gap;
    private Rect[][] rectCells = new Rect[3][3];
    private Paint borderColor;
    private int center, top, right, bottom, left;
    private Rect topRect;
    private Rect leftRect;
    private Rect rightRect;
    private Rect bottomRect;
    private Paint[] cellColors = new Paint[6];

    public CubeFaceView(Context context, int dim) {
        super(context);
        init(null, dim);
    }

    public CubeFaceView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs, dim);
    }

    public CubeFaceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, dim);
    }

    public CubeFaceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs, dim);
    }
    private void init(@Nullable AttributeSet set, int dim){
        this.dim = dim;
        this.gap = dim/4;

        borderColor = new Paint();
        borderColor.setColor(Color.WHITE);
        borderColor.setStyle(Paint.Style.STROKE);

        cellColors[0] = new Paint();
        cellColors[0].setColor(Color.WHITE);

        cellColors[1] = new Paint();
        cellColors[1].setColor(Color.rgb(255,128,0));

        cellColors[2] = new Paint();
        cellColors[2].setColor(Color.RED);

        cellColors[3] = new Paint();
        cellColors[3].setColor(Color.YELLOW);

        cellColors[4] = new Paint();
        cellColors[4].setColor(Color.BLUE);

        cellColors[5] = new Paint();
        cellColors[5].setColor(Color.GREEN);

        createRects();
        invalidate();
    }
    public void setDim(int dim){

        this.dim = dim;
        createRects();
        invalidate();
    }
    public void setColors(int center, int top, int right, int bottom, int left){
        this.center = center;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.left = left;
        invalidate();
    }

    private void createRects() {
        for(int i=0;i<3;i++){
            int r = gap*(i+3) + dim*i;
            for(int j=0;j<3;j++){
                int l = gap*(j+3) + dim*j;
                rectCells[i][j] = new Rect();
                rectCells[i][j].left = l;
                rectCells[i][j].top = r;
                rectCells[i][j].right = rectCells[i][j].left + dim;
                rectCells[i][j].bottom = rectCells[i][j].top + dim;

            }
        }

        topRect = new Rect();
        topRect.left = 4*gap + dim;
        topRect.top = gap;
        topRect.right = topRect.left + dim;
        topRect.bottom = topRect.top + gap;

        leftRect = new Rect();
        leftRect.left = gap;
        leftRect.top = 4*gap + dim;
        leftRect.right = leftRect.left + gap;
        leftRect.bottom = leftRect.top + dim;

        rightRect = new Rect();
        rightRect.left = 6*gap + 3*dim;
        rightRect.top = 4*gap + dim;
        rightRect.right = rightRect.left + gap;
        rightRect.bottom = rightRect.top + dim;

        bottomRect = new Rect();
        bottomRect.left = 4*gap + dim;
        bottomRect.top = 6*gap + 3*dim;
        bottomRect.right = bottomRect.left + dim;
        bottomRect.bottom = bottomRect.top + gap;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                if(i == 1 && j == 1){
                    canvas.drawRect(rectCells[i][j], cellColors[center-1]);
                }
                else{
                    canvas.drawRect(rectCells[i][j], borderColor);
                }
            }
        }

        canvas.drawRect(topRect, cellColors[top-1]);
        canvas.drawRect(leftRect, cellColors[left-1]);
        canvas.drawRect(rightRect, cellColors[right-1]);
        canvas.drawRect(bottomRect, cellColors[bottom-1]);
    }
}
