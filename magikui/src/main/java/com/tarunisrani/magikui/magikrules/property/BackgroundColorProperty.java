package com.tarunisrani.magikui.magikrules.property;

import android.graphics.Color;
import android.view.View;

import com.tarunisrani.magikui.magikrules.MagikProperty;

import java.lang.reflect.Method;

/**
 * Created by tarunisrani on 7/5/16.
 */
public class BackgroundColorProperty extends MagikProperty {

    private final String PROPERTY_NAME = "setBackgroundColor";

    public BackgroundColorProperty(int color){
        propertyname = PROPERTY_NAME;
        parameters = new Class[]{int.class};
        setValues(new Object[]{color});
    }
    public BackgroundColorProperty(){
        propertyname = PROPERTY_NAME;
        parameters = new Class[]{int.class};
    }

    public void setValue(int color){
        setValues(new Object[]{color});
    }

    @Override
    protected void parseValue(String string) {
        if(string != null && !string.isEmpty()){
            setValue(Color.parseColor(string));
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
