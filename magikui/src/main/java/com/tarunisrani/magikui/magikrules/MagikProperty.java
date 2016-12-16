package com.tarunisrani.magikui.magikrules;

import android.view.View;

/**
 * Created by tarunisrani on 7/5/16.
 */
public abstract class MagikProperty {
    protected String propertyname;
    protected Class[] parameters;
    protected Object[] values;

    public Class[] getParameters() {
        return parameters;
    }
    public String getPropertyname() {
        return propertyname;
    }
    public Object[] getValues() {
        return values;
    }

    protected void setPropertyname(String propertyname) {
        this.propertyname = propertyname;
    }
    protected void setParameters(Class[] parameters) {
        this.parameters = parameters;
    }
    protected void setValues(Object[] values) {
        this.values = values;
    }
    abstract protected void parseValue(String string);
    abstract protected void applyRule(View obj);
}
