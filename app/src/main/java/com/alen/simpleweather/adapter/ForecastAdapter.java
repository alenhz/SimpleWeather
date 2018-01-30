package com.alen.simpleweather.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alen.simpleweather.view.MyForecastData;
import com.alen.simpleweather.R;
import com.alen.simpleweather.util.Utility;
import com.alen.simpleweather.view.WeatherView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alen on 2017/11/23.
 */

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ViewHolder> {
    private List<MyForecastData> datas = new ArrayList<>();
    private int highestDegree, lowestDegree;
    private Context context;
    private int weatherViewW;
    public ForecastAdapter(Context context, List<MyForecastData> datas, int highestDegree, int lowestDegree, int weatherViewW) {
        this.context = context;
        this.datas = datas;
        this.highestDegree = highestDegree;
        this.lowestDegree = lowestDegree;
        this.weatherViewW = weatherViewW;
    }
    static class ViewHolder extends RecyclerView.ViewHolder{
        View view;
        TextView date;
        TextView cond_txt_d;
        TextView cond_txt_n;
        ImageView cond_code_d;
        ImageView cond_code_n;
        TextView wind_dir;
        TextView wind_sc;
        TextView week;
        public ViewHolder (View itemview){
            super(itemview);
            this.view = itemView;
            week = (TextView) itemview.findViewById(R.id.forecast_week);
            date = (TextView) itemview.findViewById(R.id.forecast_date);
            cond_code_d = (ImageView) itemview.findViewById(R.id.image_d);
            cond_code_n = (ImageView) itemview.findViewById(R.id.image_n);
            cond_txt_d = (TextView) itemview.findViewById(R.id.forecast_weather_d);
            cond_txt_n = (TextView) itemview.findViewById(R.id.forecast_weather_n);
            wind_dir = (TextView) itemview.findViewById(R.id.forecast_wind_dir);
            wind_sc = (TextView) itemview.findViewById(R.id.forecast_wind_sc);
            Utility.setTypeFace(new TextView[]{
                    week, date, cond_txt_d, cond_txt_n, wind_dir, wind_sc
            });
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                context).inflate(R.layout.forecast, parent,
                false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MyForecastData myForecastData = datas.get(position);
        ((WeatherView<MyForecastData>) (holder.view.findViewById(R.id.forecast_view))).setWH(weatherViewW, weatherViewW * 2);
        ((WeatherView<MyForecastData>) (holder.view.findViewById(R.id.forecast_view))).setDatas(datas, highestDegree, lowestDegree, position);
        holder.week.setText(myForecastData.week);
        holder.date.setText(myForecastData.date);
        holder.cond_txt_d.setText(myForecastData.cond_txt_d);
        holder.cond_txt_n.setText(myForecastData.cond_txt_n);
        holder.wind_dir.setText(myForecastData.wind_dir);
        holder.wind_sc.setText(myForecastData.wind_sc);
        Glide.with(context).load(myForecastData.cond_code_d).into(holder.cond_code_d);
        Glide.with(context).load(myForecastData.cond_code_n).into(holder.cond_code_n);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }
}
