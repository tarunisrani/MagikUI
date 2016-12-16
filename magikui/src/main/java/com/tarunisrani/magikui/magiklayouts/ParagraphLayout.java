package com.tarunisrani.magikui.magiklayouts;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by tarunisrani on 6/14/16.
 */
@TargetApi(11)
public class ParagraphLayout extends FrameLayout {

    public ParagraphLayout(Context context) {
        super(context);
    }

    public ParagraphLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ParagraphLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int count = getChildCount();
        int widthSum = 0;
        int heightSum = 0;

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
                final LayoutParams lp = (LayoutParams) child.getLayoutParams();
                widthSum += child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
                heightSum += child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            }
        }

        int desiredWidth = widthSum;
        int desiredHeight = heightSum;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            width = Math.min(desiredWidth, widthSize);
        } else {
            //Be whatever you want
            width = desiredWidth;
        }

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            //Must be this size
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            height = Math.min(desiredHeight, heightSize);
        } else {
            //Be whatever you want
            height = desiredHeight;
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        int count = getChildCount();

        int xPos = 0;
        int yPos = 0;
        int rowHeight = 0;

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                final LayoutParams lp = (LayoutParams) child.getLayoutParams();


                final int width = child.getWidth();
                final int height = child.getHeight();

                rowHeight = Math.max(rowHeight, height+lp.bottomMargin);


                if(xPos+(lp.leftMargin+lp.rightMargin)+width>(right)){
                    xPos = 0;
                    yPos += rowHeight;
                }

                xPos += lp.leftMargin;
                yPos += lp.topMargin;
                child.layout(xPos, yPos, xPos+width, yPos+height);
                xPos += width;
                xPos += lp.rightMargin;

            }
        }
    }
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }
    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        return super.drawChild(canvas, child, drawingTime);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
