package com.qugengting.goodfood;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.Nullable;

import com.common.library.util.BitmapUtils;
import com.common.library.util.CameraUtils;
import com.common.library.util.DeviceUtils;
import com.common.library.widget.popmenu.dialog.ShareDialog;
import com.google.android.material.tabs.TabLayout;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.common.gif.GifActivity;
import com.common.library.image.ImageChooserActivity;
import com.common.library.fragment.BaseFragment;
import com.common.library.fragment.BaseFragmentAdapter;
import com.common.library.fragment.CustomViewPager;
import com.common.library.image.LargeImageViewActivity;
import com.common.library.permission.MPermissionsActivity;
import com.common.library.util.DateUtils;
import com.common.library.util.SharedPreferencesUtils;
import com.common.library.util.ToastUtil;
import com.common.library.util.UriUtils;
import com.common.library.util.Utils;
import com.common.library.webview.BaseWebActivity;
import com.common.library.widget.CustomRadioGroup;
import com.common.library.widget.ToolBar;
import com.common.library.widget.popmenu.dialog.ActionSheetDialog;
import com.common.library.zxing.android.CaptureActivity;
import com.qugengting.filedownload.DownloadHelper;
import com.qugengting.goodfood.fragment.SeasonHotFragment;
import com.qugengting.goodfood.fragment.WeekSelectionFragment;

import org.jaaksi.pickerview.picker.BasePicker;
import org.jaaksi.pickerview.picker.MixedTimePicker;
import org.jaaksi.pickerview.picker.TimePicker;
import org.jaaksi.pickerview.widget.PickerView;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    private ShareDialog mShareDialog;


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
//        VasSonicFragment vasSonicFragment = new VasSonicFragment();
//        vasSonicFragment.setFragmentTitle("唯品会");
        fragmentList.add(weekSelectionFragment);
        fragmentList.add(seasonHotFragment);
//        fragmentList.add(vasSonicFragment);
        adapter = new BaseFragmentAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
        adapter.notifyDataSetChanged();
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(viewPager);
        initRadioGroup();
        //经测试，三种Transform比较有用BlurTransformation——模糊化，GrayscaleTransformation——灰化和CropCircleTransformation——圆角化
        Glide.with(this).load(R.drawable.girl).bitmapTransform(new BlurTransformation(this, 25)).into(ivLogo);
        initDurationPicker();
        initStartTimePicker();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                TextView textView = (TextView) LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_textview, null);
                textView.setTextSize(18);
                textView.setTextColor(Color.parseColor("#ffce3d3a"));
                textView.setText(tab.getText());
                tab.setCustomView(textView);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.setCustomView(null);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        tabLayout.getTabAt(1).select();
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

    @OnClick(R.id.tv_download)
    public void filedownload() {
        requestPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUESCODE_PERMISSION_DOWNLOAD);
    }

    private void download() {
        DownloadHelper downloadHelper = new DownloadHelper(new Callback());
        List<String> urlList = new ArrayList<>();
        List<String> nameList = new ArrayList<>();
        urlList.add("http://img6.3lian.com/c23/desk4/07/01/d/09.jpg");
        nameList.add("bit.jpg");
        downloadHelper.startMultiTask(urlList, nameList);
    }

    public class Callback implements DownloadHelper.DownloadCallback {

        @Override
        public void success() {
            //异步线程，需要切换到主线程
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    ToastUtil.showToast(MainActivity.this, "下载成功");
                }
            });

        }

        @Override
        public void failed(final String msg) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    ToastUtil.showToast(MainActivity.this, msg);
                }
            });
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

    @OnClick(R.id.tv_qrcode)
    public void qrcodeScan() {
        requestPermission(new String[]{Manifest.permission.CAMERA}, REQUESCODE_PERMISSION_CAMERA);
    }

    @OnClick(R.id.tv_show_bottom)
    public void showBottomPop() {
        new ActionSheetDialog(this)
                .builder()
                .setCancelable(true)
                .setOnCancelClickListener(new ActionSheetDialog.OnCancelClickListener() {
                    @Override
                    public void onClick() {

                    }
                })
                .setCanceledOnTouchOutside(false)
                .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                //参考：https://blog.csdn.net/qugengting/article/details/87805856
                                requestPermission(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUESCODE_PERMISSION_TAKE_PHOTO);
                            }
                        })

                .addSheetItem("相册", ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {

                            }
                        }).show();
    }

    @OnClick(R.id.tv_share)
    public void shareTo() {
        if (mShareDialog == null) {
            mShareDialog = new ShareDialog(this);
        }
        mShareDialog.setCanceledOnTouchOutside(true);
        mShareDialog.show();

        TextView tvCancle = mShareDialog.getView().findViewById(R.id.tv_share_cancle);
        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShareDialog.cancel();
            }
        });
        LinearLayout shareIJOMOO = mShareDialog.getView().findViewById(R.id.layout_share_ijomoo);
        shareIJOMOO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DeviceUtils.isInstallApp(getApplicationContext(), "cn.jomoo.imobile")) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "this is test");
                    sendIntent.setType("text/plain");
                    sendIntent.setPackage("cn.jomoo.imobile");
                    startActivity(sendIntent);
                    mShareDialog.dismiss();
                } else {
                    ToastUtil.showToast(MainActivity.this, "应用未安装");
                }

            }
        });
        LinearLayout shareSMS = mShareDialog.getView().findViewById(R.id.layout_share_sms);
        shareSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"));
                intent.putExtra("sms_body", "this is test");
                startActivity(intent);
                mShareDialog.dismiss();
            }
        });
        LinearLayout shareLianjie = mShareDialog.getView().findViewById(R.id.layout_share_clipboard);
        shareLianjie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取剪贴板管理器：
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("Label", "this is test");
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                ToastUtil.showToast(MainActivity.this, "内容已复制");
                mShareDialog.dismiss();
            }
        });
    }

    private TimePicker mTimePicker;
    private MixedTimePicker mStartPicker;
    private String mConfDurationHour = "1";
    private String mConfDurationMinute = "0";
    private DateFormat dateFormatHourMinute = new SimpleDateFormat("HH:mm");

    private void initDurationPicker() {
        int type = 0;
        // 设置type
        type = type | TimePicker.TYPE_HOUR;
        type = type | TimePicker.TYPE_MINUTE;
        mTimePicker = new TimePicker.Builder(this, type, new TimePicker.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(TimePicker picker, Date date) {
                String result = dateFormatHourMinute.format(date);
                String showStr;
                String[] strings = result.split(":");
                mConfDurationHour = strings[0];
                int h = Integer.valueOf(mConfDurationHour);//去掉“01”前面的0
                mConfDurationHour = String.valueOf(h);
                mConfDurationMinute = strings[1];
                if (mConfDurationMinute.equals("00")) {
                    showStr = mConfDurationHour + "小时";
                } else {
                    showStr = mConfDurationHour + "小时 " + mConfDurationMinute + "分钟";
                }
                ToastUtil.showToast(MainActivity.this, showStr);
            }
        })
                // 设置时间区间
                .setRangDate(1526361240000L, 1893563460000L)
                .setTimeMinuteOffset(15)//设置分钟间隔,此处15表示只显示0，15，30，45
                // 设置选中时间
                //.setSelectedDate()
                // 设置pickerview样式
                .setInterceptor(new BasePicker.Interceptor() {
                    @Override
                    public void intercept(PickerView pickerView) {
                        pickerView.setVisibleItemCount(3);
                        // 将时分设置为循环的
                        int type = (int) pickerView.getTag();
                        if (type == TimePicker.TYPE_HOUR || type == TimePicker.TYPE_MINUTE) {
                            pickerView.setIsCirculation(true);
                        }
                    }
                })
                // 设置 Formatter
                .setFormatter(new TimePicker.TimeNumFormatter()).create();
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), 4, 20, 1, 0);
        mTimePicker.setSelectedDate(calendar.getTimeInMillis());
    }

    private void initStartTimePicker() {
        int type = 0;
        type = type | MixedTimePicker.TYPE_DATE;
        type = type | MixedTimePicker.TYPE_TIME;
        // 2018/5/15 13:14:00 - 2030/1/2 13:51:0
        mStartPicker = new MixedTimePicker.Builder(this, type, new MixedTimePicker.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(MixedTimePicker picker, Date date) {
                String stime = DateUtils.date2Str(date, DateUtils.FORMAT_YMDHM);
                ToastUtil.showToast(MainActivity.this, stime);
            }
        })
                .setTimeMinuteOffset(15)
                // 设置时间区间 2018/2/5 3:14:0 - 2020/1/2 22:51:0
                .setRangDate(System.currentTimeMillis() + 60000 * 15, System.currentTimeMillis() + 86400000L * 60)//从当前时间后15分钟到两个月后
                .create();
    }

    @OnClick(R.id.tv_time_selector)
    public void selectTime() {
        mStartPicker.setSelectedDate(System.currentTimeMillis());
        mStartPicker.show();
    }

    @OnClick(R.id.tv_duration_selector)
    public void selectDuration() {
        mTimePicker.show();
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
        } else if (requestCode == REQUESCODE_PERMISSION_CAMERA) {
            gotoQRCodeScan();
        } else if (requestCode == REQUESCODE_PERMISSION_TAKE_PHOTO) {
            takePhoto();
        } else if(requestCode == REQUESCODE_PERMISSION_DOWNLOAD) {
            download();
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

    private void gotoQRCodeScan() {
        Intent intent = new Intent(this, CaptureActivity.class);
        startActivity(intent);
    }

    private void takePhoto() {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri imageUri = CameraUtils.getOutputMediaFileUri(this);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        //Android7.0添加临时权限标记，此步千万别忘了
        openCameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        startActivityForResult(openCameraIntent, CODE_TAKE_PHOTO);
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
            } else if (requestCode == CODE_TAKE_PHOTO) {
                ToastUtil.showToast(this, "拍照成功");
                Bitmap originBitmap = CameraUtils.getBitmapWithRightRotation(CameraUtils.sCameraPath);
                Bitmap bitmap = BitmapUtils.imageZoom(originBitmap, 600);
                final String path = getExternalCacheDir() + File.separator;
                final String name = "goodfood" + ".jpg";
                File file = new File(path);
                File targetFile = new File(path + name);
                boolean result = BitmapUtils.saveBitmap(bitmap, file, targetFile);
                Log.e(TAG, "图片保存是否成功：" + result + " - " + path + name);
                File file1 = new File(CameraUtils.sCameraPath);
                file1.delete();
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
    private static final int CODE_TAKE_PHOTO = 1014;//系统相机
    private static final int DURATION_PER_IMAGE_SHOW = 2000;
    private static final int WHAT_SHOW_IMAGE = 1013;
    private static final int REQUESCODE_PERMISSION_MANAGE_DOCUMENTS_I = 0x0005;
    private static final int REQUESCODE_PERMISSION_MANAGE_DOCUMENTS_II = 0x0006;
    private static final int REQUESCODE_PERMISSION_CAMERA = 0x0007;
    private static final int REQUESCODE_PERMISSION_TAKE_PHOTO = 0x0008;
    private static final int REQUESCODE_PERMISSION_DOWNLOAD = 0x0009;
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
        if (mShareDialog != null) {
            mShareDialog.cancel();
            mShareDialog = null;
        }
    }
}
