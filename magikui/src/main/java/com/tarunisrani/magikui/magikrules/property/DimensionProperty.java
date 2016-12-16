package com.tarunisrani.magikui.magikrules.property;

import android.view.View;
import android.view.ViewGroup;

import com.tarunisrani.magikui.magikrules.MagikProperty;

import java.lang.reflect.Method;

/**
 * Created by tarunisrani on 7/5/16.
 */
public class DimensionProperty extends MagikProperty {

    private ViewGroup.LayoutParams mLayoutParams;
    private final String PROPERTY_NAME = "setLayoutParams";

    public DimensionProperty(ViewGroup.LayoutParams layoutParams){
        propertyname = PROPERTY_NAME;
        parameters = new Class[]{ViewGroup.LayoutParams.class};
//        values = vals;
        setValues(new Object[]{layoutParams});
    }
    public DimensionProperty(){
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
                int width = 0;
                int height = 0;

                if(vals[0]!=null && !vals[0].isEmpty()){
                    width = Integer.parseInt(vals[0].trim());
                }
                if(vals[1]!=null && !vals[1].isEmpty()){
                    height = Integer.parseInt(vals[1].trim());
                }

                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(width, height);
                mLayoutParams = layoutParams;

            }
            else{
                int width = 0;
                int height = 0;

                if(string!=null && !string.isEmpty()){
                    height = width = Integer.parseInt(string.trim());
                }

                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(width, height);
                mLayoutParams = layoutParams;
            }
        }
    }

    @Override
    protected void applyRule(View obj) {
        try {
            ViewGroup.LayoutParams layoutParams = obj.getLayoutParams();
            layoutParams.height = mLayoutParams.height;
            layoutParams.width = mLayoutParams.width;
            setValue(layoutParams);
            Method m = obj.getClass().getMethod(getPropertyname(), getParameters());
            m.invoke(obj, getValues());
        }catch (Exception exp){
            exp.printStackTrace();
        }
    }
}
