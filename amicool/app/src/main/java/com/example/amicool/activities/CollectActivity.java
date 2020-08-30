package com.example.amicool.activities;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.example.amicool.R;
import com.example.amicool.fragment.FragmentCollect1;
import com.example.amicool.fragment.FragmentCollect3;
import com.example.amicool.fragment.FragmentCollect2;
import com.example.amicool.fragment.FragmentCollect4;
import com.example.amicool.fragment.FragmentCollect5;
import com.example.amicool.fragment.PagerSlidingTabStrip;

public class CollectActivity extends AppCompatActivity {
    private DisplayMetrics dm;
    private PagerSlidingTabStrip pagersliding;

    private ViewPager viewpager;
    private FragmentCollect1 fragment1;
    private FragmentCollect2 fragment2;
    private FragmentCollect3 fragment3;
    private FragmentCollect4 fragment4;
    private FragmentCollect5 fragment5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        dm = getResources().getDisplayMetrics();//获取屏幕密度
        viewpager = findViewById(R.id.viewpager);
        viewpager.setAdapter(new CollectActivity.MyPagerAdapter(getSupportFragmentManager()));
        pagersliding = findViewById(R.id.pagerslidingtabstrip);
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
        private String[] titles = {"视频", "课件","文章","案例","项目"};//显示在二级导航上的标题文字
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
                    if (fragment1 == null) {
                        fragment1 = new FragmentCollect1();
                    }
                    return fragment1;
                case 1:
                    if (fragment2 == null) {
                        fragment2 = new FragmentCollect2();
                    }
                    return fragment2;
                case 2:
                    if (fragment3 == null) {
                        fragment3 = new FragmentCollect3();
                    }
                    return fragment3;
                case 3:
                    if (fragment4 == null) {
                        fragment4 = new FragmentCollect4();
                    }
                    return fragment4;
                case 4:
                    if (fragment5 == null) {
                        fragment5 = new FragmentCollect5();
                    }
                    return fragment5;
                default:
                    return null;
            }
        }
    }
}
