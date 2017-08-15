package com.buhinia.search2.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.buhinia.search2.adapter.MarketAdapter;
import com.buhinia.search2.R;
import com.buhinia.search2.model.MarketListItem;
import com.buhinia.search2.util.HttpRequest;

import java.util.ArrayList;
import java.util.List;



/*recycler view的点击事件是在adapter中设置的*/
public class SearchMarket extends AppCompatActivity{

    //用于存放返回的超市对象
    private List<MarketListItem> marketList=new ArrayList<>();
    private MarketAdapter adapter;

    private EditText editText;

    //搜索关键词
    private String keyWords=null;

    //用于发送请求的工具类
    private HttpRequest httpRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_market);
        httpRequest=new HttpRequest();



/*下面的两行代码为设置toolbar为actionbar的模式,并把标题设置为空，方便在xml中用EditText来代替*/
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_market);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        /*用于筛选的三个按钮*/
        Button bt_hot=(Button)findViewById(R.id.hot_market);
        Button bt_near=(Button)findViewById(R.id.near_market);
        Button bt_cheap=(Button)findViewById(R.id.cheap_market);

        editText=(EditText)findViewById(R.id.text_search_market);

        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recycle_view_market);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new MarketAdapter(marketList);
        recyclerView.setAdapter(adapter);



        /*得到actionbar的实例并通过设置将左上角的返回按钮显示出来*/
        final ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        /**
         * 下面三个点击监听事件用于请求搜索超市
         * 如果存在关键词那么就把关键词发往服务器
         * 否则直接发送搜索请求
         * 同时根据搜索排序条件传入相应的在bean内定义的比较器用于返回结果的排序
         */
        bt_hot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                marketList.clear();
                if (keyWords==null){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            httpRequest.requestForTagMarket(HttpRequest.rootPath+"Tag=[m]",marketList,adapter,SearchMarket.this,null,new MarketListItem.comparatorWithClickTimes());
                        }
                    }).start();
                }else{
                    httpRequest.requestForTagMarket(HttpRequest.rootPath+"Se=[m]".concat(keyWords),marketList,adapter,SearchMarket.this,null,new MarketListItem.comparatorWithClickTimes());
                }
                adapter.notifyDataSetChanged();
            }
        });
        bt_cheap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                marketList.clear();
                if (keyWords==null){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            httpRequest.requestForTagMarket(HttpRequest.rootPath+"Tag=[m]",marketList,adapter,SearchMarket.this,null,new MarketListItem.comparatorWithDiscount());
                        }
                    }).start();
                }else{
                    httpRequest.requestForTagMarket(HttpRequest.rootPath+"Se=[m]".concat(keyWords),marketList,adapter,SearchMarket.this,null,new MarketListItem.comparatorWithDiscount());
                }
                adapter.notifyDataSetChanged();
            }
        });
        bt_near.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                marketList.clear();
                if (keyWords==null){
                   new Thread(new Runnable() {
                       @Override
                       public void run() {
                           httpRequest.requestForTagMarket(HttpRequest.rootPath+"Tag=[m]",marketList,adapter,SearchMarket.this,null,new MarketListItem.comparatorWithDistance());
                       }
                   }).start();
                }else{
                    httpRequest.requestForTagMarket(HttpRequest.rootPath+"Se=[m]".concat(keyWords),marketList,adapter,SearchMarket.this,null,new MarketListItem.comparatorWithDistance());
                }
                adapter.notifyDataSetChanged();
            }
        });
    }


    /**
     *  执行搜索,在onOptionsItemSelected方法中调用此函数
     *  不点击搜索排序直接点击搜索时发送的请求
     *
     */
    private void searchMarket(String query){
        if (!marketList.isEmpty()){
            marketList.clear();
        }
        httpRequest.requestForLotsMarket(HttpRequest.rootPath+"Se=[m]".concat(query),marketList,adapter,this,null,null);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_common,menu);
        return true;
    }

    /*设置toolbar的点击事件，即左上角的返回按钮，其id是系统的资源id，不会变化*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.search_icon:
                //对搜索关键词进行预处理
                String text=editText.getText().toString();
                if (!TextUtils.isEmpty(text)){
                    text.replaceAll(" ","");
                    keyWords=text;
                    searchMarket(text);
                }
                break;
        }
        return true;
    }

}
