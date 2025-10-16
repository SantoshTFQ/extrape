package com.techlabs.extrape.user;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    private static SharedPrefManager instance;
    private SharedPreferences prefs;
    private SharedPrefManager(Context ctx){
        prefs = ctx.getSharedPreferences("extrape_prefs", Context.MODE_PRIVATE);
    }
    public static SharedPrefManager getInstance(Context ctx){
        if(instance==null) instance = new SharedPrefManager(ctx.getApplicationContext());
        return instance;
    }
    public void saveUserId(String id){ prefs.edit().putString("user_id", id).apply(); }
    public String getUserId(){ return prefs.getString("user_id", null); }
}