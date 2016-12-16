package com.tarunisrani.magikui.magiklayouts;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import com.tarunisrani.magikui.R;

//import com.tarunisrani.magikui.R;

/**
 * Created by tarunisrani on 6/14/16.
 */
public class TreeLayout extends MagikLayout {

    private final int CONDENSED = 0;
    private final int ADJUST = 1;

    public static final int ANIMATION_SLIDE = 103;

    private int lType = 0;


    public TreeLayout(Context context) {
        super(context);
    }

    public TreeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttributes(attrs);
    }

    public TreeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttributes(attrs);
    }

    private void initAttributes(AttributeSet attrs){

        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.TreeLayout,
                0, 0);
        try {
            lType = a.getInteger(R.styleable.TreeLayout_layoutType, 0);
        } catch (Exception exp){
            String layoutType = a.getString(R.styleable.TreeLayout_layoutType);
            lType = layoutType!=null?layoutType.equals("adjust")?1:0:0;
        }
        finally {
            a.recycle();
        }
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
//                maxHight = Math.max(maxHight,child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);
            }
        }

        int desiredHeight = 0;

        for (int i = 0; i < levels; i++) {
            int index = (int)Math.pow(2, i);
            int range = index;
            maxHight = 0;
            for(int j=index-1;j<index-1+range;j++) {
                if(j<count) {
                    final View child = getChildAt(j);
                    if (child.getVisibility() != GONE) {
                        final LayoutParams lp = (LayoutParams) child.getLayoutParams();

//                        final int width = child.getMeasuredWidth();
                        final int height = child.getMeasuredHeight();

                        maxHight = Math.max(maxHight,height + lp.topMargin + lp.bottomMargin);
                    }
                }
            }
            desiredHeight +=maxHight;
        }

        /*int totalChildren = (int) Math.pow(2, levels) - 1;

        int numOfChildrenRemaining = totalChildren - count;
        if(numOfChildrenRemaining>0){
            for (int i=0;i<numOfChildrenRemaining;i++){
                addView(new View(getContext()), getChildAt(count-1).getWidth(), getChildAt(count-1).getHeight());
            }

        }*/

        int desiredWidth = maxWidth*childOffsetCount;
//        int desiredHeight = maxHight*levels;

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
//        super.onLayout(changed, left, top, right, bottom);

        if(lType == ADJUST){
            adjustLayout(changed, left, top, right, bottom);
        }
        else if(lType == CONDENSED){
            condensedLAyout(changed, left, top, right, bottom);
//            adjustLayout(changed, left, top, right, bottom);
        }

        if(performAnimationAfterLayouting && selectedAnimation!=0) {


            ValueAnimator animator = ValueAnimator.ofFloat(0.0f, 100.0f);
            if(selectedAnimation == ANIMATION_SLIDE) {
                attachSlideAnimation(animator);
            }
            else if(selectedAnimation == ANIMATION_APPEARE){
                attachAppearAnimation(animator);
            }
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

    private void condensedLAyout(boolean changed, int left, int top, int right, int bottom){
        int count = getChildCount();

        int xPos = 0;
        int yPos = 0;
        int rowHeight = 0;

        int levels = logBase2(getChildCount());

//        if(getChildAt(0)!=null){
//            ((Button)getChildAt(0)).setText(String.valueOf(levels));
//        }

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



//                        if(divider!=0) {
//                            if (childCount < divider) {
//                                xPos = centerX() - (divider-childCount) * width;
//                            } else {
//                                xPos = centerX() + (childCount-divider) * width;
//                            }
//                        }

                        ++childCount;

                        xPos += lp.leftMargin;
                        child.layout(xPos, yPos, xPos + width, yPos + height);
                        xPos += width;
                        xPos += lp.rightMargin;

                    }
                }

            }
            yPos += rowHeight;


        }
    }

    private void adjustLayout(boolean changed, int left, int top, int right, int bottom){
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
//        if(getChildAt(0)!=null){
//            ((Button)getChildAt(0)).setText(String.valueOf(levels));
//        }

//        for (int i = 0; i < levels; i++) {
        for (int i = levels-1; i >=0; i--) {
            int index = (int) Math.pow(2, i);
            int range = index;

            int rowWidth = 0;
            rowHeight = 0;

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
//            xPos = centerX() - rowWidth / 2;
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
//                                    xPos =  getChildAt(j-1).getRight() + (viewWidth)/(range - childCount);
                                    xPos =  getChildAt(j-1).getRight() + (child_one_avg_width/(leftchildCount*2)) + (child_two_avg_width/(rightchildCount*2));
                                    xPos += width/2;
                                }
//

                            }

                            viewWidth = getWidth() - child.getRight();

                            xPos -= width/2;

                            child.layout(xPos, yPos, xPos + width, yPos + height);
                            //TODO: Need to add provision of left and right margin for non-leaf nodes.
                        }
                        else{
//                            xPos = 0;
                            xPos += lp.leftMargin;
                            child.layout(xPos, yPos, xPos + width, yPos + height);
                            xPos += width;
                            xPos += lp.rightMargin;

                        }
                    }
                }

            }
//            yPos += rowHeight;
        }
    }

    private void attachSlideAnimation(ValueAnimator animator){
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            int count = getChildCount() - 1;
            int index_parent = (count - 1) / 2;
            final float x_parent = getChildAt(index_parent).getX();
            final float y_parent = getChildAt(index_parent).getY();

            float x_child = getChildAt(count).getX();
            float y_child = getChildAt(count).getY();

            final float delta_x = (x_child - x_parent) / 100.0f;
            final float delta_y = (y_child - y_parent) / 100.0f;

            final View animView = getChildAt(count);
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = ((Float) animation.getAnimatedValue()).floatValue();
                animView.setX(x_parent + delta_x * value);
                animView.setY(y_parent + delta_y * value);
            }
        });
    }



    /*@Override
    public void addViewWithAnimation(View child, int animCode) {

        addView(child);
        requestAnimation(animCode);
    }

    private void requestAnimation(int animCode){
        selectedAnimation = animCode;
        performAnimationAfterLayouting = true;
    }*/

}
