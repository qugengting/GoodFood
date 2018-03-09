package com.qugengting.goodfood.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.common.library.adapter.AbstractAdapter;
import com.common.library.adapter.BaseHolder;
import com.common.library.util.Utils;
import com.qugengting.goodfood.DetailActivity;
import com.qugengting.goodfood.R;
import com.qugengting.goodfood.bean.GoodFoodFavor;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;

public class FavorAdapter extends AbstractAdapter<GoodFoodFavor, FavorAdapter.FavorHolder> {

    public FavorAdapter(Context context, List<GoodFoodFavor> data) {
        super(context, data);
    }

    @Override
    public FavorHolder getHolder(View v) {
        return new FavorHolder(v);
    }

    @Override
    public int getView() {
        return R.layout.item_listview_favor;
    }

    @Override
    public void bindEvent(FavorHolder holder, final int position) {
        final GoodFoodFavor favor = data.get(position);
        holder.title.setText((position + 1) + "、" + favor.getTitle());
        holder.date.setText("收藏于：" + favor.getFavorDate());
        Glide.with(context).load(favor.getImageUrl()).centerCrop().into(holder.image);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("detail", favor.getUrl());
                intent.putExtra("position", position);
                context.startActivity(intent);
            }
        });
        holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showDelDialog(position);
                return true;
            }
        });
    }

    /**
     * 提示是否删除
     */
    private void showDelDialog(final int position) {
        Utils.showDialog(context, "提示", "是否删除", "是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                delItem(position);
            }
        }, "否", null, 1);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public class FavorHolder extends BaseHolder {
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.tv_title)
        TextView title;
        @BindView(R.id.tv_date)
        TextView date;
        @BindView(R.id.layout_favor)
        RelativeLayout layout;

        public FavorHolder(View view) {
            super(view);
        }
    }

}
