package com.example.myschedule.log;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myschedule.R;
import com.example.myschedule.db.Logs;
import com.example.myschedule.plan.PlanAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public class LogAdapter extends RecyclerView.Adapter<LogAdapter.ViewHolder> {

    private List<Logs> mLogsList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        View logView;
        TextView dateTx;
        TextView contentTx;
        Button deleteBtn;

        public ViewHolder(View view) {
            super(view);
            logView=view;
            contentTx=(TextView)view.findViewById(R.id.log_content);
            deleteBtn=(Button)view.findViewById(R.id.delete);
        }
    }

    public LogAdapter(List<Logs> logsList) {
        mLogsList=logsList;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.log_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Logs logs=mLogsList.get(position);
        holder.contentTx.setText(logs.getContent());
        holder.logView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                holder.deleteBtn.setVisibility(View.VISIBLE);
                return false;
            }
        });
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLogsList.remove(position);
                logs.delete();
                LogActivity.logadapter.notifyDataSetChanged();
            }
        });
        try {
            holder.logView.setBackgroundColor(Color.parseColor(randomColor()));
        }catch (Exception e){
            e.printStackTrace();
            Log.d("LogAdapter",randomColor());
        }
    }

    @Override
    public int getItemCount() {
        return mLogsList.size();
    }

    private String randomColor(){
        String R;
        String G;
        String B;
        Random random=new Random();
        R=Integer.toHexString(random.nextInt(256)).toUpperCase();
        G=Integer.toHexString(random.nextInt(256)).toUpperCase();
        B=Integer.toHexString(random.nextInt(256)).toUpperCase();
        R=R.length()==1?"0"+R:R;
        G=G.length()==1?"0"+G:G;
        B=B.length()==1?"0"+B:B;
        return "#"+R+G+B;
    }
}
