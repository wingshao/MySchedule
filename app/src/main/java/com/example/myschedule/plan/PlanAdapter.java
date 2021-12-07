package com.example.myschedule.plan;

import android.animation.Animator;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myschedule.R;
import com.example.myschedule.db.Plan;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import per.goweii.anylayer.AnyLayer;
import per.goweii.anylayer.Layer;
import per.goweii.anylayer.dialog.DialogLayer;
import per.goweii.anylayer.utils.AnimatorHelper;

import static com.example.myschedule.plan.PlanActivity.planAdapter;



public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.ViewHolder> {

    private List<Plan> mPlanList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        View planView;
        ImageView done;
        TextView contentTx;
        TextView dateTx;
        Button deleteBtn;

        public ViewHolder(View view) {
            super(view);
            planView=view;
            done=(ImageView)view.findViewById(R.id.done_icon);
            contentTx=(TextView)view.findViewById(R.id.contentTx);
            dateTx=(TextView)view.findViewById(R.id.date);
            deleteBtn=(Button)view.findViewById(R.id.delete);
        }
    }

    public PlanAdapter(List<Plan> planList){
        mPlanList=planList;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.plan_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Plan plan=mPlanList.get(position);
        holder.done.setVisibility(View.GONE);
        holder.contentTx.setText(plan.getWritePlan());
        holder.dateTx.setText(plan.getMonth()+"月"+plan.getDay()+"日");
        holder.planView.setOnClickListener(new View.OnClickListener() {
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
                                    planAdapter.notifyDataSetChanged();
                                }
                            }
                        },R.id.add_confirm)
                        .show();

            }
        });

        holder.planView.setOnLongClickListener(new View.OnLongClickListener() {
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
                view.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPlanList.size();
    }
}
