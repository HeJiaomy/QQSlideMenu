package com.example.hejiao.qqslidemenu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class DragLayout extends ViewGroup {
    private View redView;
    private View blueView;

    public DragLayout(Context context) {
        super(context);
    }

    public DragLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DragLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(redView.getLayoutParams().width, MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(redView.getLayoutParams().height, MeasureSpec.EXACTLY);
        redView.measure(widthMeasureSpec, heightMeasureSpec);
        blueView.measure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        int left = getPaddingLeft() + (getMeasuredWidth() / 2 - redView.getMeasuredWidth() / 2);
        int top = getPaddingTop();
        redView.layout(left, top, redView.getMeasuredWidth(), redView.getMeasuredHeight());
        blueView.layout(left, redView.getBottom(), blueView.getMeasuredWidth(), redView.getMeasuredHeight() + blueView.getMeasuredHeight());
    }
}
