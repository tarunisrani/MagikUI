package com.tarunisrani.magickui;

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

    private int radiusOuterCircle = 0;
    private double startingOffsetAngle = 0.0;

    private int xShift = 0;
    private int yShift = 0;

    private int extraPadding = 0;


    private int mWidth = 0;
    private int mHeight = 0;


    public enum Layout_Type{
        FIRST_QUARTER,
        SECOND_QUARTER,
        THIRD_QUARTER,
        FOURTH_QUARTER,
        UPPER_HALF,
        LOWER_HALF,
        FULL_CIRCLE
    }

    private Layout_Type lType = Layout_Type.FULL_CIRCLE;

    public int getyShift() {
        return yShift;
    }

    public void setyShift(int yShift) {
        this.yShift = yShift;
    }

    public int getxShift() {
        return xShift;
    }

    public void setxShift(int xShift) {
        this.xShift = xShift;
    }

    public int getExtraPadding() {
        return extraPadding;
    }

    public void setExtraPadding(int extraPadding) {
        this.extraPadding = extraPadding;
    }

    public Layout_Type getlType() {
        return lType;
    }

    public void setlType(Layout_Type lType) {
        this.lType = lType;
    }

    public int getRadiusOuterCircle() {
        return radiusOuterCircle;
    }

    public void setRadiusOuterCircle(int radiusOuterCircle) {
        this.radiusOuterCircle = radiusOuterCircle;
    }

    public double getStartingOffsetAngle() {
        return startingOffsetAngle;
    }

    public void setStartingOffsetAngle(double startingOffsetAngle) {
        this.startingOffsetAngle = startingOffsetAngle;
    }

    public ParagraphLayout(Context context) {
        super(context);
    }

    public ParagraphLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ParagraphLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private int centerY(int offset){
        return getHeight()/2 + offset;
    }

    private int centerY(){
        return centerY(0);
    }

    private int centerX(){
        return getWidth()/2;
    }

    private float getYCoordinate(double angle){
        double sinTheta = Math.sin(Math.toRadians(angle));
        double yPos = radiusOuterCircle*sinTheta;

        return (float)yPos;
    }

    private float getXCoordinate(double angle){
        double cosTheta = Math.cos(Math.toRadians(angle));
        double xPos = radiusOuterCircle*cosTheta;

        return (float)xPos;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);



        int maxHeight = 0;
        int maxWidth = 0;
        int firstChildHeight = 0;
        int firstChildWidth = 0;
        int childState = 0;
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


        /*for(int i=0;i<getChildCount();i++){
            View child = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            lp.topMargin = i*100;
            child.setVisibility(GONE);
            child.setY(i*100);
            child.setTranslationY(i*100);
            child.setLayoutParams(lp);
            child.requestLayout();
            child.draw(canvas);
        }*/
        super.draw(canvas);

    }
    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
//        View child = getChildAt(i);
//        MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
//        lp.topMargin = 100;
//        child.setVisibility(GONE);
//        child.setY(100);
//        child.setTranslationY(100);
//        child.setLayoutParams(lp);
//        child.requestLayout();
//        child.draw(canvas);
//        circlePaint.setColor(Color.BLACK);
//        circlePaint.setStrokeWidth(2.0f);
//        circlePaint.setStyle(Paint.Style.STROKE);
//        canvas.drawCircle(centerX(), centerY(), radiusOuterCircle, circlePaint);
        return super.drawChild(canvas, child, drawingTime);
//        return false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
