package com.example.myschedule.plan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.myschedule.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.Calendar;
import java.util.TimeZone;

public class ScheduleAcitvity extends AppCompatActivity {

    private Toolbar toolbar;

    private MaterialCalendarView calendarView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("计划");
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        calendarView=(MaterialCalendarView)findViewById(R.id.calendarView);
        Calendar calendar=Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String todayDay=String.valueOf(calendar.get(Calendar.DATE));
        String todayMonth=String.valueOf(calendar.get(Calendar.MONTH)+1);
        String todayYear=String.valueOf(calendar.get(Calendar.YEAR));
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(MaterialCalendarView widget, CalendarDay date, boolean selected) {
                String year=String.valueOf(date.getYear());
                String month=String.valueOf(date.getMonth()+1);
                String day=String.valueOf(date.getDay());
                Log.d("scheduleActivity","year="+year+",todayYear="+todayYear);
                Log.d("scheduleActivity","month="+month+",todayMonth="+todayMonth);
                Log.d("scheduleActivity","day="+day+",todayDay="+todayDay);
                if (((year.equals(todayYear) && month.equals(todayMonth) && Integer.parseInt(day) < Integer.parseInt(todayDay))) || (year.equals(todayYear) && Integer.parseInt(month) < Integer.parseInt(todayMonth)) || (Integer.parseInt(year) < Integer.parseInt(todayYear))){
                    Toast.makeText(ScheduleAcitvity.this,"请选择未来日期",Toast.LENGTH_SHORT).show();

                }else {
                    Intent intent=new Intent(ScheduleAcitvity.this, PlanActivity.class);
                    intent.putExtra("day",day);
                    intent.putExtra("month",month);
                    intent.putExtra("year",year);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                break;
        }
        return true;
    }
}