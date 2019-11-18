package com.project.base.app.tz.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;


import com.project.base.app.tz.R;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;

/**
 * Created by An4 on 2016/9/5.
 */
public class PtrHeaderView extends FrameLayout implements PtrUIHandler {
    private LayoutInflater inflater;
    // 下拉刷新视图（头部视图）
    private ViewGroup headView;

    // 下拉图标
//    private ImageView ivWindmill;
    private ProgressBar pb_img;
    private LinearLayout ll_refresh_completed;


    public PtrHeaderView(Context context) {
        super(context);
        init(context);
    }

    public PtrHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PtrHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(Context context) {
        inflater = LayoutInflater.from(context);
        /**
         * 头部
         */
        headView = (ViewGroup) inflater.inflate(R.layout.ptr_header, this, true);
//        ivWindmill = (ImageView) headView.findViewById(R.id.ptr_img);
        pb_img = headView.findViewById(R.id.pb_img);
        ll_refresh_completed = headView.findViewById(R.id.ll_refresh_completed);
//        ivWindmill.setImageResource(R.drawable.ptrani);
//        ivWindmill.setVisibility(VISIBLE);
    }

    @Override
    public void onUIReset(PtrFrameLayout frame) {
        pb_img.setVisibility(VISIBLE);
        ll_refresh_completed.setVisibility(GONE);
//        ivWindmill.clearAnimation();
//        ivWindmill.setImageResource(R.drawable.ptr_01);
//        ivWindmill.setImageResource(R.drawable.ptrani);
    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {
    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        pb_img.setVisibility(VISIBLE);
        ll_refresh_completed.setVisibility(GONE);
//        AnimationDrawable drawables = (AnimationDrawable) ivWindmill.getDrawable();
//        drawables.start();
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {
        pb_img.setVisibility(GONE);
        ll_refresh_completed.setVisibility(VISIBLE);
//        AnimationDrawable drawables = (AnimationDrawable) ivWindmill.getDrawable();
//        drawables.stop();
    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
        final int mOffsetToRefresh = frame.getOffsetToRefresh();
        final int currentPos = ptrIndicator.getCurrentPosY();
        final int lastPos = ptrIndicator.getLastPosY();

        if (currentPos < mOffsetToRefresh && lastPos >= mOffsetToRefresh) {
            if (isUnderTouch && status == PtrFrameLayout.PTR_STATUS_PREPARE) {
//                下拉刷新
            }
        } else if (currentPos > mOffsetToRefresh && lastPos <= mOffsetToRefresh) {
            if (isUnderTouch && status == PtrFrameLayout.PTR_STATUS_PREPARE) {
//               松开刷新
            }
        }

    }
}
