package com.example.amicool.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ActionMenuItemView;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Toast;

import com.example.amicool.R;
import com.example.amicool.common.Common;
import com.example.amicool.fragment.BaseFragment;
import com.example.amicool.iface.AttentionListener;
import com.example.amicool.iface.CollectListener;
import com.example.amicool.model.CollectModel;
import com.example.amicool.model.FocusModel;

public class ViewArticleActivity extends AppCompatActivity {
    private int resid;//资源id
    private Boolean flagfocus = false;
    private Boolean flagcollect=false;//收藏标志
    private Context context;
    private String stype;

    private CollectModel collectmodel;//收藏model
    private SharedPreferences sp;//简单存储
    private String sessionID="";  //sessionid
    private static final String TAG = "ViewArticleActivity";
    CollectListener listener=new CollectListener() {
        @SuppressLint("RestrictedApi")
        @Override
        public void onResponse(String msg) {
            //获取菜单视图
            ActionMenuItemView item = findViewById(R.id.menucollect);

            //根据mode中response返回的字符串区分返回结果
            switch (msg)
            {
                case "2": System.out.println("----收藏成功");
                    flagcollect=true;
                    item.setTitle("取消收藏");
                    break;
                case "1":System.out.println("----收藏失败");
                    break;
                case "4":System.out.println("----取消收藏成功");
                    flagcollect=false;
                    item.setTitle("收藏");
                    break;
                case "3":System.out.println("----取消收藏失败");
                    break;
                case "5":System.out.println("----已收藏");
                    flagcollect=true;
                    item.setTitle("取消收藏");
                    break;
                case "6":System.out.println("----未收藏");
                    flagcollect=false;
                    item.setTitle("收藏");
                    break;
                default:
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        public void onFail(String msg) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }
    };

    AttentionListener articleListener = new AttentionListener() {
        @SuppressLint("RestrictedApi")
        @Override
        public void onResponse(String msg) {
            //获取菜单视图
            ActionMenuItemView item = findViewById(R.id.menufocus);

            //根据mode中response返回的字符串区分返回结果
            switch (msg)
            {
                case "2":
                    System.out.println("----关注成功");
                    flagfocus=true;
                    item.setTitle("取消关注");
                    break;
                case "1":System.out.println("----关注失败");
                    break;
                case "4":System.out.println("----取消关注成功");
                    flagfocus=false;
                    item.setTitle("关注");
                    break;
                case "3":System.out.println("----取消关注失败");
                    break;
                case "5":System.out.println("----已关注");
                    flagfocus=true;
                    item.setTitle("取消关注");
                    break;
                case "6":System.out.println("----未关注");
                    flagfocus=false;
                    item.setTitle("关注");
                    break;
                default:
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        public void onFail(String msg) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }
    };


    private WebView webView;
    private FocusModel focusModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_article);

        context = ViewArticleActivity.this;

        webView = findViewById(R.id.webview);

        int type = getIntent().getIntExtra("type",1);
        resid  = getIntent().getIntExtra("resid",1);
        switch (type){
            case 1:
                stype="article";
                webView.loadUrl(Common.ARTICLEURL+resid);
                break;
            case 2:
                stype="tcase";
                webView.loadUrl(Common.TCASEURL+resid);
                break;
            case 3:
                stype="project";
                webView.loadUrl(Common.PROJECTURL+resid);
                break;
            default:
                break;
        }

    }

    private void readSP() {
        sp=getSharedPreferences(BaseFragment.FILE, MODE_PRIVATE);
        sessionID=sp.getString(BaseFragment.KEY_SESSION_ID,"");
    }
    private int userid;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        readSP();//读取sessionid
        getMenuInflater().inflate(R.menu.menucollect, menu);//加载菜单布局
        collectmodel=new CollectModel();//实例化对象
        focusModel=new FocusModel();
        resid  = getIntent().getIntExtra("resid",1);//获取传递的资源id
        userid  = getIntent().getIntExtra("userid",1);
        collectmodel.exist(stype,resid,sessionID,listener);//判断是否收藏
        focusModel.exists("userfocus",userid,sessionID,articleListener);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menucollect:
                if(flagcollect)//如果已收藏，则调用取消收藏
                {
                    System.out.println("----准备取消收藏");
                    collectmodel.uncollect(stype,resid,sessionID,listener);
                }
                else//如果未收藏，则调用收藏
                {
                    System.out.println("----准备收藏");
                    collectmodel.collect(stype,resid,sessionID,listener);
                }
                break;
            case R.id.menufocus:
                if(flagfocus)//如果已关注，则调用取消关注
                {
                    System.out.println("----准备取消关注");
                    focusModel.unfocus("userfocus",userid,sessionID,articleListener);
                }
                else//如果未关注，则调用关注
                {
                    System.out.println("----准备关注");
                    focusModel.focus("userfocus",userid,sessionID,articleListener);
                }
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

}

