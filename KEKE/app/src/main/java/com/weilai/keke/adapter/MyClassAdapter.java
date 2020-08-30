package com.weilai.keke.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.weilai.keke.R;
import com.weilai.keke.entity.TeachingClassEntity;

import java.util.HashMap;
import java.util.Map;

public class MyClassAdapter extends RecyclerView.Adapter<MyClassAdapter.ViewHolder> {
    private TeachingClassEntity teachingClassEntity;
    private Map<String, String> map = new HashMap<>();

    public MyClassAdapter(TeachingClassEntity teachingClassEntity) {
        this.teachingClassEntity = teachingClassEntity;
        map.put("Monday","星期一");
        map.put("Tuesday","星期二");
        map.put("Wednesday","星期三");
        map.put("Thursday","星期四");
        map.put("Friday","星期五");
        map.put("Saturday","星期六");
        map.put("Sunday","星期日");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_class_recy_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TeachingClassEntity.DataBean dataBean = teachingClassEntity.getData().get(position);
        holder.class_name.setText("课程名："+ dataBean.getFields().getClassName());
        holder.teacher_name.setText("老师："+dataBean.getFields().getTeacherId());
        Log.d(TAG, "onBindViewHolder: " + dataBean.getFields().getWeek());
        holder.time.setText("上课时间："+map.get(dataBean.getFields().getWeek())+"  "+dataBean.getFields().getClassStartTime()+"-"+dataBean.getFields().getClassEndTime());
        holder.sign_time.setText("签到时间："+dataBean.getFields().getClassSignTime()+"秒");
    }



    @Override
    public int getItemCount() {
        return teachingClassEntity.getData().size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView class_name,teacher_name,time,sign_time;
        ViewHolder(View view) {
            super(view);
            class_name = view.findViewById(R.id.class_name);
            teacher_name = view.findViewById(R.id.teacher_name);
            time = view.findViewById(R.id.time);
            sign_time = view.findViewById(R.id.sign_time);
        }
    }

    private static final String TAG = "QuestionAdapter";
}
