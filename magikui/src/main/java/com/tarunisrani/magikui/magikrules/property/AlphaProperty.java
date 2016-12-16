package com.tarunisrani.magikui.magikrules.property;

import android.view.View;

import com.tarunisrani.magikui.magikrules.MagikProperty;

import java.lang.reflect.Method;

/**
 * Created by tarunisrani on 7/5/16.
 */
public class AlphaProperty extends MagikProperty {

    private final String PROPERTY_NAME = "setAlpha";

    public AlphaProperty(float alpha){
        propertyname = PROPERTY_NAME;
        parameters = new Class[]{float.class};
//        values = vals;
        setValues(new Object[]{alpha});
    }
    public AlphaProperty(){
        propertyname = PROPERTY_NAME;
        parameters = new Class[]{float.class};
    }
//    public void setValues(Object[] vals){
//        values = vals;
//    }

    public void setValue(float alpha){
        setValues(new Object[]{alpha});
    }

    @Override
    protected void parseValue(String string) {
        if(string != null && !string.isEmpty()){
            setValue(Float.parseFloat(string));
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
