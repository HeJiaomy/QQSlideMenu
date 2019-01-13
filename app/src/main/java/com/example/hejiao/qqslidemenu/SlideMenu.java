package com.example.hejiao.qqslidemenu;

import android.animation.ArgbEvaluator;
import android.animation.FloatEvaluator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
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
    private FloatEvaluator floatEvaluator;
    private ArgbEvaluator argbEvaluator;

    enum DragState {
        OPEN, CLOSE
    }

    private DragState currentState = DragState.CLOSE;//默认状态关闭

    public DragState getCurrentState() {
        return currentState;
    }

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
        floatEvaluator = new FloatEvaluator();
        argbEvaluator = new ArgbEvaluator();
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
                mainView.layout(newLeft, mainView.getTop() + dy, newLeft + mainView.getMeasuredWidth() + dx, mainView.getBottom() + dy);
            }
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            float fraction = (float) (mainView.getLeft() / dragRange);
            executeAnim(fraction);

            //设置监听
            if (fraction == 0 && listener != null) {
                currentState = DragState.CLOSE;
                listener.onClose();
            } else if (fraction == 1 && listener != null) {
                currentState = DragState.OPEN;
                listener.onOpen();
            }
            //拖拽中
            if (listener != null) {
                listener.onDrafting(fraction);
            }
        }

        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            if (mainView.getLeft() < dragRange / 2) {
                //mainView向左移动，打开
                open();
            } else {
                //mainView向右移动，关闭
                close();
            }

            if (xvel > 200 && currentState == DragState.OPEN) {
                open();
            } else if (xvel < -200 && currentState == DragState.CLOSE) {
                close();
            }
        }
    };

    public void open() {
        viewDragHelper.smoothSlideViewTo(mainView, 0, mainView.getTop());
        ViewCompat.postInvalidateOnAnimation(SlideMenu.this);
    }

    public void close() {
        viewDragHelper.smoothSlideViewTo(mainView, (int) dragRange, mainView.getTop());
        ViewCompat.postInvalidateOnAnimation(SlideMenu.this);
    }

    /**
     * 执行伴随动画
     *
     * @param fraction
     */
    private void executeAnim(float fraction) {
        //缩小mainView
        mainView.setScaleX(floatEvaluator.evaluate(fraction, 1, 0.8));
        mainView.setScaleY(floatEvaluator.evaluate(fraction, 1, 0.8));
        //移动menuView
        menuView.setTranslationX(floatEvaluator.evaluate(fraction, -menuView.getMeasuredWidth() / 2, 0));
        //放大menuView
        menuView.setScaleX(floatEvaluator.evaluate(fraction, 0.5, 1));
        menuView.setScaleY(floatEvaluator.evaluate(fraction, 0.5, 1));
        //设置menuView透明度
        menuView.setAlpha(floatEvaluator.evaluate(fraction, 0.3, 1));
        //设置背景颜色遮罩效果
        getBackground().setColorFilter((Integer) argbEvaluator.evaluate(fraction, Color.BLACK, Color.TRANSPARENT), PorterDuff.Mode.SRC_OVER);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (viewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(SlideMenu.this);
        }
    }

    private OnDragStateChangeListener listener;

    public void setDragStateChangeListener(OnDragStateChangeListener listener) {
        this.listener = listener;
    }

    public interface OnDragStateChangeListener {
        void onOpen();

        void onClose();

        void onDrafting(float fraction);
    }
}
