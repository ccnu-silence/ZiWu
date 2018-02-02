package com.github.tinkerti.ziwu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.tinkerti.ziwu.R;
import com.github.tinkerti.ziwu.ui.event.RefreshRecordEvent;
import com.github.tinkerti.ziwu.ui.fragment.MeFragment;
import com.github.tinkerti.ziwu.ui.fragment.RecordFragment;
import com.github.tinkerti.ziwu.ui.fragment.TaskFragment;
import com.github.tinkerti.ziwu.ui.utils.ZLog;
import com.github.tinkerti.ziwu.ui.widget.OptionPopupWindow;
import com.github.tinkerti.ziwu.ui.widget.TabIndicatorItemView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    private static final int TAB_INDEX_TASK = 0;
    private static final int TAB_INDEX_RECORD = 1;
    private static final int TAB_INDEX_ME = 2;

    private LinearLayout bottomIndicator;
    private ViewPager contentViewPager;
    private FrameLayout optionImageContainer;
    private TextView titleView;
    private ImageView optionImage;
    private int tabIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ZLog.d(TAG, "onCreate");
        initView();
    }

    private void initView() {
        setContentView(R.layout.activity_main);
        TabIndicatorItemView taskIndicator = findViewById(R.id.bottom_indicator_plan);
        TabIndicatorItemView recordIndicator = findViewById(R.id.bottom_indicator_record);
        TabIndicatorItemView meIndicator = findViewById(R.id.bottom_indicator_me);
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
                    onPageChangedAction(position);
                    if (position == 1) {
                        EventBus.getDefault().post(new RefreshRecordEvent());
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

    private void onPageChangedAction(int index) {
        tabIndex = index;
        switch (index) {
            case TAB_INDEX_TASK:
                titleView.setText(getString(R.string.app_name));
                optionImage.setImageResource(R.mipmap.more_option_icon_2);
                optionImage.setVisibility(View.VISIBLE);
                break;
            case TAB_INDEX_RECORD:
                titleView.setText(getString(R.string.bottom_indicator_record));
                optionImage.setImageResource(R.mipmap.pie_action_icon);
                optionImage.setVisibility(View.VISIBLE);
                break;
            case TAB_INDEX_ME:
                titleView.setText(getString(R.string.bottom_indicator_me));
                optionImage.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onCreateTitleBar(TitleBar titleBar) {
        ZLog.d(TAG, "onCreateTitleBar");
        View view = titleBar.onCreateTitle(R.layout.bar_plan_content_title);
        //task add popup window
        titleView = view.findViewById(R.id.bar_tv_plan_content_title);
        optionImageContainer = view.findViewById(R.id.iv_more_option);
        optionImage = view.findViewById(R.id.iv_option);
        optionImageContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (tabIndex) {
                    case TAB_INDEX_TASK:
                        if (v.getTag() != null && ((OptionPopupWindow) v.getTag()).isShowing()) {
                            ((OptionPopupWindow) v.getTag()).dismiss();
                        } else {
                            OptionPopupWindow popupWindow = new OptionPopupWindow(v.getContext());
                            popupWindow.showAsDropDown(optionImageContainer, (int) optionImageContainer.getX(), 0);
                            v.setTag(popupWindow);
                        }
                        break;
                    case TAB_INDEX_RECORD:
                        Intent intent = new Intent(MainActivity.this, RecordChartActivity.class);
                        startActivity(intent);
                        break;
                    case TAB_INDEX_ME:
                        break;
                }
            }
        });
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

        ContentPagerAdapter(FragmentManager fm) {
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

        //方法为空，防止Fragment被回收
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

        }

        void setFragmentList(List<Fragment> fragmentList) {
            this.fragmentList = fragmentList;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ZLog.e(TAG, "onDestroy");
    }
}
