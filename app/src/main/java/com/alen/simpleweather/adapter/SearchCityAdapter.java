package com.alen.simpleweather.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alen.simpleweather.R;
import com.alen.simpleweather.SearchCityActivity;
import com.alen.simpleweather.db.CityList;
import com.alen.simpleweather.util.Utility;

import java.util.List;

/**
 * Created by Alen on 2017/12/8.
 */

public class SearchCityAdapter extends RecyclerView.Adapter<SearchCityAdapter.ViewHolder>{
    private Context context;
    private List<CityList> datas;
    private MyItemClickListener mItemClickListener;

    public SearchCityAdapter(Context context, List<CityList> datas){
        this.context = context;
        this.datas = datas;
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private MyItemClickListener mListener;
        TextView search_city_name_text, search_city_boole;
        View view;
        public ViewHolder(View itemView, MyItemClickListener myItemClickListener) {
            super(itemView);
            this.view = itemView;
            search_city_name_text = (TextView) itemView.findViewById(R.id.search_city_name_text);
            search_city_boole = (TextView) itemView.findViewById(R.id.search_city_boole);
            Utility.setTypeFace(view.getContext(), new TextView[]{
                    search_city_name_text, search_city_boole
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
                context).inflate(R.layout.search_city_item, parent,
                false);
        return new ViewHolder(view, mItemClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CityList cityList = datas.get(position);
        StringBuilder sb = new StringBuilder();
        sb.append(cityList.cn).append(" - ");
        if (cityList.cn != cityList.city){
            sb.append(cityList.city).append(",");
        }

        if (cityList.city.equals(cityList.province)){
            sb.append("中国");
        }else {
            sb.append(cityList.province);
        }
        holder.search_city_name_text.setText(sb.toString());
        if (cityList.judge.equals("y")){
            holder.search_city_boole.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public interface MyItemClickListener {
        void onItemClick(View view, int position);
    }
    public void setItemClickListener(MyItemClickListener myItemClickListener) {
        this.mItemClickListener = myItemClickListener;
    }
}
