package com.example.hejiao.qqslidemenu;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

public class SlideMenu extends FrameLayout {

    private View mainView;
    private View menuView;
    private ViewDragHelper viewDragHelper;
    private double dragRange;

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

    private void init() {
        viewDragHelper = ViewDragHelper.create(this, cb);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //异常处理
        if (getChildCount() != 2) {
            throw new IllegalArgumentException("SlideMenu only have 2 children!");
        }
        menuView = getChildAt(0);
        mainView = getChildAt(1);
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

    /**
     * 该方法在onMeasure完成后执行，可以初始化自己和子view的宽高
     *
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        dragRange = getMeasuredWidth() * 0.6;
    }

    private ViewDragHelper.Callback cb = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(@NonNull View child, int pointerId) {
            return child == menuView || child == mainView;
        }

        //控制移动范围
        @Override
        public int getViewHorizontalDragRange(@NonNull View child) {
            return (int) dragRange;
        }

        //控制View在水平方向上移动
        @Override
        public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
            if (child == mainView) {
                if (left < 0) left = 0; //限制mainView的左边界
                if (left > dragRange) left = (int) dragRange;//限制mainView的右边界
            }
            return left;
        }

        //childView位置改变时执行
        @Override
        public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
            if (changedView == menuView) {
                Log.e("dx", String.valueOf(dx));
                //固定menuView
                menuView.layout(0, 0, (int) dragRange, menuView.getMeasuredHeight());
                int newLeft = mainView.getLeft() + dx;
                if (newLeft < 0) newLeft = 0;
                if (newLeft > dragRange) newLeft = (int) dragRange;
                mainView.layout(newLeft, mainView.getTop() + dy, mainView.getRight() + dx, mainView.getBottom() + dy);
            }
            super.onViewPositionChanged(changedView, left, top, dx, dy);
        }

        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            if (mainView.getLeft() < dragRange / 2) {
                viewDragHelper.smoothSlideViewTo(mainView, 0, mainView.getTop());
            } else {
                viewDragHelper.smoothSlideViewTo(mainView, (int) dragRange, mainView.getTop());
            }
            ViewCompat.postInvalidateOnAnimation(SlideMenu.this);
        }
    };

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (viewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(SlideMenu.this);
        }
    }
}
