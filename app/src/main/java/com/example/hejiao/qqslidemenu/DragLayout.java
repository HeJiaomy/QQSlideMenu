package com.example.hejiao.qqslidemenu;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

public class DragLayout extends FrameLayout {
    private View redView;
    private View blueView;
    private ViewDragHelper viewDragHelper;

    public DragLayout(Context context) {
        super(context);
        init();
    }

    public DragLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DragLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        viewDragHelper = ViewDragHelper.create(this, callback);
    }

    /**
     * xml文件读取到结束标签时执行
     * 一般做控件的初始化
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        redView = getChildAt(0);
        blueView = getChildAt(1);
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        widthMeasureSpec = MeasureSpec.makeMeasureSpec(redView.getLayoutParams().width, MeasureSpec.EXACTLY);
//        heightMeasureSpec = MeasureSpec.makeMeasureSpec(redView.getLayoutParams().height, MeasureSpec.EXACTLY);
//        redView.measure(widthMeasureSpec, heightMeasureSpec);
//        blueView.measure(widthMeasureSpec, heightMeasureSpec);

    //对子View没有特殊的测量需求可以用下面的方法测量
//        measureChild(redView, widthMeasureSpec, heightMeasureSpec);
//        measureChild(blueView, widthMeasureSpec, heightMeasureSpec);
//    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        int left = getPaddingLeft();
        int top = getPaddingTop();
        redView.layout(left, top, redView.getMeasuredWidth(), redView.getMeasuredHeight());
        blueView.layout(left, redView.getBottom(), blueView.getMeasuredWidth(), redView.getMeasuredHeight() + blueView.getMeasuredHeight());
    }

    //由ViewDragHelper判断是否拦截
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }

    //将触摸事件交给VeiwDragHelper来解析处理
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        return true;
    }

    ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {

        //获取水平方向上移动范围
        @Override
        public int getViewHorizontalDragRange(@NonNull View child) {
            return getMeasuredWidth() - child.getMeasuredWidth();
        }

        //获取垂直方向上移动范围
        @Override
        public int getViewVerticalDragRange(@NonNull View child) {
            return getMeasuredHeight() - child.getMeasuredHeight();
        }

        //控制View在水平方向移动
        @Override
        public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
            if (left < 0) {
                left = 0;
            } else if (left > getMeasuredWidth() - child.getMeasuredWidth()) {
                left = getMeasuredWidth() - child.getMeasuredWidth();
            }
            return left;
        }

        //控制View在垂直方向移动
        @Override
        public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
            if (top < 0) {
                top = 0;
            } else if (top > getMeasuredHeight() - child.getMeasuredHeight()) {
                top = getMeasuredHeight() - child.getMeasuredHeight();
            }
            return top;
        }

        @Override
        public boolean tryCaptureView(@NonNull View child, int pointerId) {
            return child == blueView;
        }
    };
}
