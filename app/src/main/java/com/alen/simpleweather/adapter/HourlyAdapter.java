package com.alen.simpleweather.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alen.simpleweather.MyHourlyData;
import com.alen.simpleweather.R;
import com.alen.simpleweather.util.Utility;
import com.alen.simpleweather.view.WeatherView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alen on 2017/11/28.
 */

public class HourlyAdapter extends RecyclerView.Adapter<HourlyAdapter.ViewHolder>{
    private List<MyHourlyData> datas = new ArrayList<>();
    private int highestDegree, lowestDegree;
    private Context context;
    private int weatherViewW;
    public HourlyAdapter(Context context, List<MyHourlyData> datas, int highestDegree, int lowestDegree, int weatherViewW) {
        this.context = context;
        this.datas = datas;
        this.highestDegree = highestDegree;
        this.lowestDegree = lowestDegree;
        this.weatherViewW = weatherViewW;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        View view;
        TextView hourly_precipitation, hourly_aqi, hourly_time;
        public ViewHolder(View itemview){
            super(itemview);
            this.view = itemView;
            hourly_aqi = (TextView) itemView.findViewById(R.id.hourly_aqi);
            hourly_precipitation = (TextView) itemView.findViewById(R.id.hourly_precipitation);
            hourly_time = (TextView) itemView.findViewById(R.id.hourly_time);
            Utility.setTypeFace(view.getContext(), new TextView[]{
                    hourly_precipitation, hourly_aqi, hourly_time
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                context).inflate(R.layout.hourly, parent,
                false);
        return new HourlyAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MyHourlyData myHourlyData = datas.get(position);
        ((WeatherView<MyHourlyData>) (holder.view.findViewById(R.id.hourly_view))).setWH(weatherViewW, weatherViewW * 2);
        ((WeatherView<MyHourlyData>) (holder.view.findViewById(R.id.hourly_view))).setDatas(datas, highestDegree, lowestDegree, position);
        holder.hourly_time.setText(myHourlyData.time);
        holder.hourly_precipitation.setText(myHourlyData.precipitation);
        holder.hourly_aqi.setText(myHourlyData.aqi);
        switch(myHourlyData.aqiBG){
            case 1:
                holder.hourly_aqi.setBackgroundResource(R.drawable.aqi1);
                break;
            case 2:
                holder.hourly_aqi.setBackgroundResource(R.drawable.aqi2);
                break;
            case 3:
                holder.hourly_aqi.setBackgroundResource(R.drawable.aqi3);
                break;
            case 4:
                holder.hourly_aqi.setBackgroundResource(R.drawable.aqi4);
                break;
            case 5:
                holder.hourly_aqi.setBackgroundResource(R.drawable.aqi5);
                break;
            case 6:
                holder.hourly_aqi.setBackgroundResource(R.drawable.aqi6);
                break;
            default:
        }

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }



}
