package com.example.amicool.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.example.amicool.R;
import com.example.amicool.activities.AttentActivity;
import com.example.amicool.activities.CollectActivity;
import com.example.amicool.activities.LoginActivity;

public class FragmentMy extends BaseFragment implements View.OnClickListener{

    private Button attention,collect,logout;
    public FragmentMy() {
    }
    @Nullable
    @Override //生命周期方法，创建View
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragmentmy, container, false);
    }
    @Override//生命周期方法，View创建完成
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        attention = view.findViewById(R.id.attention);
        collect = view.findViewById(R.id.collect);
        logout = view.findViewById(R.id.logout);
        attention.setOnClickListener(this);
        collect.setOnClickListener(this);
        logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.attention:
                startActivity(new Intent(getActivity(), AttentActivity.class));
                break;
            case R.id.collect:
                startActivity(new Intent(getActivity(), CollectActivity.class));
                break;
            case R.id.logout:

                getActivity().finish();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;
            default:
                break;
        }
    }
}
