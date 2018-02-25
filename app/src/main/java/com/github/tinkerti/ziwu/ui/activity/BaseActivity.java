package com.github.tinkerti.ziwu.ui.activity;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.tinkerti.ziwu.R;

/**
 * Created by tiankui on 4/9/17.
 */

public class BaseActivity extends FragmentActivity {

    FrameLayout planContentFrameLayout;

    protected TextView nameTextView;
    private ViewGroup titleBar;
    private ImageView navigationImageView;
    private ImageView searchImageView;
    private View dividerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.setContentView(R.layout.activity_base);
        planContentFrameLayout = findViewById(R.id.ac_fl_base_container);
        nameTextView = findViewById(R.id.bar_tv_title);
        titleBar = findViewById(R.id.bar_rl_title_bar);
        dividerView = findViewById(R.id.divider_line);
        navigationImageView = findViewById(R.id.bar_iv_navigate_back);
        searchImageView = findViewById(R.id.bar_iv_search_button);

        onCreateTitleBar(new TitleBar());
        navigationImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPress();
            }
        });
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        View view = View.inflate(getBaseContext(), layoutResID, null);
        setContentView(view);
    }

    @Override
    public void setContentView(View view) {
        planContentFrameLayout.addView(view);
    }

    public void setNameText(String text) {
        nameTextView.setText(text);
    }

    public void onBackPress() {
        finish();
    }

    public void onCreateTitleBar(TitleBar titleBar) {
    }

    public class TitleBar {
        public void setTitleBackgroudColor(int color) {
            titleBar.setBackgroundColor(color);
        }

        public View onCreateTitle(int resId) {
            nameTextView.setVisibility(View.GONE);
            navigationImageView.setVisibility(View.GONE);
            searchImageView.setVisibility(View.GONE);
            dividerView.setVisibility(View.GONE);
            View view = View.inflate(getBaseContext(), resId, titleBar);
            return view;
        }
    }
}
