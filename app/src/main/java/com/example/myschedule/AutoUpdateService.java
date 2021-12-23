package com.example.myschedule;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AutoUpdateService extends Service {
    public AutoUpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateBanner();
        AlarmManager manager=(AlarmManager)getSystemService(ALARM_SERVICE);
        int anDay=24*60*60*1000;
        long triggerAtTime= SystemClock.elapsedRealtime()+anDay;
        Intent i=new Intent(this,AutoUpdateService.class);
        PendingIntent pi=PendingIntent.getService(this,0,i,0);
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);
        return super.onStartCommand(intent,flags,startId);
    }

    private void updateBanner(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Util.sendRequestWithOkHttp(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Toast.makeText(getApplicationContext(),"网络连接超时",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseText=response.body().string();
                        Image image=new Gson().fromJson(responseText,Image.class);
                        String bannerUrl=image.getImages().get(0).getUrl();
                        String navUrl=image.getImages().get(1).getUrl();
                        SharedPreferences.Editor editor=getApplicationContext().getSharedPreferences("MySchedule", Context.MODE_PRIVATE).edit();
                        editor.putString("bannerImage","http://cn.bing.com"+bannerUrl);
                        editor.putString("navImage","http://cn.bing.com"+navUrl);
                        editor.apply();
                    }
                });
            }
        }).start();
    }
}