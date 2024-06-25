package com.gxuwz.visitor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.widget.RadioGroup;

import com.gxuwz.visitor.adapter.ImagePagerAdapter;
import com.gxuwz.visitor.fragment.TestFragment;
import com.gxuwz.visitor.fragment.BlankFragment;
import com.gxuwz.visitor.fragment.HomeFragment;
import com.gxuwz.visitor.fragment.MeFragment;
import com.gxuwz.visitor.fragment.QuestionBankFragment;
import com.gxuwz.visitor.fragment.ExamFragment;

/**
 * 主页
 */
public class MainActivity extends AppCompatActivity {
   private static final int NUM_PAGES = 5;
   private ViewPager2 viewPager1;

   private RadioGroup rg_bottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initAdapter();
        method();
    }




    //初始化view
    private void initView() {
        viewPager1=findViewById(R.id.viewPager1);

        rg_bottom=findViewById(R.id.rg_bottom);
    }




    //初始化adapter
    private void initAdapter() {
        viewPager1.setAdapter(new PagerAdapter(this));

    }



    //方法事件
    private void method() {
        //底部导航栏点击事件
        rg_bottom.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rb_home:
                        viewPager1.setCurrentItem(0);
                        break;
                    case R.id.rb_exam:
                        viewPager1.setCurrentItem(1);
                        break;
                    case R.id.rb_test:
                        viewPager1.setCurrentItem(2);
                        break;
                    case R.id.rb_question_bank:
                        viewPager1.setCurrentItem(3);
                        break;
                    case R.id.rb_me:
                        viewPager1.setCurrentItem(4);
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
                        rg_bottom.check(R.id.rb_home);
                        break;
                    case 1:
                        rg_bottom.check(R.id.rb_exam);
                        break;
                    case 2:
                        rg_bottom.check(R.id.rb_test);
                        break;
                    case 3:
                        rg_bottom.check(R.id.rb_question_bank);
                        break;
                    case 4:
                        rg_bottom.check(R.id.rb_me);
                        break;
                }
            }
        });
    }






    class PagerAdapter extends FragmentStateAdapter {
        public PagerAdapter(FragmentActivity activity) {
            super(activity);
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }

        @Override
        public Fragment createFragment(int position) {
            Fragment fragment=null;
            switch (position){
                case 0:
                    fragment=new HomeFragment();
                    break;
                case 1:
                    fragment=new ExamFragment();
                    break;
                case 2:
                    fragment=new TestFragment();
                    break;
                case 3:
                    fragment=new QuestionBankFragment();
                    break;
                case 4:
                    fragment=new MeFragment();
                    break;
                default:
                    fragment=new BlankFragment();

            }
            return fragment;
        }
    }
    
    
    
}