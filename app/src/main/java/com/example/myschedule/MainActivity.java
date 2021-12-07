package com.example.myschedule;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myschedule.history.HistoryActivity;
import com.example.myschedule.log.LogActivity;
import com.example.myschedule.plan.PlanActivity;
import com.example.myschedule.today.TodayActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private DrawerLayout mDrawerLayout;

    private NavigationView navView;

    private ImageView navHeadImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCollector.addActivity(this);
        init();
    }
    private void init(){
        findViewById(R.id.todayBtn).setOnClickListener(this::onClick);
        findViewById(R.id.open_drawer).setOnClickListener(this::onClick);
        findViewById(R.id.planBtn).setOnClickListener(this::onClick);
        findViewById(R.id.logBtn).setOnClickListener(this::onClick);
        findViewById(R.id.historyBtn).setOnClickListener(this::onClick);
        mDrawerLayout=(DrawerLayout)findViewById(R.id.draw_layout);

        navView=(NavigationView)findViewById(R.id.navView);
        View HeaderView=navView.getHeaderView(0);
        navHeadImage=(ImageView)HeaderView.findViewById(R.id.nav_image);
        showBanner();
        refreshNightModeTitle();
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                mDrawerLayout.closeDrawers();
                switch (item.getItemId()){
                    case R.id.nav_night_mode:
                        Log.i("MainActivity","NightMode="+Util.getNightMode(MainActivity.this));
                        if(Util.getNightMode(MainActivity.this)){   //当前为夜间模式，则恢复之前的主题
                            Util.setNightMode(MainActivity.this,false);  //点击之后是日间模式
                        }else {  //当前为日间模式，则切换到夜间模式
                            Util.setNightMode(MainActivity.this,true);
                        }
                        recreate();  //重新加载之后才能切换成功
                        refreshNightModeTitle();
                        break;
                    case R.id.nav_about:
                        Intent intent=new Intent(MainActivity.this,AboutActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_logout:
                        ActivityCollector.finishAll();
                        break;
                }
                return true;
            }
        });
    }

    private void showBanner(){
        ImageView imageView=(ImageView)findViewById(R.id.bannerImage);
        SharedPreferences pref=getSharedPreferences("MySchedule",MODE_PRIVATE);
        String getBannerUrl=pref.getString("bannerImage",null);
        String getNavUrl=pref.getString("navImage",null);
        if (getBannerUrl!=null){
            Glide.with(MainActivity.this).load(Uri.parse(getBannerUrl)).into(imageView);
        }else {
            getImage(getBaseContext());
            showBanner();
        }
        if (getNavUrl!=null){
            Glide.with(MainActivity.this).load(Uri.parse(getNavUrl)).into(navHeadImage);
        }else {
            getImage(getBaseContext());
            showBanner();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(MainActivity.this);
    }

    /**
     * 滑动窗口显示的文字
     */
    private void refreshNightModeTitle(){
        if (Util.getNightMode(MainActivity.this)){
            navView.getMenu().findItem(R.id.nav_night_mode).setTitle("日间模式");
        }else {
            navView.getMenu().findItem(R.id.nav_night_mode).setTitle("夜间模式");
        }
    }

    private void getImage(Context context){
        Util.sendRequestWithOkHttp(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(context,"网络连接超时",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText=response.body().string();
                Image image=new Gson().fromJson(responseText,Image.class);
                String bannerUrl=image.getImages().get(0).getUrl();
                String navUrl=image.getImages().get(1).getUrl();
                SharedPreferences.Editor editor=context.getSharedPreferences("MySchedule",Context.MODE_PRIVATE).edit();
                editor.putString("bannerImage","http://cn.bing.com"+bannerUrl);
                editor.putString("navImage","http://cn.bing.com"+navUrl);
                editor.apply();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.open_drawer:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.todayBtn:
                Intent intent=new Intent(MainActivity.this, TodayActivity.class);
                startActivity(intent);
                break;
            case R.id.planBtn:
                Intent intent1=new Intent(MainActivity.this, PlanActivity.class);
                startActivity(intent1);
                break;
            case R.id.logBtn:
                Intent intent2=new Intent(MainActivity.this, LogActivity.class);
                startActivity(intent2);
                break;
            case R.id.historyBtn:
                Intent intent3=new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(intent3);
                break;
        }
    }
}