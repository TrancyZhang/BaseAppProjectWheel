package com.project.base.app.tz.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.projec.base.app.tz.R;
import com.project.base.app.tz.customview.tabview.TabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SecondFragment extends BaseFragment {

    @Bind(R.id.my_order_tablayout)
    TabLayout myOrderTablayout;
    @Bind(R.id.my_order_viewpager)
    ViewPager myOrderViewpager;

    private String[] mTitles = new String[]{"标题1", "标题2", "标题3"};//0所有，1待服务，2已完成

    private List<SecondItemFragment> secondItemFragments;
    SecondItemFragment secondItemFragment;
    MyOrderListAdapter myOrderListAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    private void initData() {
        secondItemFragments = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            secondItemFragment = new SecondItemFragment().newInstance(i);
            secondItemFragments.add(secondItemFragment);
        }

        myOrderViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                SecondItemFragment sd = (SecondItemFragment) myOrderListAdapter.getItem(position);
                sd.searchCallBack();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        myOrderViewpager.post(new Runnable() {
            @Override
            public void run() {
                myOrderListAdapter = new MyOrderListAdapter(getChildFragmentManager(), secondItemFragments, mTitles);
                myOrderViewpager.setAdapter(myOrderListAdapter);
                myOrderViewpager.setOffscreenPageLimit(3);
                myOrderTablayout.setupWithViewPager(myOrderViewpager);
                myOrderTablayout.setTabMode(TabLayout.MODE_FIXED);
                for (int i = 0; i < mTitles.length; i++) {
                    myOrderTablayout.getTabAt(i).setCustomView(getActivity().getLayoutInflater().inflate(R.layout.item_tab_text, null));
                    TextView textView = myOrderTablayout.getTabAt(i).getCustomView().findViewById(R.id.tv_tab);
                    textView.setText(mTitles[i]);
                    textView.setTextColor(getResources().getColor(R.color.color_Text222222));
                    if (i == 0) {
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                        textView.setTextColor(getResources().getColor(R.color.color_Text1FB7B6));
                    }
                }
                myOrderTablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        TextView textView = tab.getCustomView().findViewById(R.id.tv_tab);
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                        textView.setTextColor(getResources().getColor(R.color.color_Text1FB7B6));
                        myOrderViewpager.setCurrentItem(tab.getPosition());
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        TextView textView = tab.getCustomView().findViewById(R.id.tv_tab);
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                        textView.setTextColor(getResources().getColor(R.color.color_Text222222));
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });
                if (secondItemFragments.size() > 0) {
                    SecondItemFragment sd = (SecondItemFragment) myOrderListAdapter.getItem(0);
                    sd.searchCallBack();
                }
            }
        });
    }

    public class MyOrderListAdapter extends FragmentPagerAdapter {

        private List<SecondItemFragment> list_fragment;                         //fragment列表
        private String[] list_Title;                              //tab名的列表

        public MyOrderListAdapter(FragmentManager fm, List<SecondItemFragment> list_fragment, String[] list_Title) {
            super(fm);
            this.list_fragment = list_fragment;
            this.list_Title = list_Title;
        }

        @Override
        public Fragment getItem(int position) {
            return list_fragment.get(position);
        }

        @Override
        public int getCount() {
            return list_fragment.size();
        }

        //此方法用来显示tab上的名字
        @Override
        public CharSequence getPageTitle(int position) {
            return list_Title[position];
        }
    }

    public interface SearchCallBack {
        void searchCallBack();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
