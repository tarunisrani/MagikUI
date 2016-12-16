package com.tarunisrani.magikui.magikrules.property;

import android.view.View;
import android.view.ViewGroup;

import com.tarunisrani.magikui.magikrules.MagikProperty;

import java.lang.reflect.Method;

/**
 * Created by tarunisrani on 7/5/16.
 */
public class PaddingProperty extends MagikProperty {

    private ViewGroup.MarginLayoutParams mLayoutParams;
    private final String PROPERTY_NAME = "setPadding";

    public PaddingProperty(ViewGroup.LayoutParams layoutParams){
        propertyname = PROPERTY_NAME;
        parameters = new Class[]{int.class, int.class, int.class, int.class};
//        values = vals;
        setValues(new Object[]{layoutParams});
    }
    public PaddingProperty(){
        propertyname = PROPERTY_NAME;
        parameters = new Class[]{int.class, int.class, int.class, int.class};
    }
//    public void setValues(Object[] vals){
//        values = vals;
//    }

    public void setValue(int left, int top, int right, int bottom){
        setValues(new Object[]{left, top, right, bottom});
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

                setValue(left, top, right, bottom);

            }
            else{
                int left = 0;
                int right = 0;
                int top = 0;
                int bottom = 0;

                if(string!=null && !string.isEmpty()){
                    left = right = top = bottom = Integer.parseInt(string.trim());
                }

                setValue(left, top, right, bottom);
            }
        }
    }

    @Override
    protected void applyRule(View obj) {
        try {

            Method m = obj.getClass().getMethod(getPropertyname(), getParameters());
            m.invoke(obj, getValues());
        }catch (Exception exp){
            exp.printStackTrace();
        }
    }
}
