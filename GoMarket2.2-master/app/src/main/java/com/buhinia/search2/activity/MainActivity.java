package com.buhinia.search2.activity;

import android.Manifest;
import android.content.Intent;


import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.buhinia.search2.fragment.FirstFragment;
import com.buhinia.search2.fragment.HistoryFragment;
import com.buhinia.search2.fragment.MarketFragment;
import com.buhinia.search2.R;

import java.util.ArrayList;
import java.util.List;


/**
 * FirstFragment、GoodFragment、MarketFragment所在的Activity
 *
 */
public class MainActivity extends AppCompatActivity {



    /*点点滴滴*/
    Fragment mCurrentFragment;

    //用int来代替fragment节省开销 1为first 2为market 3为history
    private int currentFragmrnt;

    //标题视图
    private TextView title;

    //盛放所需要的权限最终一起向用户获取权限
    private List<String> permissions=new ArrayList<>();

    //定位客户端,用于对定位进行管理
    protected LocationClient locationClient;

    //在MainActivity常驻并获取地址,并存放于该类中作为全局变量以供计算时使用
    public static BDLocation userLocation;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //检查是否获取了权限,如果无权限则收集权限名,以待请求
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_STATE)!=PackageManager.PERMISSION_GRANTED){
            permissions.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissions.isEmpty()){
            ActivityCompat.requestPermissions(this,permissions.toArray(new String[permissions.size()]),1);

        }

        //初始化定位和地图客户端
        locationClient=new LocationClient(getApplicationContext());
        SDKInitializer.initialize(getApplicationContext());

        //注册定位监听器
        locationClient.registerLocationListener(new userLocationListener());

        //设置定位配置
        initLocationOption();

        //开始定位
        locationClient.start();


        /*下面的两行代码为设置toolbar为actionbar的模式,并把标题设置为空，方便在xml中用TextView来代替*/
        final Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_main);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        //title
        title=(TextView)findViewById(R.id.title);


        /*下面为注册底部导航栏的实例*/
        ImageView first=(ImageView)findViewById(R.id.first_image);
        ImageView market=(ImageView)findViewById(R.id.market_image);
        ImageView history =(ImageView)findViewById(R.id.history_image);

        final Fragment first_fragment=new FirstFragment();
        final Fragment market_fragment=new MarketFragment();
        final Fragment history_fragment=new HistoryFragment();

        /*设置初始时底部导航栏的pressed状态*/
        replaceFragment(first_fragment);
        mCurrentFragment=first_fragment;
        currentFragmrnt=1;
        first.setImageResource(R.drawable.main_bottom_tab_home_pressed);

        /*为底部导航栏各按钮设置点击切换fragment*/

        //点击首页图标转换到FirstFragment
        first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentFragmrnt!=1) {
                    title.setText("GO");
                    if(currentFragmrnt==2){
                        first.setImageResource(R.drawable.main_bottom_tab_home_pressed);
                        market.setImageResource(R.drawable.main_bottom_tab_cart_normal);
                    }
                    else if(currentFragmrnt==3){
                        first.setImageResource(R.drawable.main_bottom_tab_home_pressed);
                        history.setImageResource(R.drawable.main_bottom_tab_personal_normal);
                    }
                    currentFragmrnt=1;
                    replaceFragment(first_fragment);
                }
            }
        });

        //点击超市图标转换到MarketFragment
        market.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentFragmrnt!=2) {
                    title.setText("附近超市");
                    if(currentFragmrnt==1){
                        first.setImageResource(R.drawable.main_bottom_tab_home_normal);
                        market.setImageResource(R.drawable.main_bottom_tab_cart_pressed);
                    }
                    else if(currentFragmrnt==3){
                        market.setImageResource(R.drawable.main_bottom_tab_cart_pressed);
                        history.setImageResource(R.drawable.main_bottom_tab_personal_normal);
                    }
                    currentFragmrnt=2;
                    replaceFragment(market_fragment);
                }
            }
        });

        //点击历史图标转换到HistoryFragment
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentFragmrnt!=3) {
                    title.setText("浏览历史");
                    if(currentFragmrnt==1){
                        first.setImageResource(R.drawable.main_bottom_tab_home_normal);
                        history.setImageResource(R.drawable.main_bottom_tab_personal_pressed);
                    }
                    else if(currentFragmrnt==2){
                        history.setImageResource(R.drawable.main_bottom_tab_personal_pressed);
                        market.setImageResource(R.drawable.main_bottom_tab_cart_normal);
                    }
                    currentFragmrnt=3;
                    replaceFragment(history_fragment);
                }
            }
        });
    }

    /**
     * 根据获取权限情况,对未授权权限进行请求
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length>0){
                    for (int result:grantResults){
                        if (result!=PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(this,"拒绝授予权限，退出应用",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }
                break;
        }
    }

    /*下面两个函数为加载toolbar的布局及设置点击事件*/
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar_common,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.search_icon:
                if(mCurrentFragment instanceof FirstFragment){
                    Intent intent=new Intent(this,SearchGood.class);
                    startActivity(intent);
                    break;
                }
                else if(mCurrentFragment instanceof MarketFragment){
                    Intent intent=new Intent(this,SearchMarket.class);
                    startActivity(intent);
                    break;
                }

        }
        return true;
    }

    /*我设想成按一次在主界面按一次返回键，有toast提示再按一次，待实现*/
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /*下面的函数用于响应点击事件来切换fragment*/
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager=getSupportFragmentManager();
        /*开启事务，转换fragment*/
        FragmentTransaction transaction =fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment,fragment);
        transaction.commit();
    }

    //定位回调监听器内部类,只用于MainActivity
    private class userLocationListener implements BDLocationListener{

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            userLocation=bdLocation;
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }

    //配置定位设置
    private void initLocationOption(){
        LocationClientOption option=new LocationClientOption();
        option.setScanSpan(1000);
        locationClient.setLocOption(option);
    }

    //关闭程序后终止定位客户端
    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationClient.stop();
    }

}
