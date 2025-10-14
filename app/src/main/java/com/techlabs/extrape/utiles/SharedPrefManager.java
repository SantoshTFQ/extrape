package com.techlabs.extrape.utiles;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    private static final String PREF_NAME = "myAppPrefs";
    private static SharedPrefManager instance;
    private Context ctx;

    private SharedPrefManager(Context ctx){ this.ctx = ctx; }

    public static synchronized SharedPrefManager getInstance(Context ctx){
        if(instance==null) instance = new SharedPrefManager(ctx);
        return instance;
    }

    public void saveUser(String userId, String name){
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        sp.edit().putString("user_id", userId).putString("name", name).apply();
    }

    public String getUserId(){
        return ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getString("user_id", "");
    }

    public String getUserName(){
        return ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getString("name", "");
    }
}
