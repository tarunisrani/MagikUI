package com.tarunisrani.magikui.magiklayouts;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.tarunisrani.magikui.magikrules.MagikRule;

import java.util.ArrayList;

/**
 * Created by tarunisrani on 7/8/16.
 */
abstract public class MagikLayout extends FrameLayout {

    private ArrayList<MagikRule> magikRuleList;
    protected final int ANIMATION_DEFAULT_DURATION = 200;
    protected boolean performAnimationAfterLayouting = false;
    protected int selectedAnimation = 0;
    public static final int ANIMATION_APPEARE = 101;


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        applyRules();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public MagikLayout(Context context) {
        super(context);
        magikRuleList = new ArrayList<MagikRule>();
    }

    public MagikLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        magikRuleList = new ArrayList<MagikRule>();
    }

    public MagikLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        magikRuleList = new ArrayList<MagikRule>();
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

    public void addRule(MagikRule rl){
        magikRuleList.add(rl);
    }
    public void addRule(ArrayList<MagikRule> rl){
//        magikRuleList.addAll(rl);
        magikRuleList = rl;
    }

    private void applyRules(){

        //Spread the rules in each and every child element which are derived from magiklayout.
        for(int i=0;i<getChildCount();i++){
            View obj = getChildAt(i);
            if(obj!=null){
                if(obj.getClass().getSuperclass().equals(MagikLayout.class)) {
                    ((MagikLayout)obj).addRule(magikRuleList);
                }
            }
        }


        if(magikRuleList !=null){
            for(int i=0;i<getChildCount();i++){
                View obj = getChildAt(i);
                if(obj!=null){
                    for(MagikRule rl : magikRuleList){

                        if(obj.getClass().getSuperclass().equals(MagikLayout.class)){
                            ((MagikLayout)obj).applyRules();
                        }
                        else{
                            rl.applyRule(obj);
                        }
                    }
                }
            }
        }
    }


    public void addViewWithAnimation(View child, int animCode) {

        addView(child);
        requestAnimation(animCode);
    }

    private void requestAnimation(int animCode){
        selectedAnimation = animCode;
        performAnimationAfterLayouting = true;
    }

    protected void attachAppearAnimation(ValueAnimator animator){
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            int count = getChildCount() - 1;

            final View animView = getChildAt(count);

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = ((Float) animation.getAnimatedValue()).floatValue();
                value  = value/100.0f;
                animView.setScaleX(value);
                animView.setScaleY(value);
            }
        });
    }

    protected void attachDropAnimation(ValueAnimator animator){
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            int count = getChildCount() - 1;
            
            float y_child = getChildAt(count).getY();

            final float delta_y = (y_child ) / 100.0f;

            final View animView = getChildAt(count);
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = ((Float) animation.getAnimatedValue()).floatValue();
//                animView.setX(x_parent + delta_x * value);
                animView.setY(delta_y * value);
            }
        });
    }

}
