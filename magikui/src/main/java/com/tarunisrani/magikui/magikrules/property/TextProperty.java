package com.tarunisrani.magikui.magikrules.property;

import android.view.View;

import com.tarunisrani.magikui.magikrules.MagikProperty;

import java.lang.reflect.Method;

/**
 * Created by tarunisrani on 7/5/16.
 */
public class TextProperty extends MagikProperty {

    private final String PROPERTY_NAME = "setText";

    public TextProperty(String string){
        propertyname = PROPERTY_NAME;
        parameters = new Class[]{CharSequence.class};
//        values = vals;
        setValues(new Object[]{string});
    }
    public TextProperty(){
        propertyname = PROPERTY_NAME;
        parameters = new Class[]{CharSequence.class};
    }
//    public void setValues(Object[] vals){
//        values = vals;
//    }

    public void setValue(String string){
        setValues(new Object[]{string});
    }

    @Override
    protected void parseValue(String string) {
        if(string != null){
            setValue(string);
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
