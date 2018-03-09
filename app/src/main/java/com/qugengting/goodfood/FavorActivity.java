package com.qugengting.goodfood;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ListView;

import com.common.library.util.systembar.SystemBarUtils;
import com.common.library.widget.ToolBar;
import com.qugengting.goodfood.adapter.FavorAdapter;
import com.qugengting.goodfood.bean.GoodFoodFavor;
import com.qugengting.goodfood.eventbus.ItemDelete;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xuruibin on 2018/3/9.
 * 描述：美食收藏界面
 */

public class FavorActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    ToolBar toolBar;
    @BindView(R.id.lv_favor)
    ListView listView;
    private FavorAdapter adapter;
    private List<GoodFoodFavor> data = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        SystemBarUtils.setTranslucentStatus(this, Color.parseColor("#ffce3d3a"));
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_favor);
        ButterKnife.bind(this);
        toolBar.setLeftBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        data = DataSupport.order("favorTime desc").find(GoodFoodFavor.class);
        adapter = new FavorAdapter(this, data);
        listView.setAdapter(adapter);
    }

    //列表项删除响应
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void itemDelete(ItemDelete itemDelete) {
        adapter.delItem(itemDelete.getPosition());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}