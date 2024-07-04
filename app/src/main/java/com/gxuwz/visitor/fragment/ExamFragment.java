package com.gxuwz.visitor.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gxuwz.visitor.MyApplication;
import com.gxuwz.visitor.R;
import com.gxuwz.visitor.activity.ExamActivity;
import com.gxuwz.visitor.activity.ExamRecordActivity;
import com.gxuwz.visitor.activity.QuestionActivity;
import com.gxuwz.visitor.adapter.ExamRecordAdapter;
import com.gxuwz.visitor.model.bean.ExamRecord;
import com.gxuwz.visitor.util.AppDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * 空白模板Fragment
 */
public class ExamFragment extends Fragment implements View.OnClickListener{
    private AppDatabase db;
    private LinearLayout exam_begin_layout,check_record_layout;
    private Intent intent;

    private ListView RecordeListView;
    private List<ExamRecord> examRecordList;
    public ExamFragment() {
        super();
    }

    /**
     * 创建带给定Bundle参数的Fragment实例的类方法
     */
    public static ExamFragment newInstance(String param1, String param2) {
        ExamFragment fragment = new ExamFragment();

        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        exam_begin_layout = view.findViewById(R.id.exam_begin_layout);
        check_record_layout = view.findViewById(R.id.check_record_layout);
        exam_begin_layout.setOnClickListener(this);
        check_record_layout.setOnClickListener(this);
        db = MyApplication.database;
        database();


    }

    private void database() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_exam, container, false);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.exam_begin_layout:
                intent = new Intent(getActivity(), ExamActivity.class);
                intent.putExtra("subject", 1);
                intent.putExtra("model", "c1");
                intent.putExtra("testType", "rand");
                startActivity(intent);
                Toast.makeText(getContext(), "考试开始", Toast.LENGTH_SHORT).show();
                break;
            case R.id.check_record_layout:
                intent = new Intent(getActivity(), ExamRecordActivity.class);
                startActivity(intent);
                break;
        }
    }
}