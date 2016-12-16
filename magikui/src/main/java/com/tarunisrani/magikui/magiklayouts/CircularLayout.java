package com.tarunisrani.magikui.magiklayouts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

//import com.tarunisrani.magikui.R;

/**
 * Created by tarunisrani on 6/14/16.
 */

public class CircularLayout extends FrameLayout {

    private int radiusCircle = 0;
    private int radiusOuterCircle = 0;
    private int radiusInnerCircle = 0;
    private int widthCircularPath = 0;
    private double startingOffsetAngle = 0.0;
    private double endAngle = 360.0;
    private double shiftangle = 0.0;

    private int circularPathColor = Color.TRANSPARENT;

    private int xShift = 0;
    private int yShift = 0;

    private int extraPadding = 0;


    private final int FULL_CIRCLE = 101;
    private final int UPPER_HALF = 102;
    private final int LOWER_HALF = 103;
    private final int FIRST_QUARTER = 104;
    private final int SECOND_QUARTER = 105;
    private final int THIRD_QUARTER = 106;
    private final int FOURTH_QUARTER = 107;


    private int lType = FULL_CIRCLE;

    public CircularLayout(Context context) {
        super(context);
    }

    public CircularLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircularLayout(Context context, AttributeSet attrs, int defStyleAttr) {
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

    private float getYCoordinate(double angle, int radius){
        double sinTheta = Math.sin(Math.toRadians(angle));
        double yPos = radius*sinTheta;

        return (float)yPos;
    }

    private float getXCoordinate(double angle, int radius){
        double cosTheta = Math.cos(Math.toRadians(angle));
        double xPos = radius*cosTheta;

        return (float)xPos;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
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


        //Calculating dimension of first child element of layout.
        // Since we have added an extra view previously, first element index is 1.

        final View firstChild = getChildAt(0);
        if (firstChild.getVisibility() != GONE) {
            measureChildWithMargins(firstChild, widthMeasureSpec, 0, heightMeasureSpec, 0);
            final LayoutParams lp = (LayoutParams) firstChild.getLayoutParams();
            firstChildWidth = firstChild.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            firstChildHeight = firstChild.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            childState = combineMeasuredStates(childState, firstChild.getMeasuredState());

        }

        //Calculating maximum height and maximum width of all the elements in layout
        // apart from first element as it is placed in center.

        for (int i = 1; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
                final LayoutParams lp = (LayoutParams) child.getLayoutParams();
                maxWidth = Math.max(maxWidth,
                        child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin);
                maxHeight = Math.max(maxHeight,
                        child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);
                childState = combineMeasuredStates(childState, child.getMeasuredState());
            }
        }


        int max_diagonal_of_child = (int) Math.sqrt(maxHeight * maxHeight + maxWidth * maxWidth);

        maxHeight = maxHeight * 2 + firstChildHeight;
        maxWidth = maxWidth * 2 + firstChildWidth;

        if (lType == FIRST_QUARTER || lType == SECOND_QUARTER || lType == THIRD_QUARTER || lType == FOURTH_QUARTER) {
            maxHeight += (int) (firstChildHeight * 1.25f);      //Just for a little padding
            maxWidth += (int) (firstChildWidth * 1.25f);        //Just for a little padding
        } else {
            maxHeight += (int) (firstChildHeight * 0.75f);      //Just for a little padding
            maxWidth += (int) (firstChildWidth * 0.75f);        //Just for a little padding
        }

        if (lType == FULL_CIRCLE) {
            maxHeight = Math.max(maxHeight, maxWidth);
            maxWidth = maxHeight;
        } else if (lType == SECOND_QUARTER || lType == FOURTH_QUARTER) {
            int lastchildwidth = getChildAt(getChildCount() - 1).getMeasuredWidth();
            shiftangle = (Math.atan2(lastchildwidth / 2, (double) radiusCircle) * 180) / Math.PI;
            maxWidth = maxWidth / 2;
        } else if (lType == FIRST_QUARTER || lType == THIRD_QUARTER) {
            int lastchildwidth = getChildAt(1).getMeasuredWidth();
            shiftangle = (Math.atan2(lastchildwidth / 2, (double) radiusCircle) * 180) / Math.PI;
            maxWidth = maxWidth / 2;
        }

        int desiredWidth = maxWidth;
        int desiredHeight = maxHeight;

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


        if (lType == FULL_CIRCLE) {
            maxHeight = Math.max(maxHeight, maxWidth);
            maxWidth = maxHeight;
        } else if (lType == UPPER_HALF || lType == FIRST_QUARTER || lType == FOURTH_QUARTER) {
            yShift = height / 2;

        } else if (lType == LOWER_HALF || lType == SECOND_QUARTER || lType == THIRD_QUARTER) {
            yShift = -height / 2;
        }

        if (lType == FIRST_QUARTER || lType == SECOND_QUARTER) {
            xShift = -width / 2;
        } else if (lType == THIRD_QUARTER || lType == FOURTH_QUARTER) {
            xShift = width / 2;
        }

        if(lType == FULL_CIRCLE || lType == UPPER_HALF || lType == LOWER_HALF){
            radiusOuterCircle = Math.min(width, height) / 2;
        }
        else {
            radiusOuterCircle = Math.min(width, height);
        }


        int maxVal = Math.max(getPaddingLeft(), getPaddingRight());
        maxVal = Math.max(getPaddingBottom(), maxVal);
        maxVal = Math.max(getPaddingTop(), maxVal);
        radiusOuterCircle -= maxVal;
        radiusOuterCircle -= extraPadding;


        widthCircularPath = (int) (max_diagonal_of_child * 1.0);
        radiusInnerCircle = radiusOuterCircle - widthCircularPath;
        radiusCircle = (radiusOuterCircle + radiusInnerCircle) / 2;

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        int maxWidth = 0;
        int maxHeight = 0;

        for (int i = 1; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                final LayoutParams lp = (LayoutParams) child.getLayoutParams();

                int width = child.getMeasuredWidth();
                int height = child.getMeasuredHeight();

                maxHeight = Math.max(maxHeight, height);
                maxWidth = Math.max(maxWidth, width);

            }
        }

        performLayouting(radiusCircle);
    }

    public void performLayouting(int radius){
        int count = getChildCount()-1;
        double stepAngle = 0.0;

        double ang = 0.0;

        if(lType == FULL_CIRCLE){
            endAngle = 360.0;
        }
        else if(lType == LOWER_HALF){
            endAngle = 180.0;
        }
        else if(lType == UPPER_HALF){
            endAngle = 180.0;
            ang = -180.0;
        }
        else if(lType == FIRST_QUARTER){
            endAngle = 90.0;
            ang = -90.0;
            ang += shiftangle;
            endAngle -= shiftangle;
        }
        else if(lType == SECOND_QUARTER){
            endAngle = 90.0;
            endAngle -= shiftangle;
        }
        else if(lType == THIRD_QUARTER){
            endAngle = 90.0;
            ang = 90.0;
            ang += shiftangle;
            endAngle -= shiftangle;
        }
        else if(lType == FOURTH_QUARTER){
            endAngle = 90.0;
            ang = 180.0;
            endAngle -= shiftangle;
        }

        ang += startingOffsetAngle;

        stepAngle = endAngle/count;
        ang += stepAngle/2;

        int center_x = centerX();
        center_x += xShift;
        center_x -= getChildAt(0).getMeasuredWidth()/2;



        int center_y = centerY();
        if(lType!=FULL_CIRCLE) {
            center_y += yShift;
        }
        else{
            center_y -= getChildAt(0).getMeasuredHeight()/2;
        }
        if(lType == UPPER_HALF || lType == FIRST_QUARTER || lType == FOURTH_QUARTER) {
            center_y -= getChildAt(0).getMeasuredHeight();
        }else if(lType == LOWER_HALF || lType == SECOND_QUARTER || lType == THIRD_QUARTER) {
//            center_y += getChildAt(1).getMeasuredHeight()/2;
        }

        if(lType == FIRST_QUARTER || lType == SECOND_QUARTER) {
            center_x += getChildAt(0).getMeasuredHeight();
        }else if(lType == THIRD_QUARTER || lType == FOURTH_QUARTER) {
            center_x -= getChildAt(0).getMeasuredHeight();
        }

        getChildAt(0).layout(center_x, center_y, center_x + getChildAt(0).getMeasuredWidth(), center_y + getChildAt(0).getMeasuredHeight());


        for (int i = 1; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                final LayoutParams lp = (LayoutParams) child.getLayoutParams();

                final int width = child.getMeasuredWidth();
                final int height = child.getMeasuredHeight();


                int x = (int)getXCoordinate(ang, radius);
                x += centerX();
                x += xShift;
                x -= width/2;


                int y = (int)getYCoordinate(ang, radius);
                y += centerY();
                y += yShift;
                y -= height/2;

                ang = ang + stepAngle;
                child.layout(x, y, x + width, y + height);
            }
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
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
