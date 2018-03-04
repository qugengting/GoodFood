package com.common.library.file;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.common.library.R;

import java.io.File;
import java.util.List;

public class ImageChooserAdapter extends BaseAdapter {
	private Context ct;
	private List<FolderItem> dataList;

	public ImageChooserAdapter(Context ct, List<FolderItem> list) {
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
	public Object getItem(int arg0) {
		return dataList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	class Holder {
		private ImageView iv;
		private TextView name;
		private TextView count;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		Holder holder;
		if (convertView == null) {
			holder = new Holder();
			convertView = View.inflate(ct, R.layout.item_image_chooser, null);
			holder.iv = (ImageView) convertView.findViewById(R.id.image);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.count = (TextView) convertView.findViewById(R.id.count);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		FolderItem item = dataList.get(arg0);
		Glide.with(ct).load(item.path).placeholder(R.drawable.img_default_pic).into(holder.iv);
		holder.name.setText(new File(item.folderPath).getName());
		holder.count.setText(item.count + "");

		return convertView;
	}

	public static class FolderItem {
		public String folderPath;
		String path;
		int count;

		public FolderItem(String folderPath, String path, int count) {
			this.folderPath = folderPath;
			this.path = path;
			this.count = count;
		}
	}


}
