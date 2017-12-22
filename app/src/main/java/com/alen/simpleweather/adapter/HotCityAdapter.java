package com.alen.simpleweather.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alen.simpleweather.R;
import com.alen.simpleweather.util.Utility;


/**
 * Created by Alen on 2017/12/8.
 */

public class HotCityAdapter extends RecyclerView.Adapter<HotCityAdapter.ViewHolder>{
    private Context context;
    private String[] datas;
    private MyItemClickListener mItemClickListener;

    public HotCityAdapter(Context context, String[] datas){
        this.context = context;
        this.datas = datas;
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private MyItemClickListener mListener;
        View view;
        TextView hot_city_text;
        ImageView hot_city_location_image;
        public ViewHolder(View itemView, MyItemClickListener myItemClickListener){
            super(itemView);
            this.view = itemView;
            hot_city_text = (TextView) itemView.findViewById(R.id.hot_city_text);
            hot_city_location_image = (ImageView) itemView.findViewById(R.id.hot_city_location_image);
            Utility.setTypeFace(view.getContext(), new TextView[]{
                    hot_city_text
            });
            this.mListener = myItemClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(view, getAdapterPosition());
            }
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                context).inflate(R.layout.hot_city_item, parent,
                false);
        return new ViewHolder(view, mItemClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(position == 0){
            holder.hot_city_location_image.setVisibility(View.VISIBLE);
        }
        holder.hot_city_text.setText(datas[position]);
    }

    @Override
    public int getItemCount() {
        return datas.length;
    }

    public interface MyItemClickListener {
        void onItemClick(View view, int position);
    }
    public void setItemClickListener(MyItemClickListener myItemClickListener) {
        this.mItemClickListener = myItemClickListener;
    }
}
