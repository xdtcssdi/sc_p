package com.weilai.keke.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.weilai.keke.R;
import com.weilai.keke.base.BaseActivity;
import com.weilai.keke.entity.UserInfo;
import com.weilai.keke.fragment.QuestionFragment;
import com.weilai.keke.fragment.ScheduleFragment;
import com.weilai.keke.fragment.SignFragment;
import com.weilai.keke.fragment.UserDataFragment;
import com.weilai.keke.util.Common;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.message.TextMessage;

public class MainActivity extends BaseActivity {

    private ViewPager mViewPager;
    private List<Fragment> mFragment;
    private Context context;
    private BottomNavigationBar mBottomNavigationBar;
    private static final String TAG = "MainActivity";
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addCategory(Intent.CATEGORY_HOME);
        startActivity(i);
    }


    @Override
    protected void onResume() {
        super.onResume();
        HooliganActivity.killHooligan();
    }

    private UserInfo userInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getBaseContext();
        try{
            userInfo = (UserInfo) getIntent().getSerializableExtra("userinfo");
        }catch (ClassCastException e){
            new MaterialDialog.Builder(context)
                    .title("警告")
                    .content("获取数据错误").autoDismiss(false)
                    .negativeText("返回").onNegative((dialog12, which12) -> {
                dialog12.dismiss();
                finish();
            }).show();
        }

        RongIM.connect(Common.user.getToken(), new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                Log.e(TAG, "--onTokenIncorrect");
            }
            @Override
            public void onSuccess(String userid) {
                Log.e(TAG, "--onSuccess--" + userid);
                Toast.makeText(MainActivity.this, "登录成功,用户：" + userid, Toast.LENGTH_SHORT).show();

                RongIMClient.setOnReceiveMessageListener((message, i) -> {

                    //0 更新问题  1更新白名单
                    String msg = ((TextMessage)message.getContent()).getContent();
                    Log.d(TAG, "onSuccess: " + msg);

                    if("updateWhiteApp".equals(msg)){
                        Log.d(TAG, "onSuccess: updateWhiteApp");
                        sendBroadcast(new Intent(Common.WhiteAppListReceiver)
                                .putExtra("content",msg));
                    }else if("updateQuestion".equals(msg)){
                        sendBroadcast(new Intent(Common.UpdateQuestions));
                    }

                    return true;
                });
            }
            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.e(TAG, "--onError");
            }
        });

        mFragment = new ArrayList<>();

        mViewPager = findViewById(R.id.viewpager_id);

        mBottomNavigationBar = findViewById(R.id.bottom_navigation_bar);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mBottomNavigationBar.selectTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mBottomNavigationBar.setMode(BottomNavigationBar.MODE_SHIFTING)
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);

        mBottomNavigationBar //值得一提，模式跟背景的设置都要在添加tab前面，不然不会有效果。
                .setActiveColor(R.color.yellow);//选中颜色 图标和文字


        //课程表
        ScheduleFragment scheduleFragment = new ScheduleFragment();

        //个人信息表
        UserDataFragment userDataFragment = new UserDataFragment();

        //问题表
        QuestionFragment questionFragment = new QuestionFragment();


        BottomNavigationBar bnb = mBottomNavigationBar
                .addItem(new BottomNavigationItem(R.mipmap.icon_circle_selected, "课程表")
                        .setInactiveIcon(ContextCompat.getDrawable(MainActivity.this, R.mipmap.icon_circle)));

        //添加碎片
        mFragment.add(scheduleFragment);

        if (!Common.user.isIs_staff()) {
            SignFragment signFragment = new SignFragment();
            mFragment.add(signFragment);
            bnb.addItem(new BottomNavigationItem(R.mipmap.icon_find_selected, "签到")
                    .setInactiveIcon(ContextCompat.getDrawable(MainActivity.this, R.mipmap.icon_find))).addItem(new BottomNavigationItem(R.mipmap.icon_message_selected, "课堂问题")
                    .setInactiveIcon(ContextCompat.getDrawable(MainActivity.this, R.mipmap.icon_message)))
                    .addItem(new BottomNavigationItem(R.mipmap.icon_me_selected, "个人信息").setInactiveIcon(ContextCompat.getDrawable(MainActivity.this, R.mipmap.icon_me)))
                    .setFirstSelectedPosition(0)//设置默认选择的按钮
                    .initialise();//所有的设置需在调用该方法前完成
        }else{
            bnb.addItem(new BottomNavigationItem(R.mipmap.icon_message_selected, "课堂问题")
                    .setInactiveIcon(ContextCompat.getDrawable(MainActivity.this, R.mipmap.icon_message)))
                    .addItem(new BottomNavigationItem(R.mipmap.icon_me_selected, "个人信息").setInactiveIcon(ContextCompat.getDrawable(MainActivity.this, R.mipmap.icon_me)))
                    .setFirstSelectedPosition(0)//设置默认选择的按钮
                    .initialise();//所有的设置需在调用该方法前完成
        }

        mFragment.add(questionFragment);
        mFragment.add(userDataFragment);

        FragmentManager fragmentManager = getSupportFragmentManager();

        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                return mFragment.get(position);
            }

            @Override
            public int getCount() {
                return mFragment.size();
            }
        });
        mViewPager.setOffscreenPageLimit(3);

        mBottomNavigationBar
                .setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(int position) {
                        mViewPager.setCurrentItem(position);
                    }


                    @Override
                    public void onTabUnselected(int position) {

                    }

                    @Override
                    public void onTabReselected(int position) {
                        //重选的按钮执行（未测试）
                    }
                });

    }

}
