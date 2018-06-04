package com.sadeny.alphaim;


import android.content.Context;
import android.content.SharedPreferences;
import android.security.keystore.KeyNotYetValidException;

import java.security.Key;

public class DataClass {
    private Context context;
    public static final String MY_PREF="myname";
    public static final String NAME_KEY="nameKey";
    public static final String PASS_KEY="passKey";
    public static final String IMG_KEY="imgKey";
    final SharedPreferences sharedPref ;


    public DataClass(Context context)
    {
        this.context = context;
        sharedPref= context.getSharedPreferences(MY_PREF,context.MODE_PRIVATE);
    }
    public void setName(String name)
    {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(NAME_KEY,name);
        editor.commit();
    }
    public void setPass(String pass)
    {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(PASS_KEY,pass);
        editor.commit();
    }
    public String getName()
    {
        String name = sharedPref.getString(NAME_KEY,null);
        return name;
    }

    public String getPass()
    {
        String pass = sharedPref.getString(PASS_KEY,null);
        return pass;
    }
    public void setImage(String img)
    {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(IMG_KEY,img);
        editor.commit();
    }
    public String getImage()
    {
        String img = sharedPref.getString(IMG_KEY,null);
        return img;
    }
    public void RemoveKey()
    {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(PASS_KEY);
        editor.remove(NAME_KEY);
        editor.remove(IMG_KEY);
        editor.apply();
    }
}