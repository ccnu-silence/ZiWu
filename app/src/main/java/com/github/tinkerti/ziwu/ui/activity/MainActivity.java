package com.github.tinkerti.ziwu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import com.github.tinkerti.ziwu.R;
import com.github.tinkerti.ziwu.data.Constants;
import com.github.tinkerti.ziwu.ui.fragment.MeFragment;
import com.github.tinkerti.ziwu.ui.fragment.PlanFragment;
import com.github.tinkerti.ziwu.ui.fragment.RecordFragment;
import com.github.tinkerti.ziwu.ui.utils.ZLog;
import com.github.tinkerti.ziwu.ui.widget.AddPlanButton;
import com.github.tinkerti.ziwu.ui.widget.TabIndicatorItemView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    //TOdo:一是退出之后正在进行的计划记录没有了 ；
    private static final String TAG = "MainActivity";
    private AddPlanButton addPlanButton;
    private LinearLayout addPlanPopupWindow;

    private TabIndicatorItemView planIndicator;
    private TabIndicatorItemView recordIndicator;
    private TabIndicatorItemView meIndicator;
    private LinearLayout bottomIndicator;

    private ViewPager contentViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ZLog.d(TAG, "onCreate");
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        addPlanButton = (AddPlanButton) findViewById(R.id.ac_bt_add_plan);
        addPlanPopupWindow = (LinearLayout) findViewById(R.id.ac_ll_add_plan_popup_window);

        planIndicator = (TabIndicatorItemView) findViewById(R.id.bottom_indicator_plan);
        recordIndicator = (TabIndicatorItemView) findViewById(R.id.bottom_indicator_record);
        meIndicator = (TabIndicatorItemView) findViewById(R.id.bottom_indicator_me);
        bottomIndicator = (LinearLayout) findViewById(R.id.ac_ll_bottom_indicator);

        contentViewPager = (ViewPager) findViewById(R.id.ac_view_pager);
        final List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new PlanFragment());
        fragmentList.add(new RecordFragment());
        fragmentList.add(new MeFragment());
        ContentPagerAdapter adapter = new ContentPagerAdapter(getSupportFragmentManager());
        adapter.setFragmentList(fragmentList);
        contentViewPager.setAdapter(adapter);
        contentViewPager.setCurrentItem(0);
        bottomIndicator.getChildAt(0).setSelected(true);
        contentViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < bottomIndicator.getChildCount(); i++) {
                    bottomIndicator.getChildAt(i).setSelected(i == position);
                    if (position == 1) {
                        if (fragmentList.get(position) instanceof RecordFragment) {
                            RecordFragment fragment = (RecordFragment) fragmentList.get(position);
                            fragment.selectPlanType(fragment.type);
                        }
                    }
                    if (position == 0) {
                        addPlanButton.setVisibility(View.VISIBLE);
                    } else {
                        addPlanButton.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        findViewById(R.id.ac_tv_add_today_plan).setOnClickListener(this);
        findViewById(R.id.ac_tv_add_this_week_plan).setOnClickListener(this);
        findViewById(R.id.ac_tv_add_this_long_time_plan).setOnClickListener(this);
        planIndicator.setOnClickListener(this);
        recordIndicator.setOnClickListener(this);
        meIndicator.setOnClickListener(this);
        addPlanButton.setOnClickListener(this);
        addPlanButton.setSelected(false);
    }

    @Override
    public void onCreateTitleBar(TitleBar titleBar) {
        ZLog.d(TAG, "onCreateTitleBar");
        View view = titleBar.onCreateTitle(R.layout.bar_plan_content_title);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ac_tv_add_today_plan:
                setAddPlanButtonState(Constants.DAY_TYPE);
                break;
            case R.id.ac_tv_add_this_week_plan:
                setAddPlanButtonState(Constants.WEEK_TYPE);
                break;
            case R.id.ac_tv_add_this_long_time_plan:
                setAddPlanButtonState(Constants.LONG_TERM_TYPE);
                break;
            case R.id.ac_bt_add_plan:
                if (!addPlanButton.isSelected()) {
                    addPlanButton.animate().rotation(45).setDuration(300).start();
                    addPlanPopupWindow.setVisibility(View.VISIBLE);
                    addPlanButton.setSelected(true);
                } else {
                    addPlanButton.animate().rotation(0).setDuration(300).start();
                    addPlanPopupWindow.setVisibility(View.GONE);
                    addPlanButton.setSelected(false);
                }
                break;

            case R.id.bottom_indicator_plan:
                contentViewPager.setCurrentItem(0);
                break;
            case R.id.bottom_indicator_record:
                contentViewPager.setCurrentItem(1);
                break;
            case R.id.bottom_indicator_me:
                contentViewPager.setCurrentItem(2);
                break;
        }
    }

    private void setAddPlanButtonState(int type) {
        addPlanButton.setSelected(false);
        addPlanButton.animate().setDuration(300).rotation(0).start();
        addPlanPopupWindow.setVisibility(View.GONE);
        Intent intent = new Intent(this, AddPlanDetailActivity.class);
        intent.putExtra("type", type);
        startActivityForResult(intent, Constants.ADD_PLAN_REQUEST);
    }

    private class ContentPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragmentList;

        public ContentPagerAdapter(FragmentManager fm) {
            super(fm);
            fragmentList = new ArrayList<>();
        }

        @Override
        public Fragment getItem(int position) {
            if (fragmentList != null) {
                return fragmentList.get(position);
            }
            return null;
        }

        @Override
        public int getCount() {
            if (fragmentList != null) {
                return fragmentList.size();
            }
            return 0;
        }

        public void setFragmentList(List<Fragment> fragmentList) {
            this.fragmentList = fragmentList;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ZLog.e(TAG, "onDestroy");
    }
}
