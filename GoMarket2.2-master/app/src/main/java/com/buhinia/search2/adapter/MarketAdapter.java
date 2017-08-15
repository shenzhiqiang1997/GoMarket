package com.buhinia.search2.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.buhinia.search2.R;
import com.buhinia.search2.activity.MainActivity;
import com.buhinia.search2.activity.MarketActivity;
import com.buhinia.search2.model.MarketListItem;
import com.buhinia.search2.util.Distance;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * 超市配适器
 */
/*这个adapter仅供参考，用于做例子*/
/*recycler view的点击事件是在adapter中设置的*/
/*market在搜索超市界面中显示的adapter*/
public class MarketAdapter extends RecyclerView.Adapter<MarketAdapter.ViewHolder>{
    //上下文,用于Activity跳转
    private Context mContext;
    private List<MarketListItem> mMarketList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        View view1;
        ImageView marketImage;
        TextView marketText;
        TextView marketSite;
        public ViewHolder(View view){
            super(view);
            view1=view;
            marketImage=(ImageView) view.findViewById(R.id.list_market_pic);
            marketText=(TextView) view.findViewById(R.id.list_market_text);
            marketSite=(TextView) view.findViewById(R.id.list_market_site);
        }
    }
    public MarketAdapter(List<MarketListItem> marketList){
        mMarketList=marketList;
    }

    /*当有recycelrview中的子项滚动进屏幕的时候系统先调用下面的函数
    *来加载布局和生成ViewHolder
    * 然后系统将返回的ViewHolder传入onBindViewHolder();这个函数中来设置图片和文字
    * */
    @Override
    public MarketAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mContext==null){
            mContext=parent.getContext();
        }
        /*下面加载布局*/
        View view= LayoutInflater.from(mContext).inflate(R.layout.item_market_list,
                parent,false);
        final ViewHolder holder=new ViewHolder(view);


        /*设置点击事件*/
        holder.view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                MarketListItem market=mMarketList.get(position);
                Intent intent=new Intent(mContext,MarketActivity.class);
                intent.putExtra("marketId",market.getMarketId());
                mContext.startActivity(intent);
            }
        });
        return holder;
    }


    /**
     * @param holder 为在onCreateViewHolder()中生成的
     * @param position 需要设置的gooditem在list中的位置
     */
    @Override
    public void onBindViewHolder(MarketAdapter.ViewHolder holder, int position) {

        try {
            MarketListItem marketListItem=mMarketList.get(position);
            holder.marketText.setText(marketListItem.getMarketName());
        /*使用了GitHub上的开源项目Glide
        * Gilde是一个强大的图片加载库，可以自动实现压缩功能
        * https://github.com/bumptech/glide
        * */
            Glide.with(mContext).load(marketListItem.getMarketImageUrl()).centerCrop().into(holder.marketImage);
            holder.marketSite.setText(marketListItem.getMarketLocation()+"\n"+marketListItem.getMarketDistance()+"km");
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public int getItemCount(){
        return mMarketList.size();
    }
}
