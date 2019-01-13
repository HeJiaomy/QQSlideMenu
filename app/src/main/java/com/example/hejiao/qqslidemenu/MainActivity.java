package com.example.hejiao.qqslidemenu;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.CycleInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ListView menuListView, mainListView;
    private SlideMenu slideMenu;
    private ImageView ivHead;
    private MyLinearLayout myLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        myLinearLayout.setSlideMenu(slideMenu);
    }

    private void initData() {
        mainListView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Constant.NAMES));
        menuListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Constant.sCheeseStrings) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setTextColor(Color.WHITE);
                return textView;
            }
        });

        slideMenu.setDragStateChangeListener(new SlideMenu.OnDragStateChangeListener() {
            @Override
            public void onOpen() {
                menuListView.smoothScrollToPosition(new Random().nextInt(menuListView.getCount()));
            }

            @Override
            public void onClose() {
                ivHead.animate().translationXBy(15)
                        .setInterpolator(new CycleInterpolator(2))  //循环次数
                        .setDuration(500)
                        .start();
            }

            @Override
            public void onDrafting(float fraction) {
                ivHead.setAlpha(1 - fraction);
            }
        });


    }

    private void initView() {
        mainListView = findViewById(R.id.main_listview);
        menuListView = findViewById(R.id.menu_listview);
        slideMenu = findViewById(R.id.slideMenu);
        ivHead = findViewById(R.id.iv_head);
        myLinearLayout = findViewById(R.id.my_layout);
    }


}
