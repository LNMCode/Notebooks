package com.example.notebooks.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.notebooks.MainActivity;
import com.example.notebooks.R;
import com.example.notebooks.SlideActivity;
import com.example.notebooks.activities.SplashActivity;
import com.example.notebooks.activities.signin.SignInActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SlideAdapter extends PagerAdapter {
    private Context mContext;
    private ImageView imageView;
    private TextView contentViewPager;
    private TextView headingViewPager;

    private ArrayList<Drawable> listImage = new ArrayList<>();
    private ArrayList<String> listHeading = new ArrayList<>();
    private ArrayList<String> listContent = new ArrayList<>();

    public SlideAdapter(Context mContext) {
        this.mContext = mContext;
        addValue();
    }

    @Override
    public int getCount() {
        return listHeading.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((RelativeLayout) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.slide_item, container, false);

        imageView = view.findViewById(R.id.image_slide);
        headingViewPager = view.findViewById(R.id.heading_viewPager);
        contentViewPager = view.findViewById(R.id.content_viewPager);

        imageView.setImageDrawable(listImage.get(position));
        headingViewPager.setText(listHeading.get(position));
        contentViewPager.setText(listContent.get(position));

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
        super.destroyItem(container, position, object);
    }

    private void addValue(){
        listImage.add(mContext.getResources().getDrawable(R.drawable.human2));
        listImage.add(mContext.getResources().getDrawable(R.drawable.human3));

        listHeading.add("Save your brain");
        listHeading.add("Thanks for using");

        listContent.add("It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout.");
        listContent.add("Contrary to popular belief, Lorem Ipsum is not simply random text. It has roots in a piece of classical Latin literature from 45 BC, making it over 2000 years old.");


    }
}
