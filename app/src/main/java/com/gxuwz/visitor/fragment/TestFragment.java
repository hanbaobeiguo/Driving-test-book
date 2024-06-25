package com.gxuwz.visitor.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.gxuwz.visitor.QuestionActivity;
import com.gxuwz.visitor.R;
import com.gxuwz.visitor.adapter.ImagePagerAdapter;

/**
 * 空白模板Fragment
 */
public class TestFragment extends Fragment implements View.OnClickListener{
    // 声明向Fragment传递的参数对应的Key常量

    private LinearLayout order_layout;
    private ViewPager2 viewPager2;
    private ImagePagerAdapter imgAdapter;

    public TestFragment() {
        super();
    }

    /**
     * 创建带给定Bundle参数的Fragment实例的类方法
     */
    public static TestFragment newInstance(String param1, String param2) {
        TestFragment fragment = new TestFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager2=view.findViewById(R.id.viewPager2);
//        viewPager2.setAdapter(new ImagePagerAdapter(this,imgList));
        order_layout=view.findViewById(R.id.order_layout);
        order_layout.setOnClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_test, container, false);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.order_layout:
                Intent intent = new Intent(getActivity(), QuestionActivity.class);
                intent.putExtra("subject", 1);
                intent.putExtra("model", "c1");
                intent.putExtra("testType", "order");
                startActivity(intent);
                break;
        }
    }
}