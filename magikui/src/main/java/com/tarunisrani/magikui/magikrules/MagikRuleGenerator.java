package com.tarunisrani.magikui.magikrules;

import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.tarunisrani.magikui.magikrules.property.AlphaProperty;
import com.tarunisrani.magikui.magikrules.property.BackgroundColorProperty;
import com.tarunisrani.magikui.magikrules.property.DimensionProperty;
import com.tarunisrani.magikui.magikrules.property.MarginProperty;
import com.tarunisrani.magikui.magikrules.property.PaddingProperty;
import com.tarunisrani.magikui.magikrules.property.TextColorProperty;
import com.tarunisrani.magikui.magikrules.property.TextProperty;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by tarunisrani on 7/6/16.
 */
public class MagikRuleGenerator {

    private final String CLASS_TEXTVIEW = "textview";
    private final String CLASS_BUTTON = "button";


    private final String PROPERTY_TEXTCOLOR = "textcolor";
    private final String PROPERTY_BGCOLOR = "bgcolor";
    private final String PROPERTY_DIMENSION = "dimension";
    private final String PROPERTY_TEXT = "text";
    private final String PROPERTY_ALPHA = "alpha";
    private final String PROPERTY_MARGIN = "margin";
    private final String PROPERTY_PADDING = "padding";

    private final String TAG_RULES = "rules";
    private final String TAG_CLASS = "class";
    private final String TAG_PROPERTY = "property";
    private final String TAG_TYPE = "type";
    private final String TAG_VALUE = "value";

    private HashMap<String, Class> class_collection;
    private HashMap<String, MagikProperty> properties_collection;

    public ArrayList<MagikRule> getRuleList() throws JSONException{
        generate();
        return ruleList;
    }

    public void setRuleList(ArrayList<MagikRule> ruleList) {
        this.ruleList = ruleList;
    }

    private ArrayList<MagikRule> ruleList;

    private JSONObject jsonObject;
    public MagikRuleGenerator(JSONObject jsonObject){
        generateClassCollection();
        generatePropertiesCollection();
        this.jsonObject = jsonObject;
        this.ruleList = new ArrayList<MagikRule>();
    }
    public MagikRuleGenerator(String string){
        generateClassCollection();
        generatePropertiesCollection();
        this.jsonObject = null;
        this.ruleList = null;
        try {
            jsonObject = new JSONObject(string);
            ruleList = new ArrayList<MagikRule>();
        }catch (JSONException exp){
            exp.printStackTrace();
        }

    }
    public MagikRuleGenerator(InputStream inputStream) throws IOException{
        generateClassCollection();
        generatePropertiesCollection();

        String content = "";
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            while(true) {
                String next = reader.readLine();
                if(next != null) {
                    content += next;
                } else {
                    break;
                }
            }
        } catch (IOException e) {
//            e.printStackTrace();
            throw e;
        }
        this.jsonObject = null;
        this.ruleList = null;
        try {
            jsonObject = new JSONObject(content);
            ruleList = new ArrayList<MagikRule>();
        }catch (JSONException exp){
            exp.printStackTrace();
        }
    }

    private void generateClassCollection(){
        class_collection = new HashMap<String, Class>();
        class_collection.put(CLASS_TEXTVIEW, TextView.class);
        class_collection.put(CLASS_BUTTON, Button.class);
        Log.i("generateClass", "COMPLETED");
    }

    private void generatePropertiesCollection(){
        properties_collection = new HashMap<String, MagikProperty>();
        properties_collection.put(PROPERTY_TEXTCOLOR, new TextColorProperty());
        properties_collection.put(PROPERTY_BGCOLOR, new BackgroundColorProperty());
        properties_collection.put(PROPERTY_DIMENSION, new DimensionProperty());
        properties_collection.put(PROPERTY_TEXT, new TextProperty());
        properties_collection.put(PROPERTY_ALPHA, new AlphaProperty());
        properties_collection.put(PROPERTY_MARGIN, new MarginProperty());
        properties_collection.put(PROPERTY_PADDING, new PaddingProperty());
        Log.i("generateProperties", "COMPLETED");
    }

    private void generate() throws JSONException{
        Log.i("generate", "STARTED");
        if(jsonObject == null || ruleList == null){
            return;
        }

        try {
            JSONArray rules = jsonObject.getJSONArray(TAG_RULES);
            for(int i=0; i<rules.length();i++){
                JSONObject object = (JSONObject)rules.get(i);
                String applicableclasses = object.getString(TAG_CLASS);
                JSONObject property = object.getJSONObject(TAG_PROPERTY);
                ArrayList<Class> list = getApplicableClasses(applicableclasses);
                MagikProperty magikProperty = getMagikProperty(property);
                MagikRule rule = new MagikRule(list, magikProperty);
                ruleList.add(rule);

            }
        }catch(JSONException exp){
            ruleList = null;
//            Log.i("generate", "PROBLEM");
//            exp.printStackTrace();
            throw exp;
        }
        Log.i("generate", "COMPLETED");
    }

    private ArrayList<Class> getApplicableClasses(String string){
        if(string == null || string.isEmpty())
        return null;

        ArrayList<Class> list = new ArrayList<Class>();

        if(string.contains(",")){
            String collection[] = string.split(",");
            for(String str : collection){
                if(str!=null && !str.isEmpty()){
                    str = str.trim();
                    if(class_collection.containsKey(str)){
                        Class cls = class_collection.get(str);
                        list.add(cls);
                    }
                }
            }
        }
        else {
            if(string!=null && !string.isEmpty()){
                string = string.trim();
                if(class_collection.containsKey(string)){
                    Class cls = class_collection.get(string);
                    list.add(cls);
                }
            }
        }
        return list;
    }

    private MagikProperty getMagikProperty(JSONObject property) throws JSONException{
        if(property == null)
            return null;

        MagikProperty magikProperty = null;
        try{
            String type = property.getString(TAG_TYPE);
            String value = property.getString(TAG_VALUE);
            if(properties_collection.containsKey(type)) {
                magikProperty = properties_collection.get(type);
                magikProperty.parseValue(value);
            }

        }catch (JSONException exp){
//            exp.printStackTrace();
            throw exp;
        }

        return magikProperty;
    }
}
