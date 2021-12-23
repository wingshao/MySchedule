package com.example.myschedule.log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.animation.Animator;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.myschedule.R;
import com.example.myschedule.db.Logs;
import com.example.myschedule.db.Plan;
import com.example.myschedule.today.TodayActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;
import org.litepal.LitePal;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import per.goweii.anylayer.AnyLayer;
import per.goweii.anylayer.Layer;
import per.goweii.anylayer.dialog.DialogLayer;
import per.goweii.anylayer.utils.AnimatorHelper;

public class LogActivity extends AppCompatActivity {



    private List<Logs> dataList;

    public static LogAdapter logadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("便签");
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.log_recyclerView);
        StaggeredGridLayoutManager staggeredGridLayoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        dataList= LitePal.findAll(Logs.class);
        if (dataList.size()>0){
            logadapter=new LogAdapter(dataList);
            recyclerView.setAdapter(logadapter);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.log_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnyLayer.dialog(LogActivity.this)
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
                                    Logs logs=new Logs();
                                    logs.setContent(content);
                                    logs.save();
                                    dataList.add(logs);
                                    logadapter.notifyDataSetChanged();
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