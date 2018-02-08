package com.qugengting.goodfood;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;

import com.common.library.fragment.BaseFragment;
import com.common.library.fragment.BaseFragmentAdapter;
import com.common.library.fragment.CustomViewPager;
import com.common.library.widget.CustomRadioGroup;
import com.common.library.widget.ToolBar;
import com.qugengting.goodfood.adapter.ImageTitleAdapter;
import com.qugengting.goodfood.fragment.SeasonHotFragment;
import com.qugengting.goodfood.fragment.WeekSelectionFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by xuruibin on 2018/2/1.
 * 描述：
 */

public class TestActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    ToolBar toolBar;
    @BindView(R.id.rg_image)
    CustomRadioGroup radioGroup;
    @BindView(R.id.rb_local)
    RadioButton rbLocal;
    @BindView(R.id.rb_net)
    RadioButton rbNet;
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
        initRadioGroup();
    }

    @Override
    public void onBackPressed() {
        if (layoutDrawer.isDrawerOpen(GravityCompat.START)) {
            layoutDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private int clickTime = 0;
    private long time;
    private boolean isTest = false;

    @OnClick(R.id.iv_logo)
    public void getWelfare() {
        if (isTest) {
            if (clickTime == 0) {
                time = System.currentTimeMillis();
            }
            clickTime += 1;
            if (clickTime == 6) {
                long n = System.currentTimeMillis() - time;
                if (n < 1500) {
                    clickTime = 0;
                    Intent intent = new Intent(this, ImageTitleActivity.class);
                    startActivity(intent);
                } else {
                    clickTime = 0;
                    time = System.currentTimeMillis();
                }
            }
        } else {
            Intent intent = new Intent(this, ImageTitleActivity.class);
            startActivity(intent);
        }

    }

    private static final String LOCAL_IMAGE = "本地图片";
    private static final String NET_IMAGE = "网络图片";
    private boolean isNetImage = true;

    private void initRadioGroup() {
        rbLocal.setText(LOCAL_IMAGE);
        rbNet.setText(NET_IMAGE);
        radioGroup.check(R.id.rb_net);
        radioGroup.setHorizontalSpacing(12);
        radioGroup.setVerticalSpacing(8);
        radioGroup.setListener(new CustomRadioGroup.OnclickListener() {
            @Override
            public void OnText(String text) {
                if (text.equals(LOCAL_IMAGE)) {
                    isNetImage = false;
                } else {
                    isNetImage = true;
                }

            }
        });
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
