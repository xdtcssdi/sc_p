package com.example.amicool.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.amicool.R;
import com.example.amicool.adapter.UserAdapter;
import com.example.amicool.bean.FocusResultBean;
import com.example.amicool.bean.UserBean;
import com.example.amicool.fragment.BaseFragment;
import com.example.amicool.iface.AttentionListListener;
import com.example.amicool.model.AttentionListModel;

import java.util.ArrayList;
import java.util.List;

public class AttentActivity extends AppCompatActivity {

    private RecyclerView recyclerview;
    private int page = 1;
    private UserAdapter adapter;
    private List<FocusResultBean<UserBean>> list = new ArrayList<>();
    private Context context;
    private String sessionID;
    private int lastVisibleItemPosition;//最后一条可见条目的位置
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment1);
        readSP();
        context = this;
        recyclerview = findViewById(R.id.recyclerview);
        layoutManager = new LinearLayoutManager(context);
        recyclerview.setLayoutManager(layoutManager);
        adapter = new UserAdapter(context);
        recyclerview.setAdapter(adapter);
        recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItemPosition + 1 == list.size()) {
                    page += 1;
                    //再次实例化ArticleModel，调用方法获取网络数据，请求新一页数据
                    AttentionListModel attentionListModel = new AttentionListModel();
                    attentionListModel.getResultList("user", page, sessionID, attentListener);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();//滚动结束后将赋值为可见条目中最后一条位置
            }
        });
        AttentionListModel attentionListModel = new AttentionListModel();
        attentionListModel.getResultList("user", 1, sessionID, attentListener);
    }

    private static final String TAG = "AttentActivity";
    AttentionListListener<FocusResultBean<UserBean>> attentListener = new AttentionListListener<FocusResultBean<UserBean>>() {
        @Override
        public void onResponse(List<FocusResultBean<UserBean>> beanlist) {

            if (beanlist == null) {
                return;
            } else {
                list.removeAll(beanlist);
                list.addAll(beanlist);
            }
            adapter.setList(list);
        }

        @Override
        public void onFail(String msg) {
            Toast.makeText(context, "获取关注列表失败", Toast.LENGTH_SHORT).show();
        }
    };

    private void readSP() {
        sessionID = getSharedPreferences(BaseFragment.FILE, MODE_PRIVATE)
                .getString(BaseFragment.KEY_SESSION_ID, "");
    }

}
