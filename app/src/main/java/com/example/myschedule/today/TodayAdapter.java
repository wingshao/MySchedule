package com.example.myschedule.today;

import android.animation.Animator;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myschedule.db.Plan;
import com.example.myschedule.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import per.goweii.anylayer.AnyLayer;
import per.goweii.anylayer.Layer;
import per.goweii.anylayer.dialog.DialogLayer;
import per.goweii.anylayer.utils.AnimatorHelper;

import static com.example.myschedule.today.TodayActivity.todayAdapter;

public class TodayAdapter extends RecyclerView.Adapter<TodayAdapter.ViewHolder> {

    private List<Plan> mPlanList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        View todayView;
        ImageView done;
        TextView contentTx;
        TextView dateTx;
        Button deleteBtn;

        public ViewHolder(View view) {
            super(view);
            todayView=view;
            done=(ImageView)view.findViewById(R.id.done_icon);
            contentTx=(TextView)view.findViewById(R.id.contentTx);
            dateTx=(TextView)view.findViewById(R.id.date);
            deleteBtn=(Button)view.findViewById(R.id.delete);
        }
    }

    public TodayAdapter(List<Plan> planList){
        mPlanList=planList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.plan_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(TodayAdapter.ViewHolder holder, int position) {
        final Handler handler = new Handler() {  //异步消息处理机制来在非主线程中来修改UI

            public void handleMessage(Message msg){
                switch (msg.what){
                    case 1:
                        mPlanList.remove(position);  //将该Plan从列表中移除（此时数据库中的没有移除）
                        todayAdapter.notifyDataSetChanged();  //更新适配器以达到更新列表的效果
                        break;
                    default:
                        break;
                }
            }
        };
        Plan plan=mPlanList.get(position);
        holder.contentTx.setText(plan.getWritePlan());
        holder.done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                plan.setStatus(true);
                plan.save();
                TimerTask task1 = new TimerTask() {  //设置定时任务
                    public void run() {
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);  //发送异步Message
                    }
                };
                Timer timer = new Timer();
                timer.schedule(task1, 800);  //设置延时的时间，单位是毫秒
                Animation animation = AnimationUtils.loadAnimation(view.getContext(),R.anim.today_anim);  //设置动画
                holder.todayView.startAnimation(animation);
            }
        });
        holder.dateTx.setVisibility(View.GONE);
        holder.todayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnyLayer.dialog(view.getContext())
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
                        .onClickToDismiss(new Layer.OnClickListener() {
                            @Override
                            public void onClick(@NonNull @NotNull Layer layer, @NonNull @NotNull View view) {
                                EditText editText=(EditText)layer.getView(R.id.add_edit);
                                String content=editText.getText().toString();
                                if (content!=null){
                                    plan.setWritePlan(content);
                                    plan.save();
                                    mPlanList.set(position,plan);
                                    todayAdapter.notifyDataSetChanged();
                                }
                            }
                        },R.id.add_confirm)
                        .show();

            }
        });

        holder.todayView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                holder.deleteBtn.setVisibility(View.VISIBLE);
                return true;
            }
        });
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                plan.delete();
                mPlanList.remove(position);
                todayAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPlanList.size();
    }
}
