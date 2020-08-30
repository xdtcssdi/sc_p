package com.example.music163.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.example.music163.R;
import com.example.music163.fragment.star.BBSFragment;
import com.example.music163.fragment.star.BrandFragment;
import com.example.music163.fragment.star.CarFragment;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class StarListActivity extends BaseActivity {
    @BindView(R.id.mTabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.mViewPager)
    ViewPager mviewPager;
    @BindView(R.id.tb_main_title_bar)
    public TitleBar titleBar;
    private List<String> stringList = new ArrayList<>();
    private List<Fragment> fragmentList = new ArrayList<>();
    private MyAdapter myAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_star);
        super.onCreate(savedInstanceState);
        titleBar.setOnTitleBarListener(new OnTitleBarListener() {

            @Override
            public void onLeftClick(View v) {
                finish();
            }

            @Override
            public void onTitleClick(View v) {
            }

            @Override
            public void onRightClick(View v) {
            }
        });
        stringList.add("品牌");
        stringList.add("车型");
        stringList.add("帖子");
        fragmentList.add(new BrandFragment());
        fragmentList.add(new CarFragment());
        fragmentList.add(new BBSFragment());

        myAdapter = new MyAdapter(getSupportFragmentManager());

        mviewPager.setAdapter(myAdapter);

        mTabLayout.setupWithViewPager(mviewPager);

        mTabLayout.setTabsFromPagerAdapter(myAdapter);
    }

    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return stringList.get(position);
        }
    }
}
