package com.tarunisrani.magickui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tarunisrani.magikui.magiklayouts.TreeLayout;

public class MainActivity extends AppCompatActivity {

    TreeLayout Layout;
    int counter = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Layout = (TreeLayout) findViewById(R.id.testLayout);
//        Layout.setlType(CircularLayout.Layout_Type.FOURTH_QUARTER);

        Button centerBtn = (Button) findViewById(R.id.centerButton);
        if(centerBtn!=null){
            centerBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button btn = new Button(getApplicationContext());
                    ViewGroup.LayoutParams btnlp = new ViewGroup.LayoutParams(100,100);
//                    btnlp.width = 100;
//                    btnlp.height = 100;
                    btn.setLayoutParams(btnlp);
                    btn.setText(String.valueOf(counter++));
                    Layout.addView(btn);
                    Layout.requestLayout();
                }
            });
        }

    }
}
