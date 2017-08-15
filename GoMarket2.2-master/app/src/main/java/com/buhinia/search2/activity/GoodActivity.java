package com.buhinia.search2.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.buhinia.search2.R;
import com.buhinia.search2.model.BrowseHistory;
import com.buhinia.search2.model.GoodListItem;
import com.buhinia.search2.model.MarketListItem;
import com.buhinia.search2.util.Distance;
import com.buhinia.search2.util.HttpRequest;
import com.bumptech.glide.Glide;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.HashMap;
import java.util.Map;


/*当直接在首页或者搜索界面中点击某个商品时就会跳转到此界面，同时传入一个Intent，或是其他的提取数据的方法
*
* 在本活动中goodMarketSite与进入超市按钮时是可以点击的
*
* goodMarketSite点击后进入地图
* */


/**
 * 显示商品详细信息的Activity
 */
public class GoodActivity extends AppCompatActivity {


    ImageView goodImage;
    TextView goodText;
    TextView goodPrice;
    TextView goodOldPrice;
    TextView goodDistance;
    TextView goodMarketName;
    TextView goodMarketSite;
    TextView goodMarketPhone;
    ImageButton phoneIcon;
    HttpRequest httpRequest;
    //TextView enterMarket; 改成了点击超市名称进入超市
    public static Map<Long,GoodListItem> params=new HashMap<>();
    GoodListItem good=null;
    MarketListItem market=null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good);

        //获得goodId与marketId
        Intent intent=getIntent();
        long goodId=intent.getLongExtra("goodId", 0);
        long marketId=intent.getLongExtra("marketId",0);

        //请求服务器获得数据
        httpRequest=new HttpRequest();
        httpRequest.requestForOneGood(HttpRequest.rootPath+"Id=[g]".concat(String.valueOf(goodId)),this);
        httpRequest.requestForOneMarket(HttpRequest.rootPath+"Id=[m]".concat(String.valueOf(marketId)),this);


        //对浏览历史和点击量进行记录
        LitePal.getDatabase();
        BrowseHistory browseHistory=new BrowseHistory(goodId);
        if (!DataSupport.findBySQL("select * from BrowseHistory where goodId="+browseHistory.getGoodId()).moveToFirst()){
            browseHistory.save();
        }
        /*下面的两行代码为设置toolbar为actionbar的模式,并把标题设置为空，方便在xml中用Text来代替*/
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_good);
        toolbar.setTitle("商品详情");
        setSupportActionBar(toolbar);

        /*得到actionbar的实例并通过设置将左上角的返回按钮显示出来*/
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        /*初始化数据*/
        goodImage=(ImageView)findViewById(R.id.good_image);
        goodText=(TextView)findViewById(R.id.good_text);
        goodPrice=(TextView)findViewById(R.id.good_price);
        goodOldPrice=(TextView)findViewById(R.id.old_price) ;
        goodDistance=(TextView)findViewById(R.id.good_distance);
        goodMarketName=(TextView)findViewById(R.id.good_market_name);
        goodMarketPhone=(TextView)findViewById(R.id.good_market_phone);
        goodMarketSite=(TextView)findViewById(R.id.good_market_site);
        phoneIcon=(ImageButton)findViewById(R.id.phone_icon);
        //enterMarket=(TextView)findViewById(R.id.enter_market);

        //阻塞主线程直到异步请求完毕再向布局设置文字
        while ((good=GoodActivity.params.get(goodId))==null||(market= MarketActivity.params.get(marketId))==null);

        //向布局上设置信息
        Glide.with(this).load(good.getGoodImageUrl()).into(goodImage);
        goodText.setText(good.getGoodName());
        goodPrice.setText(String.valueOf(good.getGoodCurrentPrice()));
        goodOldPrice.setText(String.valueOf(good.getGoodOriginalPrice()));
        goodDistance.setText(String.valueOf(Distance.getDistance(MainActivity.userLocation,market.getMarketLatitude(),market.getMarketLongitude())).substring(0,4).concat("km"));
        goodMarketSite.setText(market.getMarketLocation());
        goodMarketPhone.setText(String.valueOf(market.getMarketPhoneNumber()));
        goodMarketName.setText(market.getMarketName());

        //为超市地点设置点击事件,点击后打开地图定位到超市所在位置
        goodMarketSite.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent1=new Intent(GoodActivity.this,MapActivity.class);
               intent1.putExtra("la",market.getMarketLatitude());
               intent1.putExtra("lo",market.getMarketLongitude());
               startActivity(intent1);
           }
       });

        //为点击超市名字设置点击事件,点击后打开该商品所在的超市Activity
        goodMarketName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(GoodActivity.this,MarketActivity.class);
                intent.putExtra("marketId",market.getMarketId());
                startActivity(intent);
            }
        });

        //为电话号码设置点击事件,点击后打开拨号板预输入该商品所在超市电话
        phoneIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+goodMarketPhone.getText().toString()));
                startActivity(intent);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_good,menu);
        return true;
    }

    /*设置toolbar的点击事件，即左上角的返回按钮，其id是系统的资源id，不会变化*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }
}
