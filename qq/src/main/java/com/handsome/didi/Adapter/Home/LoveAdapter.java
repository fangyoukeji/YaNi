package com.handsome.didi.Adapter.Home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.handsome.didi.Activity.Home.DetailActivity;
import com.handsome.didi.Base.BaseController;
import com.handsome.didi.Bean.Shop;
import com.handsome.didi.Bean.Store;
import com.handsome.didi.Controller.ActivityController;
import com.handsome.didi.Controller.StoreController;
import com.handsome.didi.R;
import com.handsome.didi.Utils.GlideUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by handsome on 2016/4/8.
 */
public class LoveAdapter extends BaseAdapter implements View.OnClickListener {

    private StoreController storeController;
    private ActivityController activityController;

    private List<Shop> list;
    private LayoutInflater mInflater;
    private Context context;
    //选中集合
    private List<String> selected_objectId;
    //记录所有商品的商店ID集合
    private List<String> S_OID_list;
    //缓存商店名称
    private HashMap<Integer, String> map_store_name;

    public LoveAdapter(Context context, List<Shop> list) {
        this.list = list;
        this.context = context;
        mInflater = LayoutInflater.from(context);
        selected_objectId = new ArrayList<>();
        map_store_name = new HashMap<>();
        S_OID_list = new ArrayList<>();
        //将所有商店ID存放在集合中
        for (Shop shop : list) {
            S_OID_list.add(shop.S_OID);
        }

        storeController = StoreController.getInstance();
        activityController = ActivityController.getInstance();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_cart, null);
        }
        ViewHolder holder = getViewHolder(convertView);
        Shop shop = list.get(position);
        GlideUtils.displayImage(context, shop.show_urls.get(0), holder.iv_shop);
        holder.tv_name.setText(shop.name);
        holder.tv_price.setText("￥" + shop.price);
        holder.tv_postage.setText("快递：" + shop.postage);
        holder.tv_price_discount.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        holder.tv_price_discount.setText("￥" + shop.price_discount);
        holder.tv_sell_num.setText("月售" + shop.sell_num + "笔");
        holder.ly_shop.setTag(position);
        holder.ly_shop.setOnClickListener(this);
        holder.iv_check.setTag(position);
        holder.iv_check.setOnClickListener(this);

        if (selected_objectId.contains(shop.getObjectId())) {
            holder.iv_check.setBackgroundResource(R.drawable.cart_mid_ic_check_on);
        }else{
            holder.iv_check.setBackgroundResource(R.drawable.cart_mid_ic_check_off);
        }
        //区分不同商店物品
        if (position == 0) {
            holder.ly_store.setVisibility(View.VISIBLE);
            holder.ly_store.setTag(position);
            holder.ly_store.setOnClickListener(this);
            setStoreName(position, shop.S_OID, holder.tv_store_name);
        } else {
            if (S_OID_list.get(position).equals(S_OID_list.get(position - 1))) {
                holder.ly_store.setVisibility(View.GONE);
            } else {
                holder.ly_store.setVisibility(View.VISIBLE);
                holder.ly_store.setOnClickListener(this);
                holder.ly_store.setTag(position);
                setStoreName(position, shop.S_OID, holder.tv_store_name);
            }
        }
        return convertView;
    }

    /**
     * 获得控件管理对象
     *
     * @param view
     * @return
     */
    private ViewHolder getViewHolder(View view) {
        ViewHolder holder = (ViewHolder) view.getTag();
        if (holder == null) {
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        return holder;
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        switch (v.getId()) {
            case R.id.iv_check:
                checkAndSum(position, v);
                break;
            case R.id.ly_shop:
                activityController.startDetailActivityWithShop(context, list.get(position));
                break;
            case R.id.ly_store:
                activityController.startStoreActivityWithStoreId(context, list.get(position).S_OID);
                break;
        }
    }

    /**
     * 控件管理类
     */
    private class ViewHolder {
        private TextView tv_name, tv_price, tv_price_discount, tv_sell_num, tv_postage, tv_store_name;
        private ImageView iv_shop, iv_check;
        private LinearLayout ly_shop, ly_store;

        ViewHolder(View view) {
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_price = (TextView) view.findViewById(R.id.tv_price);
            tv_price_discount = (TextView) view.findViewById(R.id.tv_price_discount);
            tv_sell_num = (TextView) view.findViewById(R.id.tv_sell_num);
            tv_postage = (TextView) view.findViewById(R.id.tv_postage);
            tv_store_name = (TextView) view.findViewById(R.id.tv_store_name);
            iv_shop = (ImageView) view.findViewById(R.id.iv_shop);
            iv_check = (ImageView) view.findViewById(R.id.iv_check);
            ly_shop = (LinearLayout) view.findViewById(R.id.ly_shop);
            ly_store = (LinearLayout) view.findViewById(R.id.ly_store);
        }
    }

    public void checkAndSum(int position, View v) {
        Shop shop = list.get(position);
        String objectId = shop.getObjectId();
        if (selected_objectId.contains(objectId)) {
            selected_objectId.remove(objectId);
            v.setBackgroundResource(R.drawable.cart_mid_ic_check_off);
        } else {
            selected_objectId.add(objectId);
            v.setBackgroundResource(R.drawable.cart_mid_ic_check_on);
        }
    }

    /**
     * 获取选中的ObjectId
     *
     * @return
     */
    public List<String> getSelected_objectId() {
        return selected_objectId;
    }

    /**
     * 设置商铺名
     *
     * @param S_OID
     * @param textView
     */
    private void setStoreName(final int position, String S_OID, final TextView textView) {
        //取缓存
        if (map_store_name.containsKey(position)) {
            textView.setText(map_store_name.get(position));
            return;
        }
        //查询商店名称
        storeController.query(S_OID, new BaseController.OnBmobListener() {
            @Override
            public void onSuccess(List<?> list) {
                String store_name = ((Store) list.get(0)).name;
                map_store_name.put(position, store_name);
                textView.setText(store_name);
            }

            @Override
            public void onError(String error) {

            }
        });
    }
}
