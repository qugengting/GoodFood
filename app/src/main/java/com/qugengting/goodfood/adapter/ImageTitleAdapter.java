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
import com.qugengting.goodfood.ImageActivity;
import com.qugengting.goodfood.R;
import com.qugengting.goodfood.eventbus.ImageEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;

public class ImageTitleAdapter extends AbstractAdapter<String, ImageTitleAdapter.ImageTitleHolder> {

    public ImageTitleAdapter(Context context, List<String> data) {
        super(context, data);
    }

    @Override
    public ImageTitleHolder getHolder(View v) {
        return new ImageTitleHolder(v);
    }

    @Override
    public int getView() {
        return R.layout.item_listview_imagetitles;
    }

    @Override
    public void bindEvent(ImageTitleHolder holder, final int position) {
        holder.titleName.setText(data.get(position));
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().postSticky(new ImageEvent(data.get(position)));
                Intent intent = new Intent(context, ImageActivity.class);
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


    public class ImageTitleHolder extends BaseHolder {
        @BindView(R.id.tv_image_title)
        TextView titleName;
        @BindView(R.id.view_line)
        View line;
        @BindView(R.id.layout_image_item)
        LinearLayout layout;

        public ImageTitleHolder(View view) {
            super(view);
        }
    }

}
