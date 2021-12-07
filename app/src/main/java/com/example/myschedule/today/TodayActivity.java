package com.example.myschedule.today;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.myschedule.db.Plan;
import com.example.myschedule.R;
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

public class TodayActivity extends AppCompatActivity {

    private List<Plan> planList=new ArrayList<>();

    private List<Plan> todayList=new ArrayList<>();

    public static TodayAdapter todayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("今日");
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        RecyclerView recyclerView=(RecyclerView) findViewById(R.id.today_recyclerview);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        todayAdapter=new TodayAdapter(todayList);
        recyclerView.setAdapter(todayAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Calendar calendar=Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));  //设置为中国区的时间
        String year=String.valueOf(calendar.get(Calendar.YEAR));
        String month=String.valueOf(calendar.get(Calendar.MONTH)+1);
        String day=String.valueOf(calendar.get(Calendar.DATE));
        Log.d("todayActivity","today is "+year+"年"+month+"月"+day+"日");
        planList= LitePal.findAll(Plan.class);
        if (planList.size()>0){
            todayList.clear();
            for (Plan plan:planList){
                if (plan.getDay().equals(day)&&plan.getMonth().equals(month)&&plan.getYear().equals(year)&&!plan.isStatus()){
                    todayList.add(plan);
                }
            }
        }else todayList.clear();

        FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.today_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnyLayer.dialog(TodayActivity.this)
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
                                    todayList.add(plan);
                                    todayAdapter.notifyDataSetChanged();
                                }
                            }
                        },R.id.add_confirm)
                        .show();
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