package com.example.amicool.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.ActionMenuItemView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amicool.R;
import com.example.amicool.callback.HttpCallBack;
import com.example.amicool.fragment.BaseFragment;
import com.example.amicool.iface.AttentionListener;
import com.example.amicool.iface.CollectListener;
import com.example.amicool.model.CollectModel;
import com.example.amicool.model.FocusModel;
import com.example.amicool.service.DownloadService;
import com.joanzapata.pdfview.PDFView;
import com.joanzapata.pdfview.listener.OnPageChangeListener;

import java.io.File;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ViewTwareActivity extends AppCompatActivity implements OnPageChangeListener {

    private String name;
    private String attach;
    private PDFView pdfView;
    private String sessionID;  //sessionid
    private TextView tvinfo,tvpage;
    private Context context;
    private SharedPreferences sp;//简单存储
    private CollectModel collectmodel;//收藏model
    private String BASEURL ="http://amicool.neusoft.edu.cn/";
    private Boolean flagfocus = false;
    private Boolean flagcollect=false;//收藏标志
    private FocusModel focusModel;
    private int resid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tware);
        System.out.println("----查看课件详情");
        context = this;
        init();
        resid = getIntent().getIntExtra("resid",1);
        attach=getIntent().getStringExtra("pdfattach");
        name=getIntent().getStringExtra("name");

        System.out.println("----pdf地址："+attach);
        System.out.println("----pdf完整地址："+BASEURL+"/Uploads/"+attach);

        downloadfile();//下载文件

    }


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
    private void init() {
        tvinfo = findViewById(R.id.textView10);
        tvpage = findViewById(R.id.textView11);
        pdfView=findViewById(R.id.pdfview);
    }

    private void downloadfile() {
        String downloadUrl = "/Uploads/"+attach;    //补全pdf文件相对地址
        //定义Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASEURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
//定义service
        DownloadService downloadService = retrofit.create(DownloadService.class);
//定义call
        Call<ResponseBody> responseBodyCall = downloadService.downloadFile(downloadUrl);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                System.out.println("----"+response.message()+" length "+response.body().contentLength()+" type "+response.body().contentType());
                //建立一个文件
                final File file = FileUtils4download.createFile(ViewTwareActivity.this,name);
                //下载文件放在子线程
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        //保存到本地
                        FileUtils4download.writeFile2Disk(response, file, new HttpCallBack() {
                            @Override
                            public void onLoading(final long current, final long total) {
                                /**更新进度条*/
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        tvinfo.setText(current+"");//当前进度
                                        System.out.println("----"+current+"--totale:"+total);

                                        if(current==total)  //如果达到最大值
                                        {
                                            tvinfo.setText("下载完成");
                                            tvinfo.setVisibility(View.GONE);//不可见
                                            String state = Environment.getExternalStorageState();
                                            String pdfName="";
                                            if(state.equals(Environment.MEDIA_MOUNTED)){
                                                pdfName=Environment.getExternalStorageDirectory().getAbsolutePath()+"/zyfypt-temp/"+name+".pdf";
                                            }
                                            else {
                                                pdfName=getCacheDir().getAbsolutePath()+"/zyfypt-temp/"+name+".pdf";
                                            }

                                            display(pdfName, false);
                                        }
                                    }
                                });
                            }
                        });
                    }
                }.start();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void display(String assetFileName, boolean jumpToFirstPage) {
        if (jumpToFirstPage) {
            setTitle(assetFileName);
        }
        File file = new File(assetFileName);
        pdfView.fromFile(file)
                .defaultPage(1)//默认展示第一页
                .onPageChange(this)//监听页面切换
                .load();
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        tvpage.setText(page + "/" + pageCount);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    private int userid;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        readSP();
        getMenuInflater().inflate(R.menu.menucollect, menu);//加载菜单布局
        collectmodel=new CollectModel();//实例化对象
        focusModel=new FocusModel();
        userid  = getIntent().getIntExtra("userid",1);
        collectmodel.exist("tware",resid,sessionID,listener);//判断是否收藏
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
                    collectmodel.uncollect("tware",resid,sessionID,listener);
                }
                else//如果未收藏，则调用收藏
                {
                    System.out.println("----准备收藏");
                    collectmodel.collect("tware",resid,sessionID,listener);
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
    private void readSP() {
        sp=getSharedPreferences(BaseFragment.FILE, MODE_PRIVATE);
        sessionID=sp.getString(BaseFragment.KEY_SESSION_ID,"");
    }
}
