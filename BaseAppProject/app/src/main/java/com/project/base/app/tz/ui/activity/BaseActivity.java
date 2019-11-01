package com.project.base.app.tz.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.projec.base.app.tz.R;
import com.project.base.app.tz.customview.PtrHeaderView;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RequestExecutor;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrFrameLayout;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by An4 on 2019-09-24.
 */

public class BaseActivity extends AppCompatActivity {

    @Bind(R.id.toolbar_actionbar)
    @Nullable
    public Toolbar mToolbar;
    @Bind(R.id.include_ll_network_error)
    @Nullable
    LinearLayout networkErrorLl;
    @Bind(R.id.network_error_refresh_tv)
    @Nullable
    TextView networkErrorRefreshTv;
    @Bind(R.id.ptrframelayout)
    @Nullable
    PtrFrameLayout mPtrFrameLayout;
    CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        StatusBarUtil.setStatusBarMode(this, false, R.color.colorPrimary);
        setBarColor(getResources().getColor(R.color.color_White));
    }

    public void setBarColor(@ColorInt int color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 设置状态栏底色颜色
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(color);
            if (isLightColor(color)) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
        }
    }

    /**
     * 判断颜色是不是亮色
     *
     * @param color
     * @return
     * @from https://stackoverflow.com/questions/24260853/check-if-color-is-dark-or-light-in-android
     */
    private boolean isLightColor(@ColorInt int color) {
        return ColorUtils.calculateLuminance(color) >= 0.5;
    }
    public void getPermissionMethod() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            AndPermission.with(this).permission(Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS)
                    .rationale(new Rationale() {
                        @Override
                        public void showRationale(Context context, List<String> permissions, RequestExecutor executor) {

                        }
                    })
                    .onGranted(new Action() {
                        @Override
                        public void onAction(List<String> permissions) {
                            System.out.println("---------有权限了");
//                            showToast("有权限了");
                        }
                    })
                    .onDenied(new Action() {
                        @Override
                        public void onAction(List<String> permissions) {
                            System.out.println("---------没有权限");
//                            if (AndPermission.hasAlwaysDeniedPermission(Select_or_Picture_Activity.this, permissions)) {
//                                mSetting.showSetting(permissions);
//                            }
                        }
                    }).start();
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeAsUpIndicator(R.mipmap.icon_toolbar_back);
            }
        }

        if (mPtrFrameLayout != null) {
            mPtrFrameLayout.setResistance(1.7f);
            mPtrFrameLayout.setRatioOfHeaderHeightToRefresh(1.2f);
            mPtrFrameLayout.setDurationToClose(300);
            mPtrFrameLayout.setDurationToCloseHeader(1500);
            mPtrFrameLayout.setPullToRefresh(false);
            mPtrFrameLayout.setKeepHeaderWhenRefresh(true);
            PtrHeaderView headerView = new PtrHeaderView(BaseActivity.this);
            mPtrFrameLayout.setHeaderView(headerView);
            mPtrFrameLayout.addPtrUIHandler(headerView);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    void jumpToActivity(Class<?> toClass, Bundle bundle,int flag) {
        Intent intent = new Intent(this, toClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if(flag!=-1){
            intent.setFlags(flag);
        }
        startActivity(intent);
    }

    void showToast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BaseActivity.this.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public CompositeSubscription getmCompositeSubscription() {
        if (mCompositeSubscription == null)
            mCompositeSubscription = new CompositeSubscription();
        else if (mCompositeSubscription.isUnsubscribed()) {
            mCompositeSubscription = new CompositeSubscription();
        }
        return mCompositeSubscription;
    }

    public <T> void bindObservable(final Observable<T> observable, final Action1<T> nextAction, final Action1<Throwable> errorAction) {

        if (observable == null) {
            errorAction.call(new Throwable("请求失败"));
            return;
        }
        getmCompositeSubscription().add(observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<T>() {
            @Override
            public void call(T t) {
                nextAction.call(t);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                handleNetworkError(throwable, observable, nextAction, errorAction);
                errorAction.call(throwable);
            }
        }));
    }

    public <T> void handleNetworkError(Throwable throwable, final Observable<T> observable, final Action1<T> successAction, final Action1<Throwable> errorAction) {
        if (networkErrorLl != null) {
            networkErrorLl.setVisibility(View.VISIBLE);
            networkErrorRefreshTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    networkErrorLl.setVisibility(View.GONE);
                    BaseActivity.this.bindObservable(observable, successAction, errorAction);
                }
            });
        }
    }

}
