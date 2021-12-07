package com.example.myschedule.plan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.myschedule.db.Plan;
import com.example.myschedule.R;
import com.example.myschedule.today.TodayAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import per.goweii.anylayer.AnyLayer;
import per.goweii.anylayer.Layer;
import per.goweii.anylayer.dialog.DialogLayer;
import per.goweii.anylayer.utils.AnimatorHelper;

public class PlanActivity extends AppCompatActivity {

    private List<Plan> planList=new ArrayList<>();

    private List<Plan> scheduleList=new ArrayList<>();

    public static PlanAdapter planAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("计划");
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
        Log.d("todayActivity","today is "+todayYear+"年"+todayMonth+"月"+todayDay+"日");
        planList= LitePal.findAll(Plan.class);
        if (planList.size() > 0){
            scheduleList.clear();
            for(Plan plan : planList){
                if((plan.getYear().equals(todayYear) && plan.getMonth().equals(todayMonth) && Integer.parseInt(plan.getDay()) > Integer.parseInt(todayDay) ) || ( plan.getYear().equals(todayYear) && Integer.parseInt(plan.getMonth()) > Integer.parseInt(todayMonth) ) || (Integer.parseInt(plan.getYear()) > Integer.parseInt(todayYear)) ){  //只要不是今天的就会显示在计划列表当中
                    scheduleList.add(plan);
                    //排序
                }
            }
        }else scheduleList.clear();


        FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.plan_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(PlanActivity.this, ScheduleAcitvity.class);
                startActivity(intent);
                finish();
            }
        });
        Intent intent=getIntent();
        String year=intent.getStringExtra("year");
        String month=intent.getStringExtra("month");
        String day=intent.getStringExtra("day");
        if (day!=null&&month!=null&&year!=null){
            AnyLayer.dialog(PlanActivity.this)
                    .contentView(R.layout.pop_add)
                    .backgroundDimDefault()
                    .gravity(Gravity.BOTTOM)
                    .contentAnimator(new DialogLayer.AnimatorCreator() {
                        @Override
                        public Animator createInAnimator(@NonNull View content) {
                            return AnimatorHelper.createBottomInAnim(content);
                        }

                        @Override
                        public Animator createOutAnimator(@NonNull View content) {
                            return AnimatorHelper.createBottomOutAnim(content);
                        }
                    })
                    .compatSoftInput(false)
                    .onClickToDismiss(new Layer.OnClickListener() {
                        @Override
                        public void onClick(@NonNull @NotNull Layer layer, @NonNull @NotNull View view) {
                            EditText editText=layer.getView(R.id.add_edit);
                            String content=editText.getText().toString();
                            if (content!=null){
                                Plan plan=new Plan();
                                plan.setStatus(false);
                                plan.setWritePlan(editText.getText().toString());
                                plan.setDay(day);
                                plan.setMonth(month);
                                plan.setYear(year);
                                Date date=new Date(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DATE));
                                plan.setCreateTime(date);
                                plan.save();
                                scheduleList.add(plan);
                                planAdapter.notifyDataSetChanged();
                            }
                        }
                    },R.id.add_confirm)
                    .show();
        }
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