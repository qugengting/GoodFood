package com.qugengting.goodfood;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.common.library.adapter.AbstractAdapter;
import com.common.library.adapter.BaseHolder;

import java.util.List;

import butterknife.BindView;

public class MaterialAdapter extends AbstractAdapter<String, MaterialAdapter.MaterialHolder>  {

    public MaterialAdapter(Context context, List<String> data) {
        super(context, data);
    }

    @Override
    protected MaterialHolder getHolder(View v) {
        return new MaterialHolder(v);
    }

    @Override
    protected int getView() {
        return R.layout.item_listview_material;
    }

    @Override
    protected void bindEvent(MaterialHolder holder, int position) {
        String oriStr = data.get(position);
        final String[] strings = oriStr.split("=====");
        holder.name.setText(strings[0]);
        holder.decription.setText(strings[1]);
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


    protected class MaterialHolder extends BaseHolder {
        @BindView(R.id.tv_material_name)
        TextView name;
        @BindView(R.id.tv_material_description)
        TextView decription;

        public MaterialHolder(View view) {
            super(view);
        }
    }

}
