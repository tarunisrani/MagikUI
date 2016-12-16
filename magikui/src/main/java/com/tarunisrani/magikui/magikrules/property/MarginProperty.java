package com.tarunisrani.magikui.magikrules.property;

import android.view.View;
import android.view.ViewGroup;

import com.tarunisrani.magikui.magikrules.MagikProperty;

import java.lang.reflect.Method;

/**
 * Created by tarunisrani on 7/5/16.
 */
public class MarginProperty extends MagikProperty {

    private ViewGroup.MarginLayoutParams mLayoutParams;
    private final String PROPERTY_NAME = "setLayoutParams";

    public MarginProperty(ViewGroup.LayoutParams layoutParams){
        propertyname = PROPERTY_NAME;
        parameters = new Class[]{ViewGroup.LayoutParams.class};
//        values = vals;
        setValues(new Object[]{layoutParams});
    }
    public MarginProperty(){
        propertyname = PROPERTY_NAME;
        parameters = new Class[]{ViewGroup.LayoutParams.class};
    }
//    public void setValues(Object[] vals){
//        values = vals;
//    }

    public void setValue(ViewGroup.LayoutParams layoutParams){
        setValues(new Object[]{layoutParams});
    }

    @Override
    protected void parseValue(String string) {
        if(string != null && !string.isEmpty()){
            if(string.contains(",")){
                String vals[] = string.split(",");
                int left = 0;
                int right = 0;
                int top = 0;
                int bottom = 0;

                if(vals[0]!=null && !vals[0].isEmpty()){
                    left = Integer.parseInt(vals[0].trim());
                }
                if(vals[1]!=null && !vals[1].isEmpty()){
                    right = Integer.parseInt(vals[1].trim());
                }
                if(vals[2]!=null && !vals[2].isEmpty()){
                    top = Integer.parseInt(vals[2].trim());
                }
                if(vals[3]!=null && !vals[3].isEmpty()){
                    bottom = Integer.parseInt(vals[3].trim());
                }

                ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(left, right);
                layoutParams.leftMargin = left;
                layoutParams.rightMargin = right;
                layoutParams.topMargin = top;
                layoutParams.bottomMargin = bottom;
                mLayoutParams = layoutParams;

            }
            else{
                int left = 0;
                int right = 0;
                int top = 0;
                int bottom = 0;

                if(string!=null && !string.isEmpty()){
                    left = right = top = bottom = Integer.parseInt(string.trim());
                }

                ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(left, right);
                layoutParams.leftMargin = left;
                layoutParams.rightMargin = right;
                layoutParams.topMargin = top;
                layoutParams.bottomMargin = bottom;
                mLayoutParams = layoutParams;
            }
        }
    }

    @Override
    protected void applyRule(View obj) {
        try {
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams)obj.getLayoutParams();
            layoutParams.leftMargin = mLayoutParams.leftMargin;
            layoutParams.rightMargin = mLayoutParams.rightMargin;
            layoutParams.topMargin = mLayoutParams.topMargin;
            layoutParams.bottomMargin = mLayoutParams.bottomMargin;

            setValue(layoutParams);
            Method m = obj.getClass().getMethod(getPropertyname(), getParameters());
            m.invoke(obj, getValues());
        }catch (Exception exp){
            exp.printStackTrace();
        }
    }
}
