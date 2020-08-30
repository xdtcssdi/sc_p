package com.example.music163.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.music163.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class OneFragment extends Fragment {
    @BindView(R.id.mTabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.mViewPager)
    ViewPager mviewPager;
    private List<String> stringList = new ArrayList<>();
    private List<Fragment> fragmentList = new ArrayList<>();
    private MyAdapter myAdapter;

    private Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        stringList.add("品牌分类");
        stringList.add("车型分类");
        fragmentList.add(new BrandFragment());
        fragmentList.add(new CarFragment());
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_one, container, false);
        ButterKnife.bind(this, rootView);
        if (getActivity() != null) {
            mContext = getActivity();
        }
        initView(rootView);

        myAdapter = new MyAdapter(getChildFragmentManager());

        mviewPager.setAdapter(myAdapter);

        mTabLayout.setupWithViewPager(mviewPager);

        mTabLayout.setTabsFromPagerAdapter(myAdapter);
        return rootView;
    }

    private void initView(View rootView) {
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
