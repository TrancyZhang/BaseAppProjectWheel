package com.project.base.app.tz.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.project.base.app.tz.R;
import com.project.base.app.tz.adapter.SecondListItemAdapter;
import com.project.base.app.tz.util.RecyclerViewUtil;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public class SecondItemFragment extends BaseFragment implements SecondFragment.SearchCallBack {

    @Bind(R.id.order_no_message_ll)
    LinearLayout orderNoMessageLl;
    @Bind(R.id.orderlist_recyclerview)
    RecyclerView orderlistRecyclerview;

    int mType = 0;
    RecyclerViewUtil recyclerViewUtil;

    int page_index = 1;
    int page_size = 10;
    ArrayList<String> mList = new ArrayList<>();
    private SecondListItemAdapter orderlistAdapter;

    public static SecondItemFragment newInstance(int index) {
        Bundle args = new Bundle();
        args.putInt("index", index);
        SecondItemFragment fragment = new SecondItemFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second_item, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mType = getArguments().getInt("index");
        initRecyclerView();
    }

    private void initRecyclerView() {
        orderlistAdapter = new SecondListItemAdapter(getActivity(), mList);
        orderlistRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        orderlistRecyclerview.setAdapter(orderlistAdapter);
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                initData();
            }
        });
        recyclerViewUtil = new RecyclerViewUtil(new RecyclerViewUtil.RecyclerCallBack() {
            @Override
            public void doRefresh() {

            }

            @Override
            public void doLoadMore() {
                loadMoreData();
            }
        }, orderlistRecyclerview, orderlistAdapter, true);
    }

    @Override
    public void searchCallBack() {
        initData();
    }

    private void initData() {

    }

    private void loadMoreData() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
