package com.tarunisrani.magikui.magiklayouts;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.tarunisrani.magikui.R;

//import com.tarunisrani.magikui.R;

/**
 * Created by tarunisrani on 6/14/16.
 */

public class CircularLayout extends MagikLayout {

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

    private BackgroundPath drawable;

    private boolean performAnimationAfterLayouting = false;
    private int selectedAnimation = 0;

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
        initialize();
    }

    public CircularLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttributes(attrs);
        initialize();
    }

    public CircularLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttributes(attrs);
        initialize();
    }

    private void initAttributes(AttributeSet attrs) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CircularLayout,
                0, 0);

        try {

            lType = a.getInteger(R.styleable.CircularLayout_circleType, FULL_CIRCLE);
            circularPathColor = a.getColor(R.styleable.CircularLayout_circularPathColor, Color.TRANSPARENT);

        } catch (Exception exp) {
            String circleType = a.getString(R.styleable.CircularLayout_circleType);
            lType = circleType != null ? circleType.equals("full") ? 101 : lType : lType;
            lType = circleType != null ? circleType.equals("upper_half") ? 102 : lType : lType;
            lType = circleType != null ? circleType.equals("lower_half") ? 103 : lType : lType;
            lType = circleType != null ? circleType.equals("first_quarter") ? 104 : lType : lType;
            lType = circleType != null ? circleType.equals("second_quarter") ? 105 : lType : lType;
            lType = circleType != null ? circleType.equals("third_quarter") ? 106 : lType : lType;
            lType = circleType != null ? circleType.equals("fourth_quarter") ? 107 : lType : lType;
        } finally {
            a.recycle();
        }
    }

    private int centerY(int offset) {
        return getHeight() / 2 + offset;
    }

    private int centerY() {
        return centerY(0);
    }

    private void initialize() {
        drawable = new BackgroundPath(getContext());
        addView(drawable, 0);
    }

    private int centerX() {
        return getWidth() / 2;
    }

    private float getYCoordinate(double angle) {
        return getYCoordinate(angle, radiusCircle);
    }

    private float getYCoordinate(double angle, int radius) {
        double sinTheta = Math.sin(Math.toRadians(angle));
        double yPos = radius * sinTheta;

        return (float) yPos;
    }

    private float getXCoordinate(double angle) {
        return getXCoordinate(angle, radiusCircle);
    }

    private float getXCoordinate(double angle, int radius) {
        double cosTheta = Math.cos(Math.toRadians(angle));
        double xPos = radius * cosTheta;

        return (float) xPos;
    }

    public void setBackgroundColor(int color) {
        if (drawable != null) {
            drawable.setBgColor(color);
        }
    }


    public void playExpandAnimation() {
        playExpandAnimation(500);
    }

    public void playExpandAnimation(int time) {
        int finalvalue = radiusCircle;
        ValueAnimator anim = ValueAnimator.ofInt(0, finalvalue);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int val = ((Integer) animation.getAnimatedValue()).intValue();
//                ((Button)getChildAt(0)).setText(String.valueOf(val));
                performLayouting(val);
                drawable.redraw(val);
                invalidate();
            }
        });
        anim.setDuration(time);
        anim.start();
    }

    public ValueAnimator.AnimatorUpdateListener getUpdateListener() {
        return new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int val = ((Integer) animation.getAnimatedValue()).intValue();
//                ((Button)getChildAt(0)).setText(String.valueOf(val));


                performLayouting(val);
                drawable.redraw(val);
            }
        };
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

        int diagonal = (int) Math.sqrt(maxHeight * maxHeight + maxWidth * maxWidth);

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


        if (lType == FIRST_QUARTER || lType == SECOND_QUARTER || lType == THIRD_QUARTER || lType == FOURTH_QUARTER) {
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

        if (lType == FULL_CIRCLE) {
            maxHeight = Math.max(maxHeight, maxWidth);
            maxWidth = maxHeight;
        } else if (lType == SECOND_QUARTER || lType == FOURTH_QUARTER) {
            int lastchildwidth = getChildAt(getChildCount() - 1).getMeasuredWidth();
            shiftangle = (Math.atan2(lastchildwidth / 2, (double) radiusCircle) * 180) / Math.PI;
            maxWidth = maxWidth / 2;
        } else if (lType == FIRST_QUARTER || lType == THIRD_QUARTER) {
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


        if (lType == FULL_CIRCLE) {
            maxHeight = Math.max(maxHeight, maxWidth);
            maxWidth = maxHeight;
        } else if (lType == UPPER_HALF || lType == FIRST_QUARTER || lType == FOURTH_QUARTER) {
            yShift = height / 2;

        } else if (lType == LOWER_HALF || lType == SECOND_QUARTER || lType == THIRD_QUARTER) {
            yShift = -height / 2;

        }

//        if (lType == Layout_Type.SECOND_QUARTER || lType == Layout_Type.FOURTH_QUARTER) {
//            int lastchildwidth = getChildAt(getChildCount() - 1).getMeasuredWidth();
//            shiftangle = (Math.atan2(lastchildwidth / 2, (double) radiusCircle) * 180) / Math.PI;
//        } else if (lType == Layout_Type.FIRST_QUARTER || lType == Layout_Type.THIRD_QUARTER) {
//            int lastchildwidth = getChildAt(2).getMeasuredWidth();
//            shiftangle = (Math.atan2(lastchildwidth / 2, (double) radiusCircle) * 180) / Math.PI;
//        }

        if (lType == FIRST_QUARTER || lType == SECOND_QUARTER) {
            xShift = -width / 2;
        } else if (lType == THIRD_QUARTER || lType == FOURTH_QUARTER) {
            xShift = width / 2;
        }


        if (lType == FULL_CIRCLE || lType == UPPER_HALF || lType == LOWER_HALF) {
            radiusOuterCircle = Math.min(width, height) / 2;
        } else {
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

        /*int maxWidth = 0;
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
        }*/


        performLayouting(radiusCircle);

        if (performAnimationAfterLayouting && selectedAnimation != 0) {


            ValueAnimator animator = ValueAnimator.ofFloat(0.0f, 100.0f);

            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    performAnimationAfterLayouting = false;
                    selectedAnimation = 0;
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            animator.setDuration(ANIMATION_DEFAULT_DURATION);
            animator.start();

        }
    }

    public void performLayouting(int radius) {
        int count = getChildCount() - 2;
        double stepAngle = 0.0;

        double ang = 0.0;


        if (lType == FULL_CIRCLE) {
            endAngle = 360.0;
        } else if (lType == LOWER_HALF) {
            endAngle = 180.0;
        } else if (lType == UPPER_HALF) {
            endAngle = 180.0;
            ang = -180.0;
        } else if (lType == FIRST_QUARTER) {
            endAngle = 90.0;
            ang = -90.0;
            ang += shiftangle;
            endAngle -= shiftangle;
        } else if (lType == SECOND_QUARTER) {
            endAngle = 90.0;
            endAngle -= shiftangle;
        } else if (lType == THIRD_QUARTER) {
            endAngle = 90.0;
            ang = 90.0;
            ang += shiftangle;
            endAngle -= shiftangle;
        } else if (lType == FOURTH_QUARTER) {
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

        stepAngle = endAngle / count;
        ang += stepAngle / 2;
//        mPaintEmptyCircle.setColor(Color.WHITE);

//        addView();


//        mPaintEmptyCircle.setColor(Color.RED);
        /*for(ang = 0.0;ang<=endAngle;ang = ang+1.2){
            float x = getXCoordinate(ang);
            float y = getYCoordinate(ang);

//            drawCircularBalls(canvas, x, y, radiusInnerCircularBalls, mPaintEmptyCircle);
        }*/


        int bg_x = (int) getChildAt(0).getX();
        bg_x += xShift;
        bg_x -= getChildAt(1).getMeasuredWidth() / 2;

        int bg_y = (int) getChildAt(0).getY();
        bg_y += yShift;
//        bg_y -= getChildAt(1).getMeasuredHeight();

//        getChildAt(0).layout(bg_x, bg_y, bg_x + getChildAt(0).getMeasuredWidth(), bg_y + getChildAt(0).getMeasuredHeight());


        int center_x = centerX();
        center_x += xShift;
        center_x -= getChildAt(1).getMeasuredWidth() / 2;


        int center_y = centerY();
//        center_y -= getChildAt(1).getMeasuredHeight()/2;
//        if(lType == Layout_Type.UPPER_HALF || lType == Layout_Type.LOWER_HALF) {
        if (lType != FULL_CIRCLE) {
            center_y += yShift;
        } else {
            center_y -= getChildAt(1).getMeasuredHeight() / 2;
        }
//        }
        if (lType == UPPER_HALF || lType == FIRST_QUARTER || lType == FOURTH_QUARTER) {
            center_y -= getChildAt(1).getMeasuredHeight();
        } else if (lType == LOWER_HALF || lType == SECOND_QUARTER || lType == THIRD_QUARTER) {
//            center_y += getChildAt(1).getMeasuredHeight()/2;
        }

        if (lType == FIRST_QUARTER || lType == SECOND_QUARTER) {
            center_x += getChildAt(1).getMeasuredHeight();
        } else if (lType == THIRD_QUARTER || lType == FOURTH_QUARTER) {
            center_x -= getChildAt(1).getMeasuredHeight();
        }

        getChildAt(1).layout(center_x, center_y, center_x + getChildAt(1).getMeasuredWidth(), center_y + getChildAt(1).getMeasuredHeight());


        for (int i = 2; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                final LayoutParams lp = (LayoutParams) child.getLayoutParams();

                final int width = child.getMeasuredWidth();
                final int height = child.getMeasuredHeight();


                int x = (int) getXCoordinate(ang, radius);
                x += centerX();
                x += xShift;
                x -= width / 2;


                int y = (int) getYCoordinate(ang, radius);
                y += centerY();
                y += yShift;
                y -= height / 2;

                ang = ang + stepAngle;

//                child.layout(childLeft, childTop, childLeft + width, childTop + height);
                child.layout(x, y, x + width, y + height);
            }
        }
    }
    /*@Override
    public void addViewWithAnimation(View child, int animCode) {

        addView(child);
//        forceLayout();
        requestAnimation(animCode);
    }

    private void requestAnimation(int animCode){
        selectedAnimation = animCode;
        performAnimationAfterLayouting = true;
    }*/

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


        public void initialize() {
            circlePaint.setColor(circularPathColor);
            circlePaint.setStrokeWidth(widthCircularPath);
            circlePaint.setStyle(Paint.Style.STROKE);

            if (lType == UPPER_HALF) {
                mPath.addRect(0, 0, getWidth(), centerY(), Path.Direction.CW);
            } else if (lType == LOWER_HALF) {
                mPath.addRect(0, centerY(), getWidth(), getHeight(), Path.Direction.CCW);
            } else if (lType == FIRST_QUARTER) {
                mPath.addRect(centerX(), 0, getWidth(), centerY(), Path.Direction.CCW);
            } else if (lType == THIRD_QUARTER) {
                mPath.addRect(0, centerX(), centerY(), getHeight(), Path.Direction.CCW);
            } else if (lType == SECOND_QUARTER) {
                mPath.addRect(centerX(), centerY(), getWidth(), getHeight(), Path.Direction.CCW);
            } else if (lType == FOURTH_QUARTER) {
                mPath.addRect(0, 0, centerX(), centerY(), Path.Direction.CCW);
            }
            center_x = centerX();
            center_x += xShift;

            center_y += centerY();
            center_y += yShift;
        }

        public void redraw(int radius) {
            this.radius = radius;
            invalidate();
        }

        public void setBgColor(int color) {
            circlePaint.setColor(color);
        }

        @Override
        public void onDraw(Canvas canvas) {

//            mPath.addCircle(centerX(), centerY(), radiusInnerCircle, Path.Direction.CCW);
            if (lType != FULL_CIRCLE) {
//                canvas.clipPath(mPath, Region.Op.REPLACE);
            }
            canvas.drawCircle(centerX() + xShift, centerY() + yShift, radius, circlePaint);
        }
    }

}
