package com.gxuwz.visitor.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import com.gxuwz.visitor.activity.QuestionActivity;
import com.gxuwz.visitor.R;
import com.gxuwz.visitor.adapter.ImagePagerAdapter;

import java.util.Arrays;
import java.util.List;

/**
 * 空白模板Fragment
 */
public class Test1Fragment extends Fragment implements View.OnClickListener{
    // 声明drawable资源数组
    private LinearLayout vip_layout;
    private List<Integer> imgList = Arrays.asList(R.drawable.img_1, R.drawable.img_2, R.drawable.img_3);
    private final long DELAY_MS = 2000; // 自动换页的延迟时间
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            int currentItem = viewPager2.getCurrentItem();
            if (currentItem == imgList.size() - 1) {
                viewPager2.setCurrentItem(0);
            } else {
                viewPager2.setCurrentItem(currentItem + 1);
            }
            handler.postDelayed(this, DELAY_MS);
        }
    };

    private Intent intent;
    // 声明控件
    private LinearLayout order_layout,rand_layout;
    private ViewPager2 viewPager2;



    public Test1Fragment() {
        super();
    }


    public static Test1Fragment newInstance(String param1, String param2) {
        Test1Fragment fragment = new Test1Fragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager2=view.findViewById(R.id.viewPager2);
        viewPager2.setAdapter(new ImagePagerAdapter(this.getContext(),imgList));

        vip_layout=view.findViewById(R.id.vip_layout);

        order_layout=view.findViewById(R.id.order_layout);
        order_layout.setOnClickListener(this);




        // 启动自动轮播
        handler.postDelayed(runnable, DELAY_MS);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_test1, container, false);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.order_layout:
                intent = new Intent(getActivity(), QuestionActivity.class);
                intent.putExtra("subject", "1");
                startActivity(intent);
                break;
        }
    }


}