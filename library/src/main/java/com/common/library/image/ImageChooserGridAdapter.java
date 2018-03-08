package com.common.library.image;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.common.library.R;

import java.util.List;

public class ImageChooserGridAdapter extends BaseAdapter {

    Context ct;
    List<ImageItem> dataList;

    public ImageChooserGridAdapter(Context ct, List<ImageItem> list) {
        this.ct = ct;
        dataList = list;

    }

    @Override
    public int getCount() {
        int count = 0;
        if (dataList != null) {
            count = dataList.size();
        }
        return count;
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class Holder {
        private ImageView iv;
        private ImageView selected;
    }

    @SuppressLint("NewApi")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = View.inflate(ct, R.layout.item_image_chooser_grid, null);
            holder.iv = (ImageView) convertView.findViewById(R.id.image);
            holder.selected = (ImageView) convertView.findViewById(R.id.isselected);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        final ImageItem item = dataList.get(position);
        Glide.with(ct).load(item.imagePath).into(holder.iv);
        if (item.isSelected) {
            holder.selected.setImageResource(R.drawable.ic_list_checked);
        } else {
            holder.selected.setImageBitmap(null);
        }
        return convertView;
    }

    public static class ImageItem {
        public String imagePath;
        public long lastModify;
        public boolean isSelected = false;
    }
}
