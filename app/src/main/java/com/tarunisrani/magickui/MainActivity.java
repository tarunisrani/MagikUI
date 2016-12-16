package com.tarunisrani.magickui;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.tarunisrani.magikui.magiklayouts.MagikLayout;
import com.tarunisrani.magikui.magiklayouts.TreeLayout;
import com.tarunisrani.magikui.magikrules.MagikRule;
import com.tarunisrani.magikui.magikrules.MagikRuleGenerator;
import com.tarunisrani.magikui.magikrules.property.BackgroundColorProperty;
import com.tarunisrani.magikui.magikrules.property.TextColorProperty;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends Activity {

    MagikLayout Layout;
    int counter = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AssetManager assetManager = getApplicationContext().getAssets();

        String content = "";

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(assetManager.open("rules.txt")));
            while(true) {
                String next = reader.readLine();
                if(next != null) {
                    content += next;
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.i("JSON", content);

//        MagikRuleGenerator generator = new MagikRuleGenerator(content);

        ArrayList<MagikRule> ruleList = null;

        try {
            MagikRuleGenerator generator = new MagikRuleGenerator(assetManager.open("rules.txt"));
            ruleList = generator.getRuleList();
        }catch (Exception exp){
            exp.printStackTrace();
        }


        Layout = (MagikLayout) findViewById(R.id.testLayout);

        TextColorProperty property = new TextColorProperty();
        property.setValue(Color.CYAN);

        BackgroundColorProperty property_2 = new BackgroundColorProperty();
        property_2.setValue(Color.RED);

        MagikRule rule_1 = new MagikRule(TextView.class, property);
        MagikRule rule_2 = new MagikRule(new Class[]{TextView.class, Button.class}, property_2);

        if(ruleList == null){
//            Toast.makeText(MainActivity.this, "rule List is null", Toast.LENGTH_SHORT).show();
        }
        else{
//            Toast.makeText(MainActivity.this, "rule List is OK", Toast.LENGTH_SHORT).show();
            Layout.addRule(ruleList);
        }


//        Layout.addRule(rule_1);
//        Layout.addRule(rule_2);
//        Layout.setlType(CircularLayout.Layout_Type.FOURTH_QUARTER);

        Button centerBtn = (Button) findViewById(R.id.centerButton);
        if(centerBtn!=null){
            centerBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button btn = new Button(getApplicationContext());
                    ViewGroup.LayoutParams btnlp = new ViewGroup.LayoutParams(100, 100);
//                    btnlp.width = 100;
//                    btnlp.height = 100;
                    btn.setLayoutParams(btnlp);
                    btn.setText(String.valueOf(counter++));
//                    Layout.addView(btn);
//                    Layout.requestLayout();
                    Layout.addViewWithAnimation(btn, TreeLayout.ANIMATION_APPEARE);
                }
            });
        }

    }
}
