package com.example.myschedule;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Util {


    public static void sendRequestWithOkHttp(okhttp3.Callback callback){
        try{
            OkHttpClient client=new OkHttpClient();
            Request request=new Request.Builder()
                    .url("https://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=2&mkt=zh-CN")
                    .build();
            client.newCall(request).enqueue(callback);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 得到是否是夜间模式
     */
    public static boolean getNightMode(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("theme",Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("night",false);
    }

    /**
     * 设置夜间模式
     */
    public static void setNightMode(Context context, boolean mode) {
        if (mode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences("theme", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =  sharedPreferences.edit();
        editor.putBoolean("night", mode).commit();
    }
}
