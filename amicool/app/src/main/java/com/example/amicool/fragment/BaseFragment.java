package com.example.amicool.fragment;



public abstract class BaseFragment extends android.support.v4.app.Fragment {
    public final static String KEY_SESSION_ID = "sessionID";//与sharedpreferences保存的关键字一致
    public final static String KEY_USERNAME = "name";//与sharedpreferences保存的关键字一致

    public final static String FILE = "login";//与sharedpreferences的文件名一致
    private final int MODE = android.content.Context.MODE_PRIVATE;

    private android.content.SharedPreferences sharedPreferences;
    protected android.app.Activity activity;


    @Override  //Fragment生命周期方法
    public void onAttach(android.app.Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
        sharedPreferences = activity.getSharedPreferences(FILE, MODE);
    }
    //返回sessionid
    protected String getSessionId(){
        return sharedPreferences.getString(KEY_SESSION_ID, "");
    }
    //返回username
    protected String getUserName(){
        return sharedPreferences.getString(KEY_USERNAME, "");
    }
}
