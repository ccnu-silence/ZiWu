package com.github.tinkerti.ziwu.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import com.github.tinkerti.ziwu.R;
import com.github.tinkerti.ziwu.ui.fragment.MeFragment;
import com.github.tinkerti.ziwu.ui.fragment.TaskFragment;
import com.github.tinkerti.ziwu.ui.fragment.RecordFragment;
import com.github.tinkerti.ziwu.ui.utils.ZLog;
import com.github.tinkerti.ziwu.ui.widget.TabIndicatorItemView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private TabIndicatorItemView taskIndicator;
    private TabIndicatorItemView recordIndicator;
    private TabIndicatorItemView meIndicator;
    private LinearLayout bottomIndicator;
    private ViewPager contentViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ZLog.d(TAG, "onCreate");
        initView();
    }

    private void initView() {
        setContentView(R.layout.activity_main);
        taskIndicator = findViewById(R.id.bottom_indicator_plan);
        recordIndicator = findViewById(R.id.bottom_indicator_record);
        meIndicator = findViewById(R.id.bottom_indicator_me);
        bottomIndicator = findViewById(R.id.ac_ll_bottom_indicator);
        contentViewPager = findViewById(R.id.ac_view_pager);
        final List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new TaskFragment());
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
                    //根据tab item的位置，选择
                    if (position == 0) {
                    } else {
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        taskIndicator.setOnClickListener(this);
        recordIndicator.setOnClickListener(this);
        meIndicator.setOnClickListener(this);
    }

    @Override
    public void onCreateTitleBar(TitleBar titleBar) {
        ZLog.d(TAG, "onCreateTitleBar");
        titleBar.onCreateTitle(R.layout.bar_plan_content_title);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
