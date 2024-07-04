package com.gxuwz.visitor.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.gxuwz.visitor.R;

/**
 * 空白模板Fragment
 */
public class BlankFragment extends Fragment {
    private static final int NUM_PAGES = 2;

    private ViewPager2 viewPager1;
    private RadioGroup rg_bottom;


    public BlankFragment() {
        super();
    }
        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewPager1 = view.findViewById(R.id.viewPager1);
        rg_bottom = view.findViewById(R.id.rg_bottom);

        viewPager1.setAdapter(new BlankFragment.PagerAdapter(this));

        //导航栏点击事件
        rg_bottom.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.first_question:
                        viewPager1.setCurrentItem(0);
                        break;
                    case R.id.fourth_question:
                        viewPager1.setCurrentItem(1);
                        break;
                }
            }
        });

        //页面切换监听
        viewPager1.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position){
                    case 0:
                        rg_bottom.check(R.id.first_question);
                        break;
                    case 1:
                        rg_bottom.check(R.id.fourth_question);
                        break;
                }
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_blank, container, false);
        return view;
    }

    class PagerAdapter extends FragmentStateAdapter {
        public PagerAdapter(BlankFragment activity) {
            super(activity);
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }

        @Override
        public Fragment createFragment(int position) {
            Fragment fragment=new Test1Fragment();
            switch (position){
                case 0:
                    fragment=new Test1Fragment();
                    break;
                case 1:
                    fragment=new Test4Fragment();
                    break;

                default:
                    fragment=new BlankFragment();

            }
            return fragment;
        }
    }
}