package com.project.base.app.tz.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.projec.base.app.tz.R;
import com.project.base.app.tz.ui.fragment.HomeFragment;
import com.project.base.app.tz.ui.fragment.SecondFragment;

import butterknife.Bind;

public class MainActivity extends BaseActivity {


    @Bind(R.id.main_framelayout)
    FrameLayout mainFramelayout;
    @Bind(R.id.main_home_radiobtn)
    RadioButton mainHomeRadiobtn;
    @Bind(R.id.main_order_radiobtn)
    RadioButton mainOrderRadiobtn;
    @Bind(R.id.main_radiogroup)
    RadioGroup mainRadiogroup;

    Fragment lastFragment;
    HomeFragment homeFragment;
    SecondFragment secondFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setBarColor(getResources().getColor(R.color.colorGreenMain));
        initView();
    }

    private void initView() {
        mainRadiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.main_home_radiobtn:
                        if (homeFragment == null) {
                            homeFragment = new HomeFragment();
                        }
                        addFragmentToStack(homeFragment);
                        setBarColor(getResources().getColor(R.color.colorGreenMain));
                        break;
                    case R.id.main_order_radiobtn:
                        if (secondFragment == null) {
                            secondFragment = new SecondFragment();
                        }
                        addFragmentToStack(secondFragment);
                        setBarColor(getResources().getColor(R.color.color_White));
                        break;
                }
            }
        });

        mainHomeRadiobtn.setChecked(true);
    }

    private void addFragmentToStack(Fragment fragment) {
        if (lastFragment != null && lastFragment == fragment) {
            return;
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (lastFragment != null) {
            transaction.hide(lastFragment);
        }
        if (!fragment.isAdded()) {
            transaction.add(R.id.main_framelayout, fragment);
        } else {
            transaction.show(fragment);
        }
        lastFragment = fragment;
        transaction.commitAllowingStateLoss();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }


}
