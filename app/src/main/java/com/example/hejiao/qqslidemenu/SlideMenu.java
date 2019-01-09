package com.example.hejiao.qqslidemenu;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

public class SlideMenu extends FrameLayout {

    private View mainView;
    private View menuView;
    private ViewDragHelper viewDragHelper;

    public SlideMenu(Context context) {
        super(context);
        init();
    }

    public SlideMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SlideMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        viewDragHelper= ViewDragHelper.create(this,cb);
    }
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //异常处理
        if (getChildCount()!= 2){
            throw new IllegalArgumentException("SlideMenu only have 2 children!");
        }
        menuView = getChildAt(0);
        mainView= getChildAt(1);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        return true;
    }

    private ViewDragHelper.Callback cb= new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(@NonNull View child, int pointerId) {
            return false;
        }
    };
}
