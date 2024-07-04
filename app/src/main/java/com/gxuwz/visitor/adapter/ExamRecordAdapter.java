package com.gxuwz.visitor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gxuwz.visitor.R;
import com.gxuwz.visitor.model.bean.ExamRecord;

import java.util.List;

public class ExamRecordAdapter extends ArrayAdapter<ExamRecord> {
    public ExamRecordAdapter(@NonNull Context context, int resource, @NonNull List<ExamRecord> objects) {
        super(context, resource, objects);
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ExamRecord examRecord = getItem(position);//得到当前项的 examRecord 实例
        //为每一个子项加载设定的布局
        View view= LayoutInflater.from(getContext()).inflate(R.layout.record_item,parent,false);
        //分别获取 image view 和 textview 的实例
        TextView time =view.findViewById(R.id.time_text);
        TextView name =view.findViewById(R.id.name_text);
        TextView score =view.findViewById(R.id.score_text);
        // 设置要显示的图片和文字

        time.setText("耗时"+examRecord.getTime());
        name.setText("第"+examRecord.getId()+"次考试");
        score.setText("得分"+examRecord.getScore());
        return view;
    }

}
