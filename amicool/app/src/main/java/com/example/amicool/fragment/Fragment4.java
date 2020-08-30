package com.example.amicool.fragment;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.amicool.R;
import com.example.amicool.adapter.ArticleAdapter;
import com.example.amicool.adapter.TcaseAdapter;
import com.example.amicool.bean.ArticleBean;
import com.example.amicool.bean.TcaseBean;
import com.example.amicool.iface.ArticleListener;
import com.example.amicool.iface.TcaseListener;
import com.example.amicool.model.ArticleModel;
import com.example.amicool.model.TcaseModel;

import java.util.List;

public class Fragment4 extends BaseFragment {
    //获取当前屏幕的密度
    private DisplayMetrics dm;
    private PagerSlidingTabStrip pagersliding;
    private ViewPager viewpager;
    private Fragment41 fragment41 = null;
    private Fragment42 fragment42 = null;

    public Fragment4() {
    }
    @Nullable
    @Override //生命周期方法，创建View
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment4, container, false);
    }
    @Override//生命周期方法，View创建完成
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        System.out.println("--f3--" + getSessionId());
        dm = getResources().getDisplayMetrics();//获取屏幕密度
        viewpager = view.findViewById(R.id.viewPager);
        viewpager.setAdapter(new Fragment4.MyPagerAdapter(getChildFragmentManager()));
        pagersliding = view.findViewById(R.id.pagerslidingtabstrip);
        pagersliding.setViewPager(viewpager);
        setpagerstyle();//设置PagerSlidingTabStrip显示效果
    }

    private void setpagerstyle() {
        pagersliding.setShouldExpand(true); // 设置Tab是自动填充满屏幕的
        pagersliding.setDividerColor(Color.TRANSPARENT); // 设置Tab的分割线是透明的
        // 设置Tab底部线的高度
        pagersliding.setUnderlineHeight((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 1, dm));
        // 设置Tab Indicator的高度
        pagersliding.setIndicatorHeight((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 4, dm));
        // 设置Tab标题文字的大小
        pagersliding.setTextSize((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 16, dm));
        pagersliding.setIndicatorColor(Color.parseColor("#45c01a"));// 设置Tab Indicator的颜色
        // 设置选中Tab文字的颜色 (这是自定义的一个方法)
        pagersliding.setSelectedTextColor(Color.parseColor("#45c01a"));
        pagersliding.setTabBackground(0); // 取消点击Tab时的背景色
    }

    //自定义ViewPagerAdapter子类
    private class MyPagerAdapter extends FragmentPagerAdapter {
        private String[] titles = {"案例", "项目"};//显示在二级导航上的标题文字
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];//确定当页导航上文字
        }
        @Override
        public int getCount() {
            return titles.length;//二级导航个数
        }
        @Override
        public Fragment getItem(int position) { //根据位置返回体某个导航上对应的Fragm具ent
            switch (position) {
                case 0:
                    if (fragment41 == null) {
                        fragment41 = new Fragment41();
                    }
                    return fragment41;
                case 1:
                    if (fragment42 == null) {
                        fragment42 = new Fragment42();
                    }
                    return fragment42;
                default:
                    return null;
            }
        }
    }
}
