package com.tarunisrani.magickui;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by tarunisrani on 6/14/16.
 */
@TargetApi(11)
public class CircularLayout extends FrameLayout {

    private int radiusCircle = 0;
    private int radiusOuterCircle = 0;
    private int radiusInnerCircle = 0;
    private int widthCircularPath = 0;
    private double startingOffsetAngle = 0.0;
    private double endAngle = 360.0;
    double shiftangle = 0.0;

    private int xShift = 0;
    private int yShift = 0;

    private int extraPadding = 0;

    private BackgroundPath drawable;


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

    public int getRadiusCircle() {
        return radiusCircle;
    }

    public void setRadiusCircle(int radiusCircle) {
        this.radiusCircle = radiusCircle;
    }

    public double getStartingOffsetAngle() {
        return startingOffsetAngle;
    }

    public void setStartingOffsetAngle(double startingOffsetAngle) {
        this.startingOffsetAngle = startingOffsetAngle;
    }

    public CircularLayout(Context context) {
        super(context);
        initialize();
    }

    public CircularLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public CircularLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    private int centerY(int offset){
        return getHeight()/2 + offset;
    }

    private int centerY(){
        return centerY(0);
    }

    private void initialize(){
        drawable = new BackgroundPath(getContext());
        addView(drawable, 0);
    }

    private int centerX(){
        return getWidth()/2;
    }

    private float getYCoordinate(double angle){
//        double sinTheta = Math.sin(Math.toRadians(angle));
//        double yPos = radiusCircle*sinTheta;
//        return (float)yPos;
        return getYCoordinate(angle, radiusCircle);
    }
    private float getYCoordinate(double angle, int radius){
        double sinTheta = Math.sin(Math.toRadians(angle));
        double yPos = radius*sinTheta;

        return (float)yPos;
    }

    private float getXCoordinate(double angle){
//        double cosTheta = Math.cos(Math.toRadians(angle));
//        double xPos = radiusCircle*cosTheta;
//        return (float)xPos;
        return getXCoordinate(angle, radiusCircle);
    }

    private float getXCoordinate(double angle, int radius){
        double cosTheta = Math.cos(Math.toRadians(angle));
        double xPos = radius*cosTheta;

        return (float)xPos;
    }

    public void setBackgroundColor(int color){
        if(drawable!=null){
            drawable.setBgColor(color);
        }
    }


    public void playExpandAnimation(){
        playExpandAnimation(500);
    }

    public void playExpandAnimation(int time){
        int finalvalue = radiusCircle;
        ValueAnimator anim = ValueAnimator.ofInt(0, finalvalue);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int val = ((Integer)animation.getAnimatedValue()).intValue();
//                ((Button)getChildAt(0)).setText(String.valueOf(val));
                performLayouting(val);
                drawable.redraw(val);
                invalidate();
            }
        });
        anim.setDuration(time);
        anim.start();
//        return anim;
    }

    public ValueAnimator.AnimatorUpdateListener getUpdateListener(){
        return new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int val = ((Integer)animation.getAnimatedValue()).intValue();
//                ((Button)getChildAt(0)).setText(String.valueOf(val));


                performLayouting(val);
                drawable.redraw(val);
            }
        };
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
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);



            int maxHeight = 0;
            int maxWidth = 0;
            int firstChildHeight = 0;
            int firstChildWidth = 0;
            int childState = 0;
            int count = getChildCount();

            for (int i = 2; i < count; i++) {
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

            final View firstChild = getChildAt(1);
            if (firstChild.getVisibility() != GONE) {
                measureChildWithMargins(firstChild, widthMeasureSpec, 0, heightMeasureSpec, 0);
                final LayoutParams lp = (LayoutParams) firstChild.getLayoutParams();
                firstChildWidth = Math.max(firstChildWidth,
                        firstChild.getMeasuredWidth() + lp.leftMargin + lp.rightMargin);
                firstChildHeight = Math.max(firstChildHeight,
                        firstChild.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);
                childState = combineMeasuredStates(childState, firstChild.getMeasuredState());

            }

            maxHeight = (int) (maxHeight * 1.0f);
            maxWidth = (int) (maxWidth * 1.0f);

            int diagonal = 0;
            diagonal = (int) Math.sqrt(maxHeight * maxHeight + maxWidth * maxWidth);

            maxHeight = (int) (maxHeight * 2.0f);
            maxWidth = (int) (maxWidth * 2.0f);

            maxHeight += firstChildHeight;
            maxWidth += firstChildWidth;


            // Account for padding too
//        maxWidth += getPaddingLeftWithForeground() + getPaddingRightWithForeground();
//        maxHeight += getPaddingTopWithForeground() + getPaddingBottomWithForeground();

            // Check against our minimum height and width
//        maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
//        maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());

//        maxHeight = Math.max(maxHeight, maxWidth);
//        maxWidth = maxHeight;


            // Check against our foreground's minimum height and width
        /*final Drawable drawable = getForeground();
        if (drawable != null) {
            maxHeight = Math.max(maxHeight, drawable.getMinimumHeight());
            maxWidth = Math.max(maxWidth, drawable.getMinimumWidth());
        }*/

//        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
//                resolveSizeAndState(maxHeight, heightMeasureSpec,
//                        childState << MEASURED_HEIGHT_STATE_SHIFT));


            if (lType == Layout_Type.FIRST_QUARTER || lType == Layout_Type.SECOND_QUARTER || lType == Layout_Type.THIRD_QUARTER || lType == Layout_Type.FOURTH_QUARTER) {
                maxHeight += (int) (firstChildHeight * 1.25f);      //Just for a little padding
                maxWidth += (int) (firstChildWidth * 1.25f);        //Just for a little padding
            } else {
                maxHeight += (int) (firstChildHeight * 0.75f);      //Just for a little padding
                maxWidth += (int) (firstChildWidth * 0.75f);        //Just for a little padding
            }

//            radiusOuterCircle = Math.max(maxWidth, maxHeight) / 2;

            /*if (lType == Layout_Type.FULL_CIRCLE) {
                maxHeight = Math.max(maxHeight, maxWidth);
                maxWidth = maxHeight;
            } else if (lType == Layout_Type.UPPER_HALF || lType == Layout_Type.FIRST_QUARTER || lType == Layout_Type.FOURTH_QUARTER) {
                yShift = maxHeight / 2;

            } else if (lType == Layout_Type.LOWER_HALF || lType == Layout_Type.SECOND_QUARTER || lType == Layout_Type.THIRD_QUARTER) {
                yShift = -maxHeight / 2;

            }

            if (lType == Layout_Type.SECOND_QUARTER || lType == Layout_Type.FOURTH_QUARTER) {
                int lastchildwidth = getChildAt(getChildCount() - 1).getMeasuredWidth();
                shiftangle = (Math.atan2(lastchildwidth / 2, (double) radiusCircle) * 180) / Math.PI;
                maxWidth = maxWidth / 2;
            } else if (lType == Layout_Type.FIRST_QUARTER || lType == Layout_Type.THIRD_QUARTER) {
                int lastchildwidth = getChildAt(2).getMeasuredWidth();
                shiftangle = (Math.atan2(lastchildwidth / 2, (double) radiusCircle) * 180) / Math.PI;
                maxWidth = maxWidth / 2;
            }

            if (lType == Layout_Type.FIRST_QUARTER || lType == Layout_Type.SECOND_QUARTER) {
                xShift = -maxWidth / 2;
            } else if (lType == Layout_Type.THIRD_QUARTER || lType == Layout_Type.FOURTH_QUARTER) {
                xShift = maxWidth / 2;
            }*/

            /*radiusOuterCircle = Math.max(maxWidth, maxHeight) / 2;
            int maxVal = Math.max(getPaddingLeft(), getPaddingRight());
            maxVal = Math.max(getPaddingBottom(), maxVal);
            maxVal = Math.max(getPaddingTop(), maxVal);
            radiusOuterCircle -= maxVal;
            radiusOuterCircle -= extraPadding;


            widthCircularPath = (int) (diagonal * 1.0);
            radiusInnerCircle = radiusOuterCircle - widthCircularPath;
            radiusCircle = (radiusOuterCircle + radiusInnerCircle) / 2;

            drawable.initialize();
            drawable.redraw(radiusCircle);*/

//        int lastchildwidth = getChildAt(getChildCount()-1).getMeasuredWidth();
//        shiftangle = (Math.atan2(lastchildwidth/2, (double)radiusCircle)* 180)/ Math.PI;


//            setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
//                    resolveSizeAndState(maxHeight, heightMeasureSpec,
//                            childState << MEASURED_HEIGHT_STATE_SHIFT));

        if (lType == Layout_Type.FULL_CIRCLE) {
            maxHeight = Math.max(maxHeight, maxWidth);
            maxWidth = maxHeight;
        } else if (lType == Layout_Type.SECOND_QUARTER || lType == Layout_Type.FOURTH_QUARTER) {
            int lastchildwidth = getChildAt(getChildCount() - 1).getMeasuredWidth();
            shiftangle = (Math.atan2(lastchildwidth / 2, (double) radiusCircle) * 180) / Math.PI;
            maxWidth = maxWidth / 2;
        } else if (lType == Layout_Type.FIRST_QUARTER || lType == Layout_Type.THIRD_QUARTER) {
            int lastchildwidth = getChildAt(2).getMeasuredWidth();
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


        if (lType == Layout_Type.FULL_CIRCLE) {
            maxHeight = Math.max(maxHeight, maxWidth);
            maxWidth = maxHeight;
        } else if (lType == Layout_Type.UPPER_HALF || lType == Layout_Type.FIRST_QUARTER || lType == Layout_Type.FOURTH_QUARTER) {
            yShift = height / 2;

        } else if (lType == Layout_Type.LOWER_HALF || lType == Layout_Type.SECOND_QUARTER || lType == Layout_Type.THIRD_QUARTER) {
            yShift = -height / 2;

        }

//        if (lType == Layout_Type.SECOND_QUARTER || lType == Layout_Type.FOURTH_QUARTER) {
//            int lastchildwidth = getChildAt(getChildCount() - 1).getMeasuredWidth();
//            shiftangle = (Math.atan2(lastchildwidth / 2, (double) radiusCircle) * 180) / Math.PI;
//        } else if (lType == Layout_Type.FIRST_QUARTER || lType == Layout_Type.THIRD_QUARTER) {
//            int lastchildwidth = getChildAt(2).getMeasuredWidth();
//            shiftangle = (Math.atan2(lastchildwidth / 2, (double) radiusCircle) * 180) / Math.PI;
//        }

        if (lType == Layout_Type.FIRST_QUARTER || lType == Layout_Type.SECOND_QUARTER) {
            xShift = -width / 2;
        } else if (lType == Layout_Type.THIRD_QUARTER || lType == Layout_Type.FOURTH_QUARTER) {
            xShift = width / 2;
        }



        if(lType == Layout_Type.FULL_CIRCLE || lType == Layout_Type.UPPER_HALF || lType == Layout_Type.LOWER_HALF){
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


        widthCircularPath = (int) (diagonal * 1.0);
        radiusInnerCircle = radiusOuterCircle - widthCircularPath;
        radiusCircle = (radiusOuterCircle + radiusInnerCircle) / 2;

        drawable.initialize();
        drawable.redraw(radiusCircle);


        setMeasuredDimension(width, height);


    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        int maxWidth = 0;
        int maxHeight = 0;

        for (int i = 2; i < getChildCount(); i++) {
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
        int count = getChildCount()-2;
        double stepAngle = 0.0;

        double ang = 0.0;



        if(lType == Layout_Type.FULL_CIRCLE){
            endAngle = 360.0;
        }
        else if(lType == Layout_Type.LOWER_HALF){
            endAngle = 180.0;
        }
        else if(lType == Layout_Type.UPPER_HALF){
            endAngle = 180.0;
            ang = -180.0;
        }
        else if(lType == Layout_Type.FIRST_QUARTER){
            endAngle = 90.0;
            ang = -90.0;
            ang += shiftangle;
            endAngle -= shiftangle;
        }
        else if(lType == Layout_Type.SECOND_QUARTER){
            endAngle = 90.0;
            endAngle -= shiftangle;
        }
        else if(lType == Layout_Type.THIRD_QUARTER){
            endAngle = 90.0;
            ang = 90.0;
            ang += shiftangle;
            endAngle -= shiftangle;
        }
        else if(lType == Layout_Type.FOURTH_QUARTER){
            endAngle = 90.0;
            ang = 180.0;
            endAngle -= shiftangle;
        }


        /*if(lType == Layout_Type.SECOND_QUARTER || lType == Layout_Type.FOURTH_QUARTER ){
            int lastchildwidth = getChildAt(getChildCount()-1).getMeasuredWidth();
            shiftangle = (Math.atan2(lastchildwidth/2, (double)radiusCircle)* 180)/ Math.PI;
        }else if(lType == Layout_Type.FIRST_QUARTER || lType == Layout_Type.THIRD_QUARTER ){
            int lastchildwidth = getChildAt(2).getMeasuredWidth();
            shiftangle = (Math.atan2(lastchildwidth/2, (double)radiusCircle)* 180)/ Math.PI;
        }*/

        ang += startingOffsetAngle;

        stepAngle = endAngle/count;
        ang += stepAngle/2;
//        mPaintEmptyCircle.setColor(Color.WHITE);

//        addView();


//        mPaintEmptyCircle.setColor(Color.RED);
        /*for(ang = 0.0;ang<=endAngle;ang = ang+1.2){
            float x = getXCoordinate(ang);
            float y = getYCoordinate(ang);

//            drawCircularBalls(canvas, x, y, radiusInnerCircularBalls, mPaintEmptyCircle);
        }*/


        int bg_x = (int)getChildAt(0).getX();
        bg_x += xShift;
        bg_x -= getChildAt(1).getMeasuredWidth()/2;

        int bg_y = (int)getChildAt(0).getY();
        bg_y += yShift;
//        bg_y -= getChildAt(1).getMeasuredHeight();

//        getChildAt(0).layout(bg_x, bg_y, bg_x + getChildAt(0).getMeasuredWidth(), bg_y + getChildAt(0).getMeasuredHeight());


        int center_x = centerX();
        center_x += xShift;
        center_x -= getChildAt(1).getMeasuredWidth()/2;



        int center_y = centerY();
//        center_y -= getChildAt(1).getMeasuredHeight()/2;
//        if(lType == Layout_Type.UPPER_HALF || lType == Layout_Type.LOWER_HALF) {
        if(lType!=Layout_Type.FULL_CIRCLE) {
            center_y += yShift;
        }
        else{
            center_y -= getChildAt(1).getMeasuredHeight()/2;
        }
//        }
        if(lType == Layout_Type.UPPER_HALF || lType == Layout_Type.FIRST_QUARTER || lType == Layout_Type.FOURTH_QUARTER) {
            center_y -= getChildAt(1).getMeasuredHeight();
        }else if(lType == Layout_Type.LOWER_HALF || lType == Layout_Type.SECOND_QUARTER || lType == Layout_Type.THIRD_QUARTER) {
//            center_y += getChildAt(1).getMeasuredHeight()/2;
        }

        if(lType == Layout_Type.FIRST_QUARTER || lType == Layout_Type.SECOND_QUARTER) {
            center_x += getChildAt(1).getMeasuredHeight();
        }else if(lType == Layout_Type.THIRD_QUARTER || lType == Layout_Type.FOURTH_QUARTER) {
            center_x -= getChildAt(1).getMeasuredHeight();
        }

        getChildAt(1).layout(center_x, center_y, center_x + getChildAt(1).getMeasuredWidth(), center_y + getChildAt(1).getMeasuredHeight());


        for (int i = 2; i < getChildCount(); i++) {
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

//                child.layout(childLeft, childTop, childLeft + width, childTop + height);
                child.layout(x, y, x + width, y + height);
            }
        }


//        invalidate();


    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
//        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
//        Bitmap bmp = Bitmap.createBitmap(getWidth(), getHeight(), conf);
//        Canvas cnvs = new Canvas(bmp);
//        drawable = new BackgroundPath(getContext());
//        drawable.draw(cnvs);
//        canvas.drawBitmap(bmp, 0, 0, null);

        super.dispatchDraw(canvas);
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
//        canvas.drawCircle(centerX(), centerY(), radiusCircle, circlePaint);
        return super.drawChild(canvas, child, drawingTime);
//        return false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    class BackgroundPath extends View {

        private Paint circlePaint = new Paint();
        private Paint textPaint = new Paint();
        private Path mPath = new Path();
        private int radius = radiusCircle;
        int center_x;
        int center_y;

        public BackgroundPath(Context context) {
            super(context);
            initialize();
        }

        public BackgroundPath(Context context, AttributeSet attrs) {
            super(context, attrs);
            initialize();
        }

        public BackgroundPath(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            initialize();
        }


        public void initialize(){
//            circlePaint.setColor(Color.WHITE);
            circlePaint.setColor(Color.BLACK);
            circlePaint.setStrokeWidth(widthCircularPath);
            circlePaint.setStyle(Paint.Style.STROKE);

            if(lType == Layout_Type.UPPER_HALF){
                mPath.addRect(0, 0, getWidth(), centerY(), Path.Direction.CW);
            }
            else if(lType == Layout_Type.LOWER_HALF){
                mPath.addRect(0, centerY(), getWidth(), getHeight(), Path.Direction.CCW);
            }
            else if(lType == Layout_Type.FIRST_QUARTER){
//                mPath.addRect(0, 0, getWidth(), centerY(), Path.Direction.CCW);
                mPath.addRect(centerX(), 0, getWidth(), centerY(), Path.Direction.CCW);
            }
            else if(lType == Layout_Type.THIRD_QUARTER){
//                mPath.addRect(0, 0, getWidth(), centerY(), Path.Direction.CCW);
                mPath.addRect(0, centerX(), centerY(), getHeight(), Path.Direction.CCW);
            }
            else if(lType == Layout_Type.SECOND_QUARTER){
//                mPath.addRect(0, 0, getWidth(), centerY(), Path.Direction.CCW);
                mPath.addRect(centerX(), centerY(), getWidth(), getHeight(), Path.Direction.CCW);
            }
            else if(lType == Layout_Type.FOURTH_QUARTER){
//                mPath.addRect(0, 0, getWidth(), centerY(), Path.Direction.CCW);

                mPath.addRect(0, 0, centerX(), centerY(), Path.Direction.CCW);
            }
//            else if(lType == Layout_Type.LOWER_HALF){
//                mPath.addRect(0, centerY(), getWidth(), getHeight(), Path.Direction.CCW);
//            }


            center_x = centerX();
            center_x += xShift;

            center_y += centerY();
            center_y += yShift;



        }

        public void redraw(int radius){
            this.radius = radius;
            invalidate();
        }

        public void setBgColor(int color){
            circlePaint.setColor(color);
        }

        @Override
        public void onDraw(Canvas canvas) {




//            mPath.addCircle(centerX(), centerY(), radiusInnerCircle, Path.Direction.CCW);
            if(lType!=Layout_Type.FULL_CIRCLE) {
//                canvas.clipPath(mPath, Region.Op.REPLACE);

                /*if(lType == Layout_Type.UPPER_HALF){
                    mPath.addRect(0, 0, getWidth(), centerY(), Path.Direction.CCW);
                    canvas.clipRect(0, 0, getWidth(), centerY(), Region.Op.REPLACE);
                }
                else if(lType == Layout_Type.LOWER_HALF){
                    mPath.addRect(0, centerY(), getWidth(), getHeight(), Path.Direction.CCW);
                    canvas.clipRect(0, centerY(), getWidth(), getHeight(), Region.Op.REPLACE);
                }
                else if(lType == Layout_Type.FIRST_QUARTER){
                    mPath.addRect(centerX(), 0, getWidth(), centerY(), Path.Direction.CCW);
                    canvas.clipRect(centerX(), 0, getWidth(), centerY(), Region.Op.REPLACE);
                }
                else if(lType == Layout_Type.THIRD_QUARTER){
                    mPath.addRect(0, centerX(), centerY(), getHeight(), Path.Direction.CCW);
                    canvas.clipRect(0, centerX(), centerY(), getHeight(), Region.Op.REPLACE);
                }
                else if(lType == Layout_Type.SECOND_QUARTER){
                    mPath.addRect(centerX(), centerY(), getWidth(), getHeight(), Path.Direction.CCW);
                    canvas.clipRect(centerX(), centerY(), getWidth(), getHeight(), Region.Op.REPLACE);
                }
                else if(lType == Layout_Type.FOURTH_QUARTER){
                    mPath.addRect(0, 0, centerX(), centerY(), Path.Direction.CCW);
                    canvas.clipRect(0, 0, centerX(), centerY(), Region.Op.REPLACE);
                }*/

            }
//            canvas.clipRect(0, 0, getWidth(), centerY(), Region.Op.REPLACE);

            canvas.drawCircle(centerX() + xShift, centerY() + yShift, radius, circlePaint);
//            canvas.drawText(String.valueOf(radius), centerX(), centerY(), textPaint);

//            canvas.clipPath(mPath, Region.Op.REVERSE_DIFFERENCE);
//            canvas.clipRect(0, centerY(), getWidth(), getHeight(), Region.Op.INTERSECT);
//        mPath.arcTo(0, centerY(), getWidth(), getHeight(),0, 90, true);
//        canvas.drawPath(mPath, circlePaint);
        }

       /* @Override
        public void setAlpha(int alpha) {

        }

        @Override
        public void setColorFilter(ColorFilter cf) {

        }

        @Override
        public int getOpacity() {
            return 0;
        }*/
    }

}
