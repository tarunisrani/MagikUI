package com.tarunisrani.magikui.magiklayouts;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

//import com.tarunisrani.magikui.R;

/**
 * Created by tarunisrani on 6/14/16.
 */
public class TreeLayout extends FrameLayout {

    private final int CONDENSED = 0;
    private final int ADJUST = 1;


    private Context context;

    private int lType = 1;

    private int radiusOuterCircle = 0;
    private double startingOffsetAngle = 0.0;

    private int xShift = 0;
    private int yShift = 0;

    private int extraPadding = 0;


    private int mWidth = 0;
    private int mHeight = 0;


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

    public TreeLayout(Context context) {
        super(context);
        this.context = context;
    }

    public TreeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public TreeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
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

    private int logBase2(int val){
        int value = val;
        int result = 0;
        while(value>0){
            value = value/2;
            result++;
        }
        return result;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


        int levels = logBase2(getChildCount());
        int interlevel = levels - 1;
        int childOffsetCount = (int)Math.pow(2, interlevel);

        int count = getChildCount();
        int maxWidth = 0;
        int maxHight = 0;

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
                final LayoutParams lp = (LayoutParams) child.getLayoutParams();
                maxWidth = Math.max(maxWidth,child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin);
                maxHight = Math.max(maxHight,child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);
            }
        }


        int totalChildren = (int) Math.pow(2, levels) - 1;

        int numOfChildrenRemaining = totalChildren - count;
        if(numOfChildrenRemaining>0){
            for (int i=0;i<numOfChildrenRemaining;i++){
//                addView(new View(getContext()), getChildAt(count-1).getWidth(), getChildAt(count-1).getHeight());
            }

        }

        int desiredWidth = maxWidth*childOffsetCount;
        int desiredHeight = maxHight*levels;

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

    /*@Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);



    }*/

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        super.onLayout(changed, left, top, right, bottom);

        if(lType == ADJUST){
            adjustLayout();
        }
        else if(lType == CONDENSED){
            condensedLAyout();
//            adjustLayout(changed, left, top, right, bottom);
        }
    }

    private void condensedLAyout(){
        int count = getChildCount();

        int xPos = 0;
        int yPos = 0;
        int rowHeight = 0;

        int levels = logBase2(getChildCount());

        for (int i = 0; i < levels; i++) {
            int index = (int)Math.pow(2, i);
            int range = index;

            int childCount = 0;
            int divider = range / 2;
            int rowWidth = 0;

            for(int j=index-1;j<index-1+range;j++) {
                if(j<count) {
                    final View child = getChildAt(j);
                    if (child.getVisibility() != GONE) {
                        final LayoutParams lp = (LayoutParams) child.getLayoutParams();

                        final int width = child.getMeasuredWidth();
                        final int height = child.getMeasuredHeight();
                        rowWidth += (width + lp.leftMargin + lp.rightMargin);

                    }
                }
            }

            xPos = centerX() - rowWidth/2;
            for(int j=index-1;j<index-1+range;j++) {
                if(j<count) {
                    final View child = getChildAt(j);
                    if (child.getVisibility() != GONE) {
                        final LayoutParams lp = (LayoutParams) child.getLayoutParams();

                        final int width = child.getMeasuredWidth();
                        final int height = child.getMeasuredHeight();

                        rowHeight = Math.max(rowHeight, height + lp.bottomMargin);
                        ++childCount;

                        child.layout(xPos, yPos, xPos + width, yPos + height);
                        xPos += width;
                    }
                }
            }
            yPos += rowHeight;
        }
    }

    private void adjustLayout(){
        int count = getChildCount();

        int xPos = 0;
        int yPos = 0;
        int rowHeight = 0;
        int leftchildCount = 0;
        int rightchildCount = 0;

        int viewHeight = getHeight();
        int viewWidth = getWidth();

        int child_one_avg_width = 0;
        int child_two_avg_width = 0;

        int levels = logBase2(getChildCount());

        for (int i = levels-1; i >=0; i--) {
            int index = (int) Math.pow(2, i);
            int range = index;

            int rowWidth = 0;

            for (int j = index - 1; j < index - 1 + range; j++) {
                if (j < count) {
                    final View child = getChildAt(j);
                    if (child.getVisibility() != GONE) {
                        final LayoutParams lp = (LayoutParams) child.getLayoutParams();

                        final int width = child.getMeasuredWidth();
                        final int height = child.getMeasuredHeight();
                        rowWidth = Math.max(rowWidth, width + lp.leftMargin + lp.rightMargin);
                        rowHeight = Math.max(rowHeight, height + lp.bottomMargin);
                    }
                }
            }

            rowWidth = rowWidth*range;
            yPos = viewHeight - rowHeight;
            viewHeight = viewHeight - rowHeight;
            xPos = 0;
            leftchildCount = 0;
            for (int j = index - 1; j < index - 1 + range; j++) {
                if (j < count) {
                    final View child = getChildAt(j);


                    if (child.getVisibility() != GONE) {

                        final LayoutParams lp = (LayoutParams) child.getLayoutParams();

                        final int width = child.getMeasuredWidth();
                        final int height = child.getMeasuredHeight();


                        //Check for child nodes.
                        if(i<levels-1){
                            View child_one = getChildAt(j*2+1);
                            View child_two = getChildAt(j*2+2);
                            if(child_one!=null){
                                leftchildCount++;
                                child_one_avg_width+=child_one.getMeasuredWidth();
                                xPos = child_one.getLeft()/2;
                                if(child_two!=null){
                                    rightchildCount++;
                                    child_two_avg_width+=child_two.getMeasuredWidth();
                                    xPos += child_two.getRight()/2;
                                }
                                else{
                                    rightchildCount++;
                                    child_two_avg_width+=child_one.getMeasuredWidth();
                                    xPos += (child_one.getRight()+child_one.getMeasuredWidth())/2;
                                }

                            }
                            else{

                                if(getChildAt(j-1)!=null){
                                    xPos =  getChildAt(j-1).getRight() + (child_one_avg_width/(leftchildCount*2)) + (child_two_avg_width/(rightchildCount*2));
                                    xPos += width/2;
                                }
                            }

                            viewWidth = getWidth() - child.getRight();

                            xPos -= width/2;
                            child.layout(xPos, yPos, xPos + width, yPos + height);
                        }
                        else{
                            child.layout(xPos, yPos, xPos + width, yPos + height);
                            xPos += width;

                        }
                    }
                }

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
