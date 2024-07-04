package com.gxuwz.visitor.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gxuwz.visitor.MyApplication;
import com.gxuwz.visitor.R;
import com.gxuwz.visitor.activity.ErrorQuestionActivity;
import com.gxuwz.visitor.activity.ErrorQuestionFrontActivity;
import com.gxuwz.visitor.activity.ExamRecordActivity;
import com.gxuwz.visitor.activity.ForgetPwdActivity;
import com.gxuwz.visitor.activity.LoginActivity;
import com.gxuwz.visitor.model.bean.User;

/**
 * 空白模板Fragment
 */
public class MeFragment extends Fragment implements View.OnClickListener{

    private TextView usernameTextView;
    private LinearLayout pwdModify_layout,examRecord_layout,logout_layout,errorQuestion_layout;
    private MyApplication myApplication;
    private String userName;

    public MeFragment() {
        super();
    }

    /**
     * 创建带给定Bundle参数的Fragment实例的类方法
     */
    public static MeFragment newInstance() {
        MeFragment fragment = new MeFragment();
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);

        // 获取 MyApplication 实例
        myApplication = (MyApplication) getActivity().getApplication();

        // 获取当前用户
        User currentUser = myApplication.getCurrentUser();

        if (currentUser != null) {
            // 使用 currentUser 对象
            userName = currentUser.getName();
            usernameTextView.setText(userName);
        } else {
            // 处理用户未登录的情况,点击前往登录页面
            usernameTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    private void initView(View view) {
        usernameTextView = view.findViewById(R.id.user_name);
        errorQuestion_layout = view.findViewById(R.id.errorQuestion_layout);
        pwdModify_layout = view.findViewById(R.id.pwdModify_layout);
        examRecord_layout = view.findViewById(R.id.examRecord_layout);
        logout_layout = view.findViewById(R.id.logout_layout);

        errorQuestion_layout.setOnClickListener(this);
        pwdModify_layout.setOnClickListener(this);
        examRecord_layout.setOnClickListener(this);
        logout_layout.setOnClickListener(this);
    }

    private void pleaseLogin() {
        Toast.makeText(getContext(), "请先登录", Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (myApplication.getCurrentUser() == null) {
            pleaseLogin();
        }else {
            switch (id) {
                case R.id.pwdModify_layout:
                    Intent intent = new Intent(getActivity(), ForgetPwdActivity.class);
                    startActivity(intent);
                    break;
                case R.id.examRecord_layout:
                    Intent intent1 = new Intent(getActivity(), ExamRecordActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.logout_layout:
                    myApplication.logout();
                    Toast.makeText(getContext(), "已退出登录", Toast.LENGTH_SHORT).show();
                    usernameTextView.setText("请登陆账号");
                    Intent intent2 = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent2);
                    break;
                case R.id.errorQuestion_layout:
                    Intent intent3 = new Intent(getActivity(), ErrorQuestionFrontActivity.class);
                    startActivity(intent3);
                    break;
                default:
                    break;
            }
        }
    }
}
