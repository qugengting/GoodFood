package com.qugengting.goodfood;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.bumptech.glide.Glide;
import com.common.gif.GifActivity;
import com.common.library.image.ImageChooserActivity;
import com.common.library.fragment.BaseFragment;
import com.common.library.fragment.BaseFragmentAdapter;
import com.common.library.fragment.CustomViewPager;
import com.common.library.image.LargeImageViewActivity;
import com.common.library.permission.MPermissionsActivity;
import com.common.library.util.SharedPreferencesUtils;
import com.common.library.util.UriUtils;
import com.common.library.util.Utils;
import com.common.library.webview.AgentWebFragment;
import com.common.library.webview.BaseWebActivity;
import com.common.library.webview.WebActivity;
import com.common.library.webview.vassonic.VasSonicFragment;
import com.common.library.widget.CustomRadioGroup;
import com.common.library.widget.ToolBar;
import com.qugengting.goodfood.fragment.SeasonHotFragment;
import com.qugengting.goodfood.fragment.WeekSelectionFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.BlurTransformation;
import me.iwf.photopicker.PhotoPicker;

/**
 * Created by xuruibin on 2018/2/1.
 * 描述：
 */

public class MainActivity extends MPermissionsActivity {
    @BindView(R.id.toolbar)
    ToolBar toolBar;
    @BindView(R.id.rg_image)
    CustomRadioGroup radioGroup;
    @BindView(R.id.rb_local)
    RadioButton rbLocal;
    @BindView(R.id.rb_net)
    RadioButton rbNet;
    @BindView(R.id.layout_drawer)
    DrawerLayout layoutDrawer;
    @BindView(R.id.tab_main)
    TabLayout tabLayout;
    @BindView(R.id.vp_content)
    CustomViewPager viewPager;
    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    private BaseFragmentAdapter adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
//        SystemBarUtils.setTranslucentStatus(this, Color.parseColor("#ffce3d3a"));
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        toolBar.setLeftBtnSrc(R.drawable.titlebar_menu);
        toolBar.setLeftBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutDrawer.openDrawer(GravityCompat.START);
            }
        });
        List<BaseFragment> fragmentList = new ArrayList<>();
        WeekSelectionFragment weekSelectionFragment = new WeekSelectionFragment();
        weekSelectionFragment.setFragmentTitle("一周精选");
        SeasonHotFragment seasonHotFragment = new SeasonHotFragment();
        seasonHotFragment.setFragmentTitle("当季最热");
//        AgentWebFragment agentWebFragment = new AgentWebFragment();
//        agentWebFragment.setFragmentTitle("唯品会");
        VasSonicFragment vasSonicFragment = new VasSonicFragment();
        vasSonicFragment.setFragmentTitle("唯品会");
        fragmentList.add(weekSelectionFragment);
        fragmentList.add(seasonHotFragment);
        fragmentList.add(vasSonicFragment);
        adapter = new BaseFragmentAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
        adapter.notifyDataSetChanged();
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(viewPager);
        initRadioGroup();
        //经测试，三种Transform比较有用BlurTransformation——模糊化，GrayscaleTransformation——灰化和CropCircleTransformation——圆角化
        Glide.with(this).load(R.drawable.girl).bitmapTransform(new BlurTransformation(this, 25)).into(ivLogo);
    }

    @Override
    public void onBackPressed() {
        if (layoutDrawer.isDrawerOpen(GravityCompat.START)) {
            layoutDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private int clickTime = 0;
    private long time;
    private boolean isTest = true;

    @OnClick(R.id.iv_logo)
    public void getWelfare() {
        if (isTest) {
            if (clickTime == 0) {
                time = System.currentTimeMillis();
            }
            clickTime += 1;
            if (clickTime == 6) {
                long n = System.currentTimeMillis() - time;
                if (n < 1500) {
                    clickTime = 0;
//                    Intent intent = new Intent(this, ImageTitleActivity.class);
                    Intent intent = new Intent(this, BaseWebActivity.class);
                    intent.putExtra("qu", "https://www.2247bb.com");
                    startActivity(intent);
                } else {
                    clickTime = 0;
                    time = System.currentTimeMillis();
                }
            }
        } else {
//            Intent intent = new Intent(this, ImageTitleActivity.class);
//            startActivity(intent);
            requestPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUESCODE_PERMISSION_MANAGE_DOCUMENTS_II);
        }

    }

    @OnClick(R.id.tv_test)
    public void test() {
        Intent intent = new Intent(this, TestActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tv_webview)
    public void jingdong() {
        Intent intent = new Intent(this, BaseWebActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tv_gif_scan)
    public void lookGif() {
        Intent intent = new Intent(this, GifActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tv_gif_local)
    public void localGif() {
        requestPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUESCODE_PERMISSION_MANAGE_DOCUMENTS_I);
    }

    @Override
    public void permissionSuccess(int requestCode) {
        super.permissionSuccess(requestCode);
        if (requestCode == REQUESCODE_PERMISSION_MANAGE_DOCUMENTS_I) {
            goToSystemImageChooseActivity();
        } else if (requestCode == REQUESCODE_PERMISSION_MANAGE_DOCUMENTS_II) {
            boolean isChooseCustom = (boolean) SharedPreferencesUtils.getData(this, SharePreferentsConstants.IMAGE_CHOOSE_KEY, false);
            if (isChooseCustom) {
                goToCustomImageChooseActivity();
            } else {
                goToPhotoPicker();
            }
        }

    }

    private void goToSystemImageChooseActivity() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, CODE_CHOOSE_IMAGE);
    }

    private void goToCustomImageChooseActivity() {
        Intent intent = new Intent(this, ImageChooserActivity.class);
        intent.putExtra("maxCount", 5);
        startActivityForResult(intent, CODE_CHOOSE_PHOTO);
    }

    private void goToPhotoPicker() {
        PhotoPicker.builder()
                .setPhotoCount(9)
                .setShowCamera(true)
                .setShowGif(true)
                .setPreviewEnabled(false)
                .start(MainActivity.this, PhotoPicker.REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CODE_CHOOSE_IMAGE) {
                mGifFilePath = UriUtils.getFileAbsolutePath(this, data.getData());
                Log.i(TAG, "gif file path : " + mGifFilePath);
                if (mGifFilePath.endsWith(".gif")) {
                    mGifUri = data.getData();
                    Intent intent = new Intent(this, GifActivity.class);
                    intent.putExtra("isLocalGif", true);
                    intent.putExtra("gifUri", mGifUri);
                    startActivity(intent);
                } else {
                    Utils.makeText(this, "只能选择gif图片");
                }
            } else if (requestCode == CODE_CHOOSE_PHOTO) {
                listPhotos = data.getStringArrayListExtra("path");
                if (handler == null) {
                    handler = new MyHandler();
                }
                handler.sendEmptyMessage(WHAT_SHOW_IMAGE);
            } else if (requestCode == PhotoPicker.REQUEST_CODE) {
                listPhotos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                if (handler == null) {
                    handler = new MyHandler();
                }
                handler.sendEmptyMessage(WHAT_SHOW_IMAGE);
            }
        }
    }

    private class MyHandler extends Handler {
        int count = 0;

        @Override
        public void handleMessage(Message msg) {
            if (count < listPhotos.size()) {
                String imagePath = listPhotos.get(count);
                Glide.with(MainActivity.this).load(imagePath).into(ivLogo);
                count++;
                sendEmptyMessageDelayed(WHAT_SHOW_IMAGE, DURATION_PER_IMAGE_SHOW);
            } else if (count == listPhotos.size()) {
                Glide.with(MainActivity.this).load(R.drawable.girl).into(ivLogo);
                count = 0;
            }
        }
    }

    private static final String TAG = MainActivity.class.getSimpleName();
    private MyHandler handler;
    private static final int CODE_CHOOSE_IMAGE = 1011;//系统图片浏览
    private static final int CODE_CHOOSE_PHOTO = 1012;//自定义图片浏览
    private static final int DURATION_PER_IMAGE_SHOW = 2000;
    private static final int WHAT_SHOW_IMAGE = 1013;
    private static final int REQUESCODE_PERMISSION_MANAGE_DOCUMENTS_I = 0x0005;
    private static final int REQUESCODE_PERMISSION_MANAGE_DOCUMENTS_II = 0x0006;
    private String mGifFilePath;
    private Uri mGifUri;
    private List<String> listPhotos;

    private static final String LOCAL_IMAGE = "本地图片/自定义";
    private static final String NET_IMAGE = "网络图片/第三方";

    private void initRadioGroup() {
        rbLocal.setText(LOCAL_IMAGE);
        rbNet.setText(NET_IMAGE);
        boolean isNetRes = (boolean) SharedPreferencesUtils.getData(this, SharePreferentsConstants.IMAGE_RES_KEY, false);
        if (isNetRes) {
            radioGroup.check(R.id.rb_net);
        } else {
            radioGroup.check(R.id.rb_local);
            SharedPreferencesUtils.putData(MainActivity.this, SharePreferentsConstants.IMAGE_CHOOSE_KEY, true);
        }
        radioGroup.setHorizontalSpacing(12);
        radioGroup.setVerticalSpacing(8);
        radioGroup.setListener(new CustomRadioGroup.OnclickListener() {
            @Override
            public void OnText(String text) {
                if (text.equals(LOCAL_IMAGE)) {
                    SharedPreferencesUtils.putData(MainActivity.this, SharePreferentsConstants.IMAGE_RES_KEY, false);
                    SharedPreferencesUtils.putData(MainActivity.this, SharePreferentsConstants.IMAGE_CHOOSE_KEY, true);
                } else {
                    SharedPreferencesUtils.putData(MainActivity.this, SharePreferentsConstants.IMAGE_RES_KEY, true);
                    SharedPreferencesUtils.putData(MainActivity.this, SharePreferentsConstants.IMAGE_CHOOSE_KEY, false);
                }

            }
        });
    }

    @OnClick(R.id.tv_largeimage)
    public void glideTest() {
        Intent intent = new Intent(this, LargeImageViewActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tv_collection)
    public void goToFavorActivity() {
        Intent intent = new Intent(this, FavorActivity.class);
        startActivity(intent);
    }

    /**
     * 按返回键不退出应用。
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (layoutDrawer.isDrawerOpen(GravityCompat.START)) {
                layoutDrawer.closeDrawer(GravityCompat.START);
            } else {
                // 不退出程序，进入后台
                moveTaskToBack(true);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
    }
}
