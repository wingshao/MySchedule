package com.example.myschedule;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.concurrent.Delayed;

public class GuideActivity extends BaseActivity {

    Boolean flag=true;//标志符，否则一直跳转MainActivity

    int recLen=5;//倒计时

    Handler handler;

    Runnable runnable;

    Timer timer=new Timer();//定时器

    TextView count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        Intent intent=new Intent(GuideActivity.this,MainActivity.class);
        Intent intent1=new Intent(GuideActivity.this,AutoUpdateService.class);
        startService(intent1);
        count=(TextView)findViewById(R.id.count);
        timer.schedule(task,1000,1000);
        handler=new Handler();
        handler.postDelayed(runnable=new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                finish();
                Log.i("GuideAcitvity","->MainActivity");
            }
        },5000);
        TextView skipBtn=(TextView)findViewById(R.id.skip);
        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
                finish();
                if (runnable!=null){
                    handler.removeCallbacks(runnable);
                }
            }
        });
    }

    TimerTask task=new TimerTask() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    recLen--;
                    count.setText(recLen+"");
                    if (recLen==0){
                        timer.cancel();
                        count.setVisibility(View.GONE);
                    }
                }
            });
        }
    };
}