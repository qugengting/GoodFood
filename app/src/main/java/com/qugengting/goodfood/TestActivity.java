package com.qugengting.goodfood;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.common.library.fragment.BaseFragment;
import com.common.library.fragment.BaseFragmentAdapter;
import com.common.library.fragment.CustomViewPager;
import com.common.library.util.systembar.SystemBarUtils;
import com.common.library.widget.ToolBar;
import com.qugengting.goodfood.fragment.SeasonHotFragment;
import com.qugengting.goodfood.fragment.WeekSelectionFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xuruibin on 2018/2/1.
 * 描述：
 */

public class TestActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    ToolBar toolBar;
    @BindView(R.id.layout_drawer)
    DrawerLayout layoutDrawer;
    @BindView(R.id.tab_main)
    TabLayout tabLayout;
    @BindView(R.id.vp_content)
    CustomViewPager viewPager;
    private BaseFragmentAdapter adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
//        SystemBarUtils.setTranslucentStatus(this, Color.parseColor("#ffce3d3a"));
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        toolBar.setLeftBtnSrc(R.drawable.titlebar_menu);
        toolBar.setLeftBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutDrawer.openDrawer(GravityCompat.START);
            }
        });
        List<BaseFragment> fragmentList = new ArrayList<>();
        WeekSelectionFragment weekSelectionFragment = new WeekSelectionFragment();
        weekSelectionFragment.setFragmentTitle("一周精选");
        SeasonHotFragment seasonHotFragment = new SeasonHotFragment();
        seasonHotFragment.setFragmentTitle("当季最热");
        fragmentList.add(weekSelectionFragment);
        fragmentList.add(seasonHotFragment);
        adapter = new BaseFragmentAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onBackPressed() {
        if (layoutDrawer.isDrawerOpen(GravityCompat.START)) {
            layoutDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 按返回键不退出应用。
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (layoutDrawer.isDrawerOpen(GravityCompat.START)) {
                layoutDrawer.closeDrawer(GravityCompat.START);
            } else {
                // 不退出程序，进入后台
                moveTaskToBack(true);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
