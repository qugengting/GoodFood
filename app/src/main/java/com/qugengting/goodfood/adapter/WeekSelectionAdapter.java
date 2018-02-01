package com.qugengting.goodfood.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.common.library.adapter.AbstractAdapter;
import com.common.library.adapter.BaseHolder;
import com.qugengting.goodfood.DetailActivity;
import com.qugengting.goodfood.R;

import java.util.List;

import butterknife.BindView;

public class WeekSelectionAdapter extends AbstractAdapter<String, WeekSelectionAdapter.WeekSelectHolder>  {

    public WeekSelectionAdapter(Context context, List<String> data) {
        super(context, data);
    }

    @Override
    public WeekSelectHolder getHolder(View v) {
        return new WeekSelectHolder(v);
    }

    @Override
    public int getView() {
        return R.layout.item_listview_weekselection;
    }

    @Override
    public void bindEvent(WeekSelectHolder holder, int position) {
        String oriStr = data.get(position);
        final String[] strings = oriStr.split("=====");
        holder.index.setText(context.getResources().getString(R.string.comma, (position + 1)));
        holder.title.setText(strings[0]);
        Glide.with(context).load(strings[1]).into(holder.imageView);
        holder.burden.setText(strings[2]);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("detail", strings[3]);
                context.startActivity(intent);
            }
        });
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


    public class WeekSelectHolder extends BaseHolder {
        @BindView(R.id.tv_index)
        TextView index;
        @BindView(R.id.tv_title)
        TextView title;
        @BindView(R.id.tv_burden)
        TextView burden;
        @BindView(R.id.iv_image)
        ImageView imageView;
        @BindView(R.id.layout_meishi)
        LinearLayout layout;

        public WeekSelectHolder(View view) {
            super(view);
        }
    }

}
