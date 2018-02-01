package com.qugengting.goodfood.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.common.library.adapter.AbstractAdapter;
import com.common.library.adapter.BaseHolder;
import com.qugengting.goodfood.R;

import java.util.List;

import butterknife.BindView;

public class StepAdapter extends AbstractAdapter<String, StepAdapter.MaterialHolder>  {

    public StepAdapter(Context context, List<String> data) {
        super(context, data);
    }

    @Override
    public MaterialHolder getHolder(View v) {
        return new MaterialHolder(v);
    }

    @Override
    public int getView() {
        return R.layout.item_listview_step;
    }

    @Override
    public void bindEvent(MaterialHolder holder, int position) {
        String oriStr = data.get(position);
        final String[] strings = oriStr.split("=====");
        holder.decription.setText("步骤" + (position + 1) + "、" + strings[0]);
        Glide.with(context).load(strings[1]).into(holder.image);
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


    public class MaterialHolder extends BaseHolder {
        @BindView(R.id.iv_step)
        ImageView image;
        @BindView(R.id.tv_step_description)
        TextView decription;

        public MaterialHolder(View view) {
            super(view);
        }
    }

}
