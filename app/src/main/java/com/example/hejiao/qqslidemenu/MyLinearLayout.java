package com.example.hejiao.qqslidemenu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class MyLinearLayout extends LinearLayout {

    public MyLinearLayout(Context context) {
        super(context);
    }

    public MyLinearLayout(Context context,AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private SlideMenu slideMenu;
    public void setSlideMenu(SlideMenu slideMenu){
        this.slideMenu= slideMenu;
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (slideMenu!= null && slideMenu.getCurrentState()==SlideMenu.DragState.OPEN){
            //SlideMenu打开，拦截事件
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (slideMenu!= null && slideMenu.getCurrentState()==SlideMenu.DragState.OPEN){
            if (event.getAction()==MotionEvent.ACTION_UP){
                //抬起则打开MainView
                slideMenu.open();
            }
            //SlideMenu打开，消费事件
            return true;
        }
        return super.onTouchEvent(event);
    }
}
