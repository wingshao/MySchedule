package com.example.myschedule.history;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.myschedule.R;
import com.example.myschedule.db.Plan;
import com.example.myschedule.plan.PlanAdapter;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class HistoryActivity extends AppCompatActivity {

    private List<Plan> dataList=new ArrayList<>();

    private List<Plan> scheduleList=new ArrayList<>();

    public static PlanAdapter planAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("历史");
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        RecyclerView recyclerView=(RecyclerView) findViewById(R.id.plan_recyclerview);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        planAdapter=new PlanAdapter(scheduleList);
        recyclerView.setAdapter(planAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Calendar calendar=Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));  //设置为中国区的时间
        String todayYear=String.valueOf(calendar.get(Calendar.YEAR));
        String todayMonth=String.valueOf(calendar.get(Calendar.MONTH)+1);
        String todayDay=String.valueOf(calendar.get(Calendar.DATE));
        Log.d("historyActivity","today is "+todayYear+"年"+todayMonth+"月"+todayDay+"日");
        dataList= LitePal.findAll(Plan.class);
        if (dataList.size() > 0){
            scheduleList.clear();
            for(Plan plan : dataList){
                if((plan.getYear().equals(todayYear) && plan.getMonth().equals(todayMonth) && Integer.parseInt(plan.getDay()) < Integer.parseInt(todayDay) ) || ( plan.getYear().equals(todayYear) && Integer.parseInt(plan.getMonth()) < Integer.parseInt(todayMonth) ) || (Integer.parseInt(plan.getYear()) < Integer.parseInt(todayYear))||plan.isStatus()){  //只要是历史记录就会显示
                    scheduleList.add(plan);
                    //排序
                }
            }
        }else scheduleList.clear();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                break;
        }
        return true;
    }
}