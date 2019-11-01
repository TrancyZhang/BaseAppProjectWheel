package com.project.base.app.tz.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.projec.base.app.tz.R;
import com.project.base.app.tz.customview.PtrHeaderView;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrFrameLayout;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class BaseFragment extends Fragment {

    @Bind(R.id.ptrframelayout)
    @Nullable
    public PtrFrameLayout mPtrFrameLayout;
    CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        if (mPtrFrameLayout != null) {
            mPtrFrameLayout.setResistance(1.7f);
            mPtrFrameLayout.setRatioOfHeaderHeightToRefresh(1.2f);
            mPtrFrameLayout.setDurationToClose(300);
            mPtrFrameLayout.setDurationToCloseHeader(1500);
            mPtrFrameLayout.setPullToRefresh(false);
            mPtrFrameLayout.setKeepHeaderWhenRefresh(true);
            PtrHeaderView headerView = new PtrHeaderView(getActivity());
            mPtrFrameLayout.setHeaderView(headerView);
            mPtrFrameLayout.addPtrUIHandler(headerView);
        }
        super.onViewCreated(view, savedInstanceState);
    }

    public void jumpToActivity(Class<?> toClass, Bundle bundle, int flag) {
        Intent intent = new Intent(getActivity(), toClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if (flag != -1) {
            intent.setFlags(flag);
        }
        startActivity(intent);
    }

    public void showToast(final String message) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }
            });
        }
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
                errorAction.call(throwable);
            }
        }));
    }

}
