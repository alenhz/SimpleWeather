package com.alen.simpleweather.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alen.simpleweather.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alen on 2017/12/23.
 */

public class ViewPagerIndicator implements ViewPager.OnPageChangeListener {
    private Context context;
    private ViewPager viewPager;
    private LinearLayout menu_layout;
    private List<ImageView> dotViewLists = new ArrayList<>();
    private int size;
    private int img1 = R.drawable.main_city, img2 = R.drawable.main_city_s;

    public ViewPagerIndicator(Context context, ViewPager viewPager, LinearLayout menu_layout, int size) {
        this.context = context;
        this.viewPager = viewPager;
        this.menu_layout = menu_layout;
        this.size = size;

        for (int i = 0; i < size; i++){
            ImageView imageView = new ImageView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            params.leftMargin = 10;
            params.rightMargin = 10;
            params.height = 10;
            params.weight = 10;
            if (i == 0){
                imageView.setBackgroundResource(img2);
            }else {
                imageView.setBackgroundResource(img1);
            }
            menu_layout.addView(imageView, params);
            dotViewLists.add(imageView);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        for (int i = 0; i < size; i++) {
            //选中的页面改变小圆点为选中状态，反之为未选中
            if ((position % size) == i) {
                ((View) dotViewLists.get(i)).setBackgroundResource(img2);
            } else {
                ((View) dotViewLists.get(i)).setBackgroundResource(img1);
            }
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
