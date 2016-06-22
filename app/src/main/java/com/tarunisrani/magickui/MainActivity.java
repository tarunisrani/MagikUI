package com.tarunisrani.magickui;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tarunisrani.magikui.magiklayouts.CircularLayout;

public class MainActivity extends AppCompatActivity {

    CircularLayout cLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cLayout = (CircularLayout) findViewById(R.id.testLayout);
        cLayout.setlType(CircularLayout.Layout_Type.FOURTH_QUARTER);

    }

    public void playAnimation(){
        int finalvalue = cLayout.getRadiusCircle();
        ValueAnimator anim = ValueAnimator.ofInt(0, finalvalue);
        /*anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int val = ((Integer) animation.getAnimatedValue()).intValue();
                cLayout.performLayouting(val);

            }
        });*/
        anim.addUpdateListener(cLayout.getUpdateListener());
        anim.setDuration(5000);
//        anim.start();

    }
}
