package com.buhinia.search2.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.buhinia.search2.adapter.GoodAdapter;
import com.buhinia.search2.R;
import com.buhinia.search2.model.GoodListItem;
import com.buhinia.search2.model.MarketListItem;
import com.buhinia.search2.util.Distance;
import com.buhinia.search2.util.HttpRequest;
import com.bumptech.glide.Glide;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MarketActivity extends AppCompatActivity {



    private List<GoodListItem> goodList=new ArrayList<>();
    private List<MarketListItem> marketList=new ArrayList<>();
    private GoodAdapter adapter;
    private HttpRequest httpRequest;
    private long marketId;
    public static Map<Long,MarketListItem> params=new HashMap<Long,MarketListItem>();

    //店铺图片
    private ImageView marketImage;
    //店铺位置
    private TextView marketSite;
    //店铺距离
    private TextView marketDistance;
    //店铺电话
    private TextView marketPhone;
    //店铺名称
    private TextView marketName;
    //电话图标
    private ImageButton phoneIcon;
    //店铺位置所在的布局
    private LinearLayout marketSiteIcon;

    MarketListItem market;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market);

        //从点击的item处获取点击的超市id
        Intent intent=getIntent();
        marketId=intent.getLongExtra("marketId", 0);
        //请求服务器获得数据
        httpRequest=new HttpRequest();
        httpRequest.requestForOneMarket(HttpRequest.rootPath+"Id=[m]".concat(String.valueOf(marketId)),this);



        /*下面的两行代码为设置toolbar为actionbar的模式,并把标题设置为空，方便在xml中用EditText来代替*/
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_market);
        toolbar.setTitle("店铺详情");
        setSupportActionBar(toolbar);

        marketName=(TextView)findViewById(R.id.market_text);
        marketDistance=(TextView)findViewById(R.id.market_distance);
        marketImage=(ImageView)findViewById(R.id.market_image);
        marketPhone=(TextView)findViewById(R.id.market_phone);
        marketSite=(TextView)findViewById(R.id.market_site);
        phoneIcon=(ImageButton)findViewById(R.id.market_phone_icon);
        marketSiteIcon=(LinearLayout)findViewById(R.id.market_site_icon);

        //阻塞主线程直到异步请求完毕再向布局设置文字
        while((market=MarketActivity.params.get(marketId))==null);

        marketName.setText(market.getMarketName());
        marketDistance.setText(String.valueOf(Distance.getDistance(MainActivity.userLocation,market.getMarketLatitude(),market.getMarketLongitude()))+"km");
        Glide.with(this).load(market.getMarketImageUrl()).into(marketImage);
        marketPhone.setText(String.valueOf(market.getMarketPhoneNumber()));
        marketSite.setText(market.getMarketLocation());

        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recycle_view_market_good);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new GoodAdapter(goodList);
        recyclerView.setAdapter(adapter);

        //请求服务器获得该商店的商品
        httpRequest.requestForLotsGood(HttpRequest.rootPath+"Id=[mg]".concat(String.valueOf(marketId)),goodList,adapter,this,null,new GoodListItem.comparatorWithClickTimes());

        /*得到actionbar的实例并通过设置将左上角的返回按钮显示出来*/
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //点击电话按钮打电话
        phoneIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+marketPhone.getText().toString()));
                startActivity(intent);
            }
        });

        //点击位置图标所在布局打开地图
        marketSiteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(MarketActivity.this,MapActivity.class);
                intent1.putExtra("la",market.getMarketLatitude());
                intent1.putExtra("lo",market.getMarketLongitude());
                startActivity(intent1);
            }
        });

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
                /*在进行此次搜索时应考虑到是在本超市内搜索*/
                Intent intent=new Intent(this,SearchGood.class);
                intent.putExtra("marketId",marketId);
                startActivity(intent);
                break;
        }
        return true;
    }
}
