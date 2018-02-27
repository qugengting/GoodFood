package com.common.gif.adapter;


import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.common.gif.R;
import com.common.gif.bean.BitmapIndexWrapper;

import java.util.List;

/**
 * Created by xuruibin on 2018/2/26.
 */

public class GifFramebyFrameAdapter extends BaseQuickAdapter<BitmapIndexWrapper, BaseViewHolder> {

    public Context context;

    public GifFramebyFrameAdapter(Context context, List<BitmapIndexWrapper> data) {
        super(R.layout.item_recyclerview_frame_by_frame, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, BitmapIndexWrapper data) {
//        ImageView imageView = baseViewHolder.getView(R.id.iv_frame_by_frame);
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        data.getBitmap().compress(Bitmap.CompressFormat.PNG, 100, baos);
//        byte[] bytes = baos.toByteArray();
//        Glide.with(context).load(bytes).into(imageView);
        baseViewHolder.setImageBitmap(R.id.iv_frame_by_frame, data.getBitmap());
        baseViewHolder.setText(R.id.tv_frame_index, data.getIndex() + "");
    }
}
