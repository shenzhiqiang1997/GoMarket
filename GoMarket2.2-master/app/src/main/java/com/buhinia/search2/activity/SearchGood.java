package com.buhinia.search2.activity;

import android.content.Intent;
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

import com.buhinia.search2.adapter.GoodAdapter;
import com.buhinia.search2.R;
import com.buhinia.search2.model.GoodListItem;
import com.buhinia.search2.util.HttpRequest;

import java.util.ArrayList;

import java.util.List;



/*recycler view的点击事件是在adapter中设置的*/
public class SearchGood extends AppCompatActivity {

    //用于存放返回的商品对象
    private List<GoodListItem> goodList=new ArrayList<>();
    private GoodAdapter adapter;

    //搜索关键词
    private String keyWords=null;


    //用于区分是否从超市搜索商品
    private boolean fromMarket=false;

    //用于存放所在的超市id
    private long marketId;

    EditText editText;
    //请求工具
    HttpRequest httpRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_good);

        //判断是否是从超市进入搜寻商品
        Intent intent=getIntent();
        marketId=intent.getLongExtra("marketId",-1);
        if (marketId==-1){
            fromMarket=false;
        }else {
            fromMarket=true;

        }

        //初始化请求客户端
        httpRequest=new HttpRequest();




/*下面的两行代码为设置toolbar为actionbar的模式,并把标题设置为空，方便在xml中用EditText来代替*/
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        /*用于筛选的三个按钮*/
        Button bt_hot=(Button) findViewById(R.id.hot);
        Button bt_near=(Button) findViewById(R.id.near);
        Button bt_cheap=(Button)findViewById(R.id.cheap);


        editText=(EditText)findViewById(R.id.text_search);

        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recycle_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new GoodAdapter(goodList);
        recyclerView.setAdapter(adapter);

        /*得到actionbar的实例并通过设置将左上角的返回按钮显示出来*/
        final ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        /**
         * 下面三个点击监听事件用于请求搜索商品
         * 如果来自超市则向所在超市发送搜索请求
         * 否则直接发送搜索请求
         * 同时根据搜索排序条件传入相应的在bean内定义的比较器用于返回结果的排序
         */

        //按热度排序搜索
        bt_hot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goodList.clear();
                if (fromMarket){
                    if (keyWords==null){
                        httpRequest.requestForLotsGood(HttpRequest.rootPath+"Id=[mg]".concat(String.valueOf(marketId)),goodList,adapter,SearchGood.this,null,new GoodListItem.comparatorWithClickTimes());
                    }else{
                        httpRequest.requestForLotsGood(HttpRequest.rootPath+"Se=[m]".concat(String.valueOf(marketId)).concat("[g]").concat(keyWords),goodList,adapter,SearchGood.this,null,new GoodListItem.comparatorWithClickTimes());
                    }

                }else {
                    if (keyWords==null){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                httpRequest.requestForTagGood(HttpRequest.rootPath+"Tag=[g]",goodList,adapter,SearchGood.this,null,new GoodListItem.comparatorWithClickTimes());
                            }
                        }).start();
                    }else{
                        httpRequest.requestForLotsGood(HttpRequest.rootPath+"Se=[g]".concat(keyWords),goodList,adapter,SearchGood.this,null,new GoodListItem.comparatorWithClickTimes());
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });

        //按价格排序搜索
        bt_cheap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goodList.clear();
                if (fromMarket){
                    if (keyWords==null){
                        httpRequest.requestForLotsGood(HttpRequest.rootPath+"Id=[mg]".concat(String.valueOf(marketId)),goodList,adapter,SearchGood.this,null,new GoodListItem.comparatorWithCurrentPrice());
                    }else{
                        httpRequest.requestForLotsGood(HttpRequest.rootPath+"Se=[m]".concat(String.valueOf(marketId)).concat("[g]").concat(keyWords),goodList,adapter,SearchGood.this,null,new GoodListItem.comparatorWithCurrentPrice());
                    }

                }else {
                    if (keyWords==null){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                httpRequest.requestForTagGood(HttpRequest.rootPath+"Tag=[g]",goodList,adapter,SearchGood.this,null,new GoodListItem.comparatorWithCurrentPrice());
                            }
                        }).start();
                    }else{
                        httpRequest.requestForLotsGood(HttpRequest.rootPath+"Se=[g]".concat(keyWords),goodList,adapter,SearchGood.this,null,new GoodListItem.comparatorWithCurrentPrice());
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });

        //按远近排序搜索
        bt_near.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goodList.clear();
                if (fromMarket){
                    if (keyWords==null){
                        httpRequest.requestForLotsGood(HttpRequest.rootPath+"Id=[mg]".concat(String.valueOf(marketId)),goodList,adapter,SearchGood.this,null,new GoodListItem.comparatorWithDistance());
                    }else{
                        httpRequest.requestForLotsGood(HttpRequest.rootPath+"Se=[m]".concat(String.valueOf(marketId)).concat("[g]").concat(keyWords),goodList,adapter,SearchGood.this,null,new GoodListItem.comparatorWithDistance());
                    }

                }else {
                    if (keyWords==null){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                httpRequest.requestForTagGood(HttpRequest.rootPath+"Tag=[g]",goodList,adapter,SearchGood.this,null,new GoodListItem.comparatorWithDistance());
                            }
                        }).start();
                    }else{
                        httpRequest.requestForLotsGood(HttpRequest.rootPath+"Se=[g]".concat(keyWords),goodList,adapter,SearchGood.this,null,new GoodListItem.comparatorWithDistance());
                    }

                }

                adapter.notifyDataSetChanged();
            }
        });

    }


    /**
     * 执行搜索,在onOptionsItemSelected方法中调用此函数
     * 当不点击标签排序搜索直接点击搜索框
     * 如果存在关键词那么就把关键词发往服务器
     * 否则直接发送搜索请求
     */
    private void searchGood(String query){
        goodList.clear();
        if (fromMarket){
            httpRequest.requestForLotsGood(HttpRequest.rootPath+"Se=[m]".concat(String.valueOf(marketId)).concat("[g]").concat(query),goodList,adapter,this,null,null);
        }else{
            httpRequest.requestForLotsGood(HttpRequest.rootPath+"Se=[g]".concat(query),goodList,adapter,this,null,null);
        }
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
                    searchGood(keyWords);
                }
                break;
        }
        return true;
    }
}


