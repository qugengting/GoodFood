package com.common.library.image;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.common.library.R;
import com.common.library.util.systembar.SystemBarUtils;
import com.common.library.widget.ToolBar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 图片选择列表
 * 
 * @author PWY
 *
 */
public class ImageChooserActivity extends AppCompatActivity {
	private List<ImageChooserAdapter.FolderItem> dataList;
	private ListView buckt_list;
	private ImageChooserAdapter adapter;// 自定义的适配器
	private List<String> listPhotos = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
		SystemBarUtils.setTranslucentStatus(this, Color.parseColor("#ffce3d3a"));
		setContentView(R.layout.activity_image_chooser);

		initData();
		initView();
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		dataList = new ArrayList<>();
		for (String sd : FilePathUtils.getIntance(this).getExtSDCardPaths()) {
			checkAdd(sd + "/相机");
			checkAdd(sd + "/截屏");
			checkAdd(sd + "/DCIM");
			checkAdd(sd + "/Pictures");
			checkAdd(sd + "/tencent/MicroMsg/WeiXin");
			checkAdd(sd + "/tencent/QQ_Images");
		}
	}
	
	private void checkAdd(String path) {
		File root = new File(path);
		if (!root.isDirectory() || root.getName().startsWith(".")) {
			return;
		}

		String imagePath = null;
		long lastModify = 0;
		int imageCount = 0;
		File[] files = root.listFiles();
		for (int i = 0; null != files && i < files.length; i ++) {
			if (files[i].isDirectory()) {
				checkAdd(files[i].getAbsolutePath());
			} else if (FilePathUtils.isPicture(files[i])) {
				if (files[i].lastModified() > lastModify) {
					imagePath = files[i].getAbsolutePath();
					lastModify = files[i].lastModified();
				}
				imageCount++;
			}
		}
		if (imageCount > 0) {
			dataList.add(new ImageChooserAdapter.FolderItem(root.getAbsolutePath(), imagePath, imageCount));
		}
	}

	/**
	 * 初始化view视图
	 */
	private void initView() {
		ToolBar toolBar = findViewById(R.id.toolbar);
		toolBar.setRightTextClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		buckt_list = (ListView) findViewById(R.id.buckt_list);
		adapter = new ImageChooserAdapter(this, dataList);
		buckt_list.setAdapter(adapter);
		buckt_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ImageChooserAdapter.FolderItem item = dataList.get(position);
				Intent intent = new Intent(ImageChooserActivity.this, ImageGridActivity.class);
				intent.putExtra("access", getIntent().getBooleanExtra("access", false));
				intent.putExtra("path", item.folderPath);
				intent.putExtra("maxCount", getIntent().getIntExtra("maxCount", 1));
				startActivityForResult(intent, 1);
			}

		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(Activity.RESULT_OK == resultCode)  {
			setResult(Activity.RESULT_OK, data);
			listPhotos = data.getStringArrayListExtra("path");
			finish();
        } else if(Activity.RESULT_FIRST_USER == resultCode) {
        	finish();
        }
		super.onActivityResult(requestCode, resultCode, data);
	}
	
}
