package com.alen.simpleweather.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alen.simpleweather.R;
import com.alen.simpleweather.gson.HefengData;
import com.alen.simpleweather.util.Utility;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Alen on 2017/12/26.
 */

public class LifeAdapter extends RecyclerView.Adapter<LifeAdapter.ViewHoder> {
    private int[] life_images = {R.drawable.life1, R.drawable.life2, R.drawable.life3,
            R.drawable.life4, R.drawable.life5, R.drawable.life6, R.drawable.life7, R.drawable.life8};
    private String[] life_names = {"生活指数", "穿衣指数", "感冒指数", "运动指数", "旅游指数", "紫外线指数", "洗车指数", "空气指数"};
    private Context context;
    private List<HefengData.LifeStyle> lifeStyles;

    public LifeAdapter(Context context, List<HefengData.LifeStyle> lifeStyles) {
        this.context = context;
        this.lifeStyles = lifeStyles;
    }


    static class ViewHoder extends RecyclerView.ViewHolder{
        View view;
        TextView life_name, life_info;
        ImageView life_image;
        public ViewHoder(View itemView) {
            super(itemView);
            this.view = itemView;
            life_name = (TextView) itemView.findViewById(R.id.life_name);
            life_info = (TextView) itemView.findViewById(R.id.life_info);
            life_image = (ImageView) itemView.findViewById(R.id.life_image);

            Utility.setTypeFace(new TextView[]{
                    life_name, life_info
            });
        }
    }

    @Override
    public ViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.life, parent, false);
        return new ViewHoder(view);

    }

    @Override
    public void onBindViewHolder(ViewHoder holder, int position) {
        holder.life_name.setText(life_names[position]+": "+lifeStyles.get(position).brf);
        Glide.with(context).load(life_images[position]).into(holder.life_image);
        holder.life_info.setText(lifeStyles.get(position).txt);
    }

    @Override
    public int getItemCount() {
        return lifeStyles.size();
    }
}
