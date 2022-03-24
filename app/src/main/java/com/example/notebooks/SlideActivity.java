package com.example.notebooks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.notebooks.activities.signin.SignInActivity;
import com.example.notebooks.adapters.SlideAdapter;

import java.util.ArrayList;

public class SlideActivity extends AppCompatActivity {
    public static final String SHARED_PREFS = "sharedPrefs";
    private int numberDot = 2;
    private LinearLayout dotView;
    private ViewPager viewPager;
    private Button buttonStarted;
    private ArrayList<String> listButton = new ArrayList<>();

    private ArrayList<TextView> listDots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        setContentView(R.layout.activity_slide);
        initView();
        dotView.bringToFront();
        viewPager.setAdapter(new SlideAdapter(SlideActivity.this));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 1){
                    buttonStarted.setText(listButton.get(position));
                    buttonStarted.animate().translationY(0f).setDuration(400)
                            .setInterpolator(new OvershootInterpolator())
                            .start();
                    buttonStarted.setOnClickListener(view1 -> {
                        Intent intent = new Intent(SlideActivity.this , SignInActivity.class); // if not sign in
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    });
                } else {
                    buttonStarted.animate().translationY(500f).setDuration(600)
                            .setInterpolator(new OvershootInterpolator())
                            .start();
                }
            }

            @Override
            public void onPageSelected(int position) {
                addDotsIndicator(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        addDotsIndicator(0);

    }

    private void initView(){
        listButton.add("");
        listButton.add("Get Started");
        dotView = findViewById(R.id.dotsViewPager);
        viewPager = findViewById(R.id.viewPager);
        buttonStarted = findViewById(R.id.button_getStarted);
    }

    private void addDotsIndicator(int position){
        listDots = new ArrayList<>();
        dotView.removeAllViews();

        for (int i = 0; i < numberDot; i++){
            TextView textView = new TextView(this);
            textView.setText(Html.fromHtml("&#8226;"));
            textView.setTextSize(50f);
            textView.setTextColor(getResources().getColor(R.color.colorGrayOpacity));
            listDots.add(textView);
            viewPager.addView(textView);
        }

        if (listDots.size() > 0){
            listDots.get(position).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            listDots.get(position).setTextSize(65f);
            listDots.get(position).setTranslationY(15f);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}