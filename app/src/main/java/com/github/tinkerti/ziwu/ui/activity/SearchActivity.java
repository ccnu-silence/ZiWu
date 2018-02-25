package com.github.tinkerti.ziwu.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.github.tinkerti.ziwu.R;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseActivity {

    private ListView historyListView;
    List<String> keywordList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        historyListView = findViewById(R.id.lv_search_history);
        TextView historyTitleView = findViewById(R.id.tv_search_history_title);
        keywordList.add("test");
        ArrayAdapter<String> listAdapter = new ArrayAdapter<>(SearchActivity.this, R.layout.ad_search_history_item, keywordList);
        historyListView.setAdapter(listAdapter);
        int visibility = keywordList.size() == 0 ? View.GONE : View.VISIBLE;
        historyListView.setVisibility(visibility);
        historyTitleView.setVisibility(visibility);
    }

    @Override
    public void onCreateTitleBar(TitleBar titleBar) {
        View view = titleBar.onCreateTitle(R.layout.title_bar_search);
        titleBar.setTitleBackgroudColor(getColor(R.color.whiteColor));
    }
}
