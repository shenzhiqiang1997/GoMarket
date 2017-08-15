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
import com.buhinia.search2.activity.GoodActivity;
import com.buhinia.search2.model.GoodListItem;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * 商品配适器
 */
/*这个adapter仅供参考，用于做例子*/
/*recycler view的点击事件是在adapter中设置的*/
public class GoodAdapter extends RecyclerView.Adapter<GoodAdapter.ViewHolder>{
    //上下文,用于Activity的跳转
    private Context mContext;
    private List<GoodListItem> mGoodList;

    static class ViewHolder extends RecyclerView.ViewHolder{

        View goodView;
        ImageView goodImage;
        TextView goodText;
        TextView goodPrice;
        TextView goodOldPrice;
        TextView goodSite;
        TextView goodDistance;

        public ViewHolder(View view){
            super(view);
            goodView=view;
            goodImage=(ImageView) view.findViewById(R.id.list_good_pic);
            goodText=(TextView) view.findViewById(R.id.list_good_text);
            goodPrice=(TextView) view.findViewById(R.id.list_good_price);
            goodOldPrice=(TextView) view.findViewById(R.id.list_old_price);
            goodSite=(TextView) view.findViewById(R.id.list_good_site);
            goodDistance=(TextView) view.findViewById(R.id.list_good_distance);
        }
    }
    public GoodAdapter(List<GoodListItem> goodList){
        mGoodList=goodList;
    }

    /*当有recycelrview中的子项滚动进屏幕的时候系统先调用下面的函数
    *来加载布局和生成ViewHolder
    * 然后系统将返回的ViewHolder传入onBindViewHolder();这个函数中来设置图片和文字
    * */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mContext==null){
            mContext=parent.getContext();
        }
        /*下面加载布局*/
        View view= LayoutInflater.from(mContext).inflate(R.layout.item_good_list,
                parent,false);
        final ViewHolder holder=new ViewHolder(view);

        /*设置点击监听器*/
        holder.goodView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                GoodListItem good=mGoodList.get(position);
                Intent intent=new Intent(mContext,GoodActivity.class);
                intent.putExtra("goodId",good.getGoodId());
                intent.putExtra("marketId",good.getMarketId());

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
    public void onBindViewHolder(ViewHolder holder, int position) {
        GoodListItem goodListItem=mGoodList.get(position);
        holder.goodText.setText(goodListItem.getGoodName());
        /*使用了GitHub上的开源项目Glide
        * Gilde是一个强大的图片加载库，可以自动实现压缩功能
        * https://github.com/bumptech/glide
        * */
        Glide.with(mContext).load(goodListItem.getGoodImageUrl()).into(holder.goodImage);
        holder.goodPrice.setText(String.valueOf(goodListItem.getGoodCurrentPrice()));
        holder.goodSite.setText(goodListItem.getGoodSite());
        holder.goodDistance.setText(String.valueOf(goodListItem.getGoodDistance())+"km");
        holder.goodOldPrice.setText(String.valueOf(goodListItem.getGoodOriginalPrice()));
    }
    public int getItemCount(){
        return mGoodList.size();
    }
}
