package com.alen.simpleweather.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alen.simpleweather.R;
import com.alen.simpleweather.gson.MyCity;
import com.alen.simpleweather.util.Utility;
import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Alen on 2017/12/13.
 */

public class MyCityListAdapter extends RecyclerView.Adapter<MyCityListAdapter.ViewHolder> {
    private Context context;
    private ButtonInterface buttonInterface;
    private MyItemClickListener mItemClickListener;
    private List<MyCity> list;
    private boolean type;

    public MyCityListAdapter(Context context, List<MyCity> list, boolean type){
        this.context = context;
        this.list = list;
        this.type = type;
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        View view;
        Button my_city_list_item_delete_button;
        TextView my_city_list_item_city, my_city_list_item_superiors, my_city_list_item_tmp, my_city_list_item_CL01;
        ImageView my_city_list_item_code_image;
        private MyItemClickListener mListener;
        public ViewHolder(View itemView, MyItemClickListener myItemClickListener) {
            super(itemView);
            this.view= itemView;
            my_city_list_item_delete_button = (Button) itemView.findViewById(R.id.my_city_list_item_delete_button);
            my_city_list_item_code_image = (ImageView) itemView.findViewById(R.id.my_city_list_item_code_image);
            my_city_list_item_city = (TextView) itemView.findViewById(R.id.my_city_list_item_city);
            my_city_list_item_superiors = (TextView) itemView.findViewById(R.id.my_city_list_item_superiors);
            my_city_list_item_tmp = (TextView) itemView.findViewById(R.id.my_city_list_item_tmp);
            my_city_list_item_CL01 = (TextView) itemView.findViewById(R.id.my_city_list_item_CL01);

            this.mListener = myItemClickListener;
            itemView.setOnClickListener(this);

            Utility.setTypeFace(new TextView[]{
                    my_city_list_item_city, my_city_list_item_superiors, my_city_list_item_tmp, my_city_list_item_CL01
            });
        }

        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(view, getAdapterPosition(), my_city_list_item_delete_button.getVisibility()==View.GONE);
            }
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                context).inflate(R.layout.my_city_list_item, parent,
                false);
        ViewHolder holder = new ViewHolder(view, mItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        MyCity myCity = list.get(position);
        holder.my_city_list_item_city.setText(myCity.cn);
        StringBuilder sb = new StringBuilder();
        if (myCity.cn != myCity.city){
            sb.append(myCity.city).append(",");
        }
        if (myCity.city.equals(myCity.province)){
            sb.append("中国");
        }else {
            sb.append(myCity.province);
        }
        holder.my_city_list_item_superiors.setText(sb.toString());
        holder.my_city_list_item_tmp.setText(myCity.tmp);
        int nowTime = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        String imageId;
        if (myCity.tmp_code <= 103 && (nowTime<6 || nowTime>=18)) {
            imageId = "weather_icon_" + myCity.tmp_code + "_n";
        } else {
            imageId = "weather_icon_" + myCity.tmp_code;
        }
        int resId_n = context.getResources().getIdentifier(imageId, "drawable", context.getPackageName());
        Glide.with(context).load(resId_n).into(holder.my_city_list_item_code_image);

        if (type && position != 0){
            holder.my_city_list_item_delete_button.setVisibility(View.VISIBLE);
        }else {
            holder.my_city_list_item_delete_button.setVisibility(View.GONE);
        }
        holder.my_city_list_item_delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.view.setVisibility(View.GONE);
                if(buttonInterface!=null) {
//                  接口实例化后的而对象，调用重写后的方法
                    buttonInterface.onclick(view,position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     *按钮点击事件需要的方法
     */
    public void buttonSetOnclick(ButtonInterface buttonInterface){
        this.buttonInterface=buttonInterface;
    }

    /**
     * 按钮点击事件对应的接口
     */
    public interface ButtonInterface{
        public void onclick(View view, int position);
    }


    public interface MyItemClickListener {
        void onItemClick(View view, int position, boolean type);
    }
    public void setItemClickListener(MyItemClickListener myItemClickListener) {
        this.mItemClickListener = myItemClickListener;
    }
}
