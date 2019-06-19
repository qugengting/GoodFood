package com.common.library.image;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.TextView;

import com.common.library.R;
import com.common.library.util.Utils;
import com.common.library.util.systembar.SystemBarUtils;
import com.common.library.widget.ToolBar;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import top.zibin.luban.Luban;

/**
 * 图片选择页
 *
 * @author PWY
 */
public class ImageGridActivity extends AppCompatActivity implements OnClickListener {

    // ArrayList<Entity> dataList;
    private List<ImageChooserGridAdapter.ImageItem> dataList;
    private GridView gridView;
    private ImageChooserGridAdapter adapter;
    private Button btnSubmit;
    private TextView subCountView;
    private CheckBox mapCheckBox;
    private int maxCount;
    private int count;
    private boolean checkedState = false;
    private String path;
    private ArrayList<String> listSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        SystemBarUtils.setTranslucentStatus(this, Color.parseColor("#ffce3d3a"));
        setContentView(R.layout.activity_image_chooser_grid);

        path = getIntent().getStringExtra("path");
        maxCount = getIntent().getIntExtra("maxCount", 1);

        initView();

    }

    private void initView() {
        listSelect = new ArrayList<String>();
        gridView = findViewById(R.id.gridview);
        btnSubmit = findViewById(R.id.submit_btn);
        btnSubmit.setOnClickListener(this);
        mapCheckBox = (CheckBox) findViewById(R.id.mapCheckBox);
        mapCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    checkedState = true;
                } else {
                    checkedState = false;
                }
            }
        });
        if (getIntent().getBooleanExtra("access", false)) {
            mapCheckBox.setVisibility(View.INVISIBLE);
            checkedState = true;
        }
        if (maxCount <= 1) {
            findViewById(R.id.bottom_linear).setVisibility(View.GONE);
        }
        ToolBar bar = findViewById(R.id.toolbar);
        bar.setTitle(new File(path).getName());
        bar.setRightTextClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_FIRST_USER);
                finish();
            }
        });
        bar.setLeftBtnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        dataList = new ArrayList<>();
        File root = new File(path);
        File[] files = root.listFiles();
        for (int i = 0; null != files && i < files.length; i++) {
            if (!FilePathUtils.isPicture(files[i])) {
                continue;
            }
            ImageChooserGridAdapter.ImageItem item = new ImageChooserGridAdapter.ImageItem();
            item.imagePath = files[i].getAbsolutePath();
            item.lastModify = files[i].lastModified();
            dataList.add(0, item);
        }
        Collections.sort(dataList, new Comparator<ImageChooserGridAdapter.ImageItem>() {
            @Override
            public int compare(ImageChooserGridAdapter.ImageItem lhs, ImageChooserGridAdapter.ImageItem rhs) {
                return Long.compare(rhs.lastModify, lhs.lastModify);
            }
        });
        adapter = new ImageChooserGridAdapter(ImageGridActivity.this, dataList);

        gridView.setAdapter(adapter);

        subCountView = (TextView) findViewById(R.id.submit_btn_cover);
        subCountView.setVisibility(View.GONE);

        gridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                ImageChooserGridAdapter.ImageItem item = dataList.get(position);

                if (maxCount <= 1) {
                    listSelect = new ArrayList<String>();
                    listSelect.add(item.imagePath);
                    Intent intent = new Intent();
                    intent.putStringArrayListExtra("path", listSelect);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                    return;
                }

                if (item.isSelected) {
                    item.isSelected = false;
                    count--;
                    subCountView.setText(count + "");
                    if (0 == count) {
                        subCountView.setVisibility(View.GONE);
                        btnSubmit.setBackgroundColor(Color.GRAY);
                    }
                    listSelect.remove(item.imagePath);
                } else {
                    if (count < maxCount) {
                        item.isSelected = true;
                        count++;

                        subCountView.setText(count + "");
                        subCountView.setVisibility(View.VISIBLE);
                        btnSubmit.setBackgroundResource(R.drawable.btn_submit);

                        listSelect.add(item.imagePath);
                    } else {
                        Utils.makeText(ImageGridActivity.this, "最多选择" + maxCount + "张图片");
                    }
                }
                adapter.notifyDataSetChanged();
            }

        });

    }

    @SuppressWarnings("unchecked")
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (R.id.submit_btn == id) {
            if (count > 0) {
                if (checkedState) {
                    Intent intent = new Intent();
                    intent.putExtra("states", true);
                    intent.putStringArrayListExtra("path", listSelect);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } else {
                    new MyTask(this).execute(listSelect);
                }
            } else {
                Utils.makeText(this, "请选择图片");
            }

        }
    }

    private static class MyTask extends AsyncTask<List<String>, Void, ArrayList<String>> {

        private WeakReference<ImageGridActivity> activityReference;
        ProgressDialog dlg;
        // only retain a weak reference to the activity
        MyTask(ImageGridActivity context) {
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute() {
            ImageGridActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing()) return;
            dlg = new ProgressDialog(activity);
            dlg.show();
        }

        @Override
        protected ArrayList<String> doInBackground(List<String>... params) {
            ImageGridActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing()) return null;
            try {
                List<File> files =  Luban.with(activity)
                        .load(params[0])
//                        .setTargetDir(activity.getApplicationContext().getCacheDir() + "/.image/thumb/")// 设置压缩后文件存储位置
                        .get();
                ArrayList<String> filePaths = new ArrayList<>();
                for (File file : files) {
                    filePaths.add(file.getAbsolutePath());
                }
                return filePaths;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            // get a reference to the activity if it is still there
            ImageGridActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing()) return;
            // modify the activity's UI
            dlg.dismiss();
            Intent intent = new Intent();
            intent.putExtra("states", false);
            intent.putStringArrayListExtra("path", result);
            activity.setResult(Activity.RESULT_OK, intent);
            activity.finish();
            super.onPostExecute(result);
        }
    }
}
