package com.tarunisrani.magikui.magikrules;

import android.view.View;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by tarunisrani on 7/5/16.
 */
public class MagikRule {
    private ArrayList<Class> applicableClasses;
    private MagikProperty applicableProperty;

    public MagikRule(){

    }

    public MagikRule(Class cls){
        applicableClasses = new ArrayList<Class>();
        applicableClasses.add(cls);
    }
    public MagikRule(Class[] clss){
        applicableClasses = new ArrayList<Class>();
        for(Class cls : clss) {
            applicableClasses.add(cls);
        }
    }
    public MagikRule(Collection<Class> clss){
        applicableClasses = new ArrayList<Class>();
        applicableClasses.addAll(clss);
    }
    public MagikRule(ArrayList<Class> cls){
        applicableClasses = cls;
    }

    public MagikRule(Class cls, MagikProperty property){
        applicableClasses = new ArrayList<Class>();
        applicableClasses.add(cls);
        applicableProperty = property;
    }
    public MagikRule(Class[] clss, MagikProperty property){
        applicableClasses = new ArrayList<Class>();
        for(Class cls : clss) {
            applicableClasses.add(cls);
        }
        applicableProperty = property;
    }
    public MagikRule(Collection<Class> clss, MagikProperty property){
        applicableClasses = new ArrayList<Class>();
        applicableClasses.addAll(clss);
        applicableProperty = property;
    }
    public MagikRule(ArrayList<Class> cls, MagikProperty property){
        applicableClasses = cls;
        applicableProperty = property;
    }



    public void applyOnClass(Class cls){
        applicableClasses.add(cls);
    }

    public void attachProperty(MagikProperty property){
        applicableProperty = property;
    }

    public void applyRule(View obj){
        if(applicableClasses ==  null || applicableClasses.isEmpty() || applicableProperty == null || obj == null)
            return;

//        Log.i("Class", obj.getClass().toString());


        for(Class applicableClass : applicableClasses) {
            if (obj.getClass().equals(applicableClass)) {
//                ((TextView)obj).setTextColor(Color.RED);
                try {
//                    Class cargs[] = new Class[1];
//                    cargs[0] = CharSequence.class;
//                    Method m = applicableClasses.getDeclaredMethod("setText", CharSequence.class);
//                    m.invoke(obj, "H");
//                    Method m = applicableClasses.getDeclaredMethod("setTextColor", int.class);
//                    m.invoke(obj, Color.BLUE);

//                    Method m = applicableClass.getDeclaredMethod(applicableProperty.getPropertyname(), applicableProperty.getParameters());
//                    Method m = applicableClass.getMethod(applicableProperty.getPropertyname(), applicableProperty.getParameters());
//                    m.invoke((applicableClasses.cast(obj)), applicableProperty.getValues());
//                    m.invoke(obj, applicableProperty.getValues());

                    applicableProperty.applyRule(obj);


//                    Field f = obj.getClass().getField("mTextColor");
//                    f.setInt(obj, Color.RED);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}


