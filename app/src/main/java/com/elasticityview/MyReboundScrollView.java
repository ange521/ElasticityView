package com.elasticityview;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

/**
 * Created by Administrator on 2016/8/29.
 */
public class MyReboundScrollView extends ScrollView {

    private static final String TAG ="ELA";
//设置百分比 ，有滑动延迟效果
    private static final float MOVE_FACTOR = 0.5f;

    //松手后返回正常的动画时间
    private static final int ANIM_TIME = 100;

    //ScrollView的子view，也是ScrollView的唯一一个子view
    private View contentView;

    //手机按下的Y值
    private float startY;

    //用于记录正常的布局位置
    private Rect originaRect = new Rect();

    //手机按下时  是否可以继续下拉，
    private boolean canPullDown  = false;

    //手指按下记录是否可以继续上拉，
    private boolean canPullUp = false;

    //手指滑动时是否布局移动；
    private boolean isMoved  =false;

    public MyReboundScrollView(Context context) {
        super(context);
    }

    public MyReboundScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyReboundScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() >0)
            contentView = getChildAt(0);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (contentView == null)return;

        //用于计算唯一控件的位置，也就是说保留控件的原始位置不变
        originaRect.set(contentView.getLeft(), contentView.getTop(), contentView.getRight(), contentView.getBottom());
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (contentView == null)
            return super.dispatchTouchEvent(ev);

        int action = ev.getAction();

        switch(action){
            case MotionEvent.ACTION_DOWN:
                canPullDown = isCanPullDown();
                canPullUp = isCanPullUp();

                //记录按下的Y值
                startY = ev.getY();
                break;
            case MotionEvent.ACTION_UP:
                if (!isMoved)break;

                TranslateAnimation anim = new TranslateAnimation(0,0,contentView.getTop(),
                        originaRect.top);
                anim.setDuration(ANIM_TIME);

                contentView.startAnimation(anim);

                //设置回到原来的位置
                contentView.layout(originaRect.left,originaRect.top,originaRect.right,originaRect.bottom);

                canPullDown = false;
                canPullUp =false;
                isMoved = false;
                break;

            case MotionEvent.ACTION_MOVE:
                if (!canPullUp&&!canPullDown){
                    startY = ev.getY();
                    canPullDown = isCanPullDown();
                    canPullUp = isCanPullUp();
                    break;
                }

                float nowY = ev.getY();
                int deltaY =(int)(nowY-startY);

                //是否应该移动布局
                boolean shouldMove =(canPullDown&& deltaY >0)
                        ||(canPullUp&& deltaY<0)
                        ||(canPullUp&&canPullDown);

                if (shouldMove){
                    int offset = (int)(deltaY * MOVE_FACTOR);

                    contentView.layout(originaRect.left,originaRect.top+offset,originaRect.right,originaRect.bottom+offset);

                    isMoved = true;
                }
            break;

            default:
                break;
        }


        return super.dispatchTouchEvent(ev);
    }
    //判断是否滚动到顶部
    private boolean isCanPullDown(){
        //普通布局
//        return getScaleY() ==0 ||
//                contentView.getHeight() < getHeight() + getScrollY();
        //重写布局
        return true;
    }
    //判断是否滚动到底部
    private boolean isCanPullUp(){
        return contentView.getHeight() <= getHeight() + getScrollY();
    }
}
