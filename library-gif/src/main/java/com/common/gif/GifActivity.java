package com.common.gif;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.common.gif.adapter.GifFramebyFrameAdapter;
import com.common.gif.bean.BitmapIndexWrapper;
import com.common.library.util.BitmapUtils;
import com.common.library.util.DateUtils;
import com.common.library.util.PxUtil;
import com.common.library.util.Utils;
import com.common.library.util.systembar.SystemBarUtils;
import com.common.library.widget.ToolBar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by xuruibin on 2018/2/26.
 * 描述：gif图片加载框架https://github.com/koral--/android-gif-drawable
 */

public class GifActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = GifActivity.class.getSimpleName();
    private ToolBar toolBar;
    private ViewPager mViewPager;
    private LinearLayout mLayoutImageEdit;
    private TextView mTextViewFramebyFrame;
    private TextView mTextViewPlay;
    private TextView mTextViewSave;
    private RecyclerView mRecyclerViewFramebyFrame;

    private List<View> mViewList = new ArrayList<>();
    private PagerAdapter mPagerAdapter;
    private GifFramebyFrameAdapter mGifFramebyFrameAdapter;
    private boolean isLocalGif;
    private int[] resIds = new int[]{R.drawable.liutaoloudian, R.drawable.shuili, R.drawable.xiashuiloudian};
    private List<BitmapIndexWrapper> mBitmapWrapperList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        SystemBarUtils.setTranslucentStatus(this, Color.parseColor("#000000"));
        setContentView(R.layout.activity_gif);
        toolBar = findViewById(R.id.toolbar);
        mViewPager = findViewById(R.id.vp_images);
        mLayoutImageEdit = findViewById(R.id.layout_gif_edit);
        mLayoutImageEdit.setOnClickListener(this);
        mTextViewFramebyFrame = findViewById(R.id.tv_frame_by_frame);
        mTextViewFramebyFrame.setOnClickListener(this);
        mTextViewPlay = findViewById(R.id.tv_frame_play);
        mTextViewPlay.setOnClickListener(this);
        mTextViewSave = findViewById(R.id.tv_save_image);
        mTextViewSave.setOnClickListener(this);
        mRecyclerViewFramebyFrame = findViewById(R.id.rv_frame_by_frame);
        toolBar.setLeftBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mPagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return mViewList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(mViewList.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = mViewList.get(position);
                container.addView(view);
                return view;
            }
        };
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                String title = (position + 1) + " / " + mViewList.size();
                toolBar.setTitle(title);
                if (mRecyclerViewFramebyFrame.getVisibility() == View.VISIBLE) {
                    initFrameByFrame(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        initImages();
    }

    private void initImages() {
        LayoutInflater inflater = LayoutInflater.from(GifActivity.this);
        Intent intent = getIntent();
        isLocalGif = intent.getBooleanExtra("isLocalGif", false);
        if (isLocalGif) {
            Uri gifUri = intent.getParcelableExtra("gifUri");
            View view = inflater.inflate(R.layout.view_gif_image, null);
            mViewList.add(view);
            mPagerAdapter.notifyDataSetChanged();
            GifImageView imageView = view.findViewById(R.id.gif_imageview);
            imageView.setImageURI(gifUri);
            imageView.setOnClickListener(this);
        } else {
            for (int i = 0; i < resIds.length; i++) {
                View view = inflater.inflate(R.layout.view_gif_image, null);
                mViewList.add(view);
            }
            mPagerAdapter.notifyDataSetChanged();
            for (int i = 0; i < mViewList.size(); i++) {
                View view = mViewList.get(i);
                GifImageView imageView = view.findViewById(R.id.gif_imageview);
                imageView.setImageResource(resIds[i]);
                imageView.setOnClickListener(this);
            }
        }

        toolBar.setTitle("1 / " + mViewList.size());
    }

    public void hideLayout() {
        mLayoutImageEdit.setVisibility(View.GONE);
    }

    public void playFrameByFrame() {
        mLayoutImageEdit.setVisibility(View.GONE);
        mRecyclerViewFramebyFrame.setVisibility(View.VISIBLE);
        int currentItem = mViewPager.getCurrentItem();
        initFrameByFrame(currentItem);
    }

    private void initFrameByFrame(int index) {
        View view = mViewList.get(index);
        GifImageView imageView = view.findViewById(R.id.gif_imageview);
        mBitmapWrapperList.clear();
        final GifDrawable gifDrawable = (GifDrawable) imageView.getDrawable();
        gifDrawable.stop();
        for (int i = 0; i < gifDrawable.getNumberOfFrames(); i++) {
            Bitmap bitmap = gifDrawable.seekToFrameAndGet(i);
            int height = bitmap.getHeight();
            int targetHeight = PxUtil.dip2px(this, 80f);
            int targetWidth = bitmap.getWidth() * targetHeight / height;
            //图片压缩
            Bitmap bm = Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, true);
            bitmap.recycle();
            BitmapIndexWrapper bitmapIndexWrapper = new BitmapIndexWrapper();
            bitmapIndexWrapper.setBitmap(bm);
            bitmapIndexWrapper.setIndex(i + 1);
            mBitmapWrapperList.add(bitmapIndexWrapper);
        }

        mGifFramebyFrameAdapter = new GifFramebyFrameAdapter(this, mBitmapWrapperList);
        mRecyclerViewFramebyFrame.setAdapter(mGifFramebyFrameAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerViewFramebyFrame.setLayoutManager(linearLayoutManager);
        mRecyclerViewFramebyFrame.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
                gifDrawable.seekToFrame(position);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.gif_imageview) {
            if (mLayoutImageEdit.getVisibility() != View.VISIBLE) {
                mLayoutImageEdit.setVisibility(View.VISIBLE);
            } else {
                mLayoutImageEdit.setVisibility(View.GONE);
                mRecyclerViewFramebyFrame.setVisibility(View.GONE);
            }
        } else if (v.getId() == R.id.layout_gif_edit) {
            hideLayout();
        } else if (v.getId() == R.id.tv_frame_by_frame) {
            playFrameByFrame();
        } else if (v.getId() == R.id.tv_frame_play) {
            play();
        } else if (v.getId() == R.id.tv_save_image) {
            saveImage();
        }
    }

    public void play() {
        int currentItem = mViewPager.getCurrentItem();
        View view = mViewList.get(currentItem);
        GifImageView imageView = view.findViewById(R.id.gif_imageview);
        final GifDrawable gifDrawable = (GifDrawable) imageView.getDrawable();
        if (gifDrawable.isPlaying()) {
            gifDrawable.stop();
            Drawable drawable = getResources().getDrawable(R.drawable.ic_frame_continue);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mTextViewPlay.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
            mTextViewPlay.setText(getResources().getString(R.string.start_play));
        } else {
            gifDrawable.start();
            Drawable drawable = getResources().getDrawable(R.drawable.ic_frame_pause);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mTextViewPlay.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
            mTextViewPlay.setText(getResources().getString(R.string.stop_play));
        }
    }

    public void saveImage() {
        String name = DateUtils.longToString(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss");
//        File file = new File(getExternalCacheDir() + File.separator + name + ".jpg");
        String path = getExternalCacheDir() + File.separator;
        File file = new File(path);
        File targetFile = new File(path + name + ".jpg");
        int currentItem = mViewPager.getCurrentItem();
        View view = mViewList.get(currentItem);
        GifImageView imageView = view.findViewById(R.id.gif_imageview);
        final GifDrawable gifDrawable = (GifDrawable) imageView.getDrawable();
        Bitmap bitmap = gifDrawable.getCurrentFrame();
        boolean result = BitmapUtils.saveBitmap(BitmapUtils.getRoundedCornerBitmap(bitmap, 90), file, targetFile);
        if (result) {
            Utils.makeText(this, getResources().getString(R.string.save_image_success));
        } else {
            Utils.makeText(this, getResources().getString(R.string.save_image_fail));
        }
    }

    @Override
    public void onBackPressed() {
        if (mRecyclerViewFramebyFrame.getVisibility() == View.VISIBLE) {
            mRecyclerViewFramebyFrame.setVisibility(View.GONE);
            mLayoutImageEdit.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }
    }
}
