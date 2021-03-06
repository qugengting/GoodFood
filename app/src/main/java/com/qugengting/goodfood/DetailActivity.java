package com.qugengting.goodfood;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.common.library.util.DateUtils;
import com.common.library.util.Utils;
import com.common.library.util.systembar.SystemBarUtils;
import com.common.library.widget.GridViewForScrollView;
import com.common.library.widget.ListViewForScrollView;
import com.common.library.widget.ToolBar;
import com.qugengting.goodfood.adapter.MaterialAdapter;
import com.qugengting.goodfood.adapter.StepAdapter;
import com.qugengting.goodfood.bean.GoodFoodFavor;
import com.qugengting.goodfood.eventbus.ItemDelete;

import org.greenrobot.eventbus.EventBus;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DetailActivity extends BaseActivity {

    protected ProgressDialog dialog;
    private String mUrl;
    private int position;
    @BindView(R.id.toolbar)
    ToolBar toolBar;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_meishi)
    ImageView imageView;
    @BindView(R.id.gv_shicai)
    GridViewForScrollView gvMaterial;
    @BindView(R.id.lv_zuofa)
    ListViewForScrollView lvStep;
    @BindView(R.id.ib_favor)
    ImageButton btnFavor;
    private static final String TAG = DetailActivity.class.getSimpleName();
    private static final String OK = "ok";
    private static final String ERROR = "error";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36";
    private String mTitle;
    private String mImgUrl;
    private List<String> mListMaterial = new ArrayList<>();
    private List<String> adapterListMaterial = new ArrayList<>();
    private List<String> mListSteps = new ArrayList<>();
    private List<String> adatperListSteps = new ArrayList<>();
    private MaterialAdapter materialAdapter;
    private StepAdapter adapter;
    private boolean isFavor = false;
    private GoodFoodFavor favor;
    private String mHtmlContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        SystemBarUtils.setTranslucentStatus(this, Color.parseColor("#ffce3d3a"));
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        toolBar.setLeftBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        materialAdapter = new MaterialAdapter(this, adapterListMaterial);
        gvMaterial.setAdapter(materialAdapter);
        adapter = new StepAdapter(this, adatperListSteps);
        lvStep.setAdapter(adapter);
        mUrl = getIntent().getStringExtra("detail");
        position = getIntent().getIntExtra("position", -1);
        favor = DataSupport.where("url = ?", mUrl).findFirst(GoodFoodFavor.class);
        if (favor != null) {
            isFavor = true;
            btnFavor.setBackgroundResource(R.drawable.favor_btn_selector_selected);
            btnFavor.setImageResource(R.drawable.ic_favor_selected);
        }
        start();
    }

    @OnClick(R.id.ib_favor)
    public void onFavor() {
        if (isFavor) {//取消收藏
            btnFavor.setBackgroundResource(R.drawable.favor_btn_selector_normal);
            btnFavor.setImageResource(R.drawable.ic_favor_unselected);
            favor.delete();
            isFavor = false;
            Utils.makeText(this, "取消收藏成功");
        } else {
            //添加收藏
            if (favor == null) {
                favor = new GoodFoodFavor();
            }
            btnFavor.setBackgroundResource(R.drawable.favor_btn_selector_selected);
            btnFavor.setImageResource(R.drawable.ic_favor_selected);
            favor.setHtml(mHtmlContent);
            favor.setUrl(mUrl);
            favor.setImageUrl(mImgUrl);
            String date = DateUtils.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss");
            favor.setFavorDate(date);
            favor.setTitle(mTitle);
            favor.setFavorTime(System.currentTimeMillis());
            favor.save();
            isFavor = true;
            Utils.makeText(this, "添加收藏成功");
        }
    }


    public void start() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("加载中...");
        dialog.show();
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                String result = getDatas();
                subscriber.onNext(result);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        dialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog.dismiss();
                        Utils.makeText(DetailActivity.this, "网络错误");
                    }

                    @Override
                    public void onNext(String s) {
                        if (s.equals(OK)) {
                            tvTitle.setText(mTitle);
                            Glide.with(DetailActivity.this).load(mImgUrl).into(imageView);
                            adapterListMaterial.addAll(mListMaterial);
                            adatperListSteps.addAll(mListSteps);
                            materialAdapter.notifyDataSetChanged();
                            adapter.notifyDataSetChanged();
//                            UIHelper.setListViewHeightBasedOnChildren(lvStep);
                        } else {

                        }
                    }
                });
    }

    //tvTitle:#御寒美食#干锅菜花pic:http://i3.meishichina.com/attachment/recipe/2018/01/04/20180104151505223691213.jpg@!c320
    //url:http://home.meishichina.com/space-9541848.html
    //burden:原料：菜花、五花肉、尖椒、葱、姜、干红辣椒、花椒、郫县豆瓣酱、生抽、料酒、糖、植物油。
    private String getDatas() {
        try {
            long a = System.currentTimeMillis();
            Connection connection;
            Document doc;
            if (favor != null) {
                //本地解析
                doc = Jsoup.parse(favor.getHtml());
                mHtmlContent = favor.getHtml();
            } else {
                //网络获取
                connection = Jsoup.connect(mUrl);
                connection.header("User-Agent", USER_AGENT);
                doc = connection.get();
                mHtmlContent = doc.html();
            }
            //“椒麻鸡”和它对应的图片都在<div class="pic">中
            mTitle = doc.select("h1.recipe_De_title").first().select("a").first().attr("title");
            mImgUrl = doc.getElementById("recipe_De_imgBox").select("a").select("img").attr("src");
            //食材列表
            Elements materialElements = doc.select("div.recipeCategory_sub_R").first().select("ul").first().select("li");
            for (int i = 0; i < materialElements.size(); i++) {
                String name = materialElements.get(i).select("span.category_s1").select("b").first().text();
                String hint = materialElements.get(i).select("span.category_s2").first().text();
                mListMaterial.add(name + "=====" + hint);
            }
            //步骤列表
            Elements stepElements = doc.select("div.recipeStep").first().select("ul").first().select("li");
            for (int i = 0; i < stepElements.size(); i++) {
                String explain = stepElements.get(i).select("div.recipeStep_word").html().split("\\<\\/div\\>")[1];
                String image = stepElements.get(i).select("div.recipeStep_img").select("img").attr("src");
                mListSteps.add(explain + "=====" + image);
            }
            long b = System.currentTimeMillis();
            Log.e(TAG, "所用时间：" + (b - a) + "毫秒");
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
        return OK;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //由收藏界面进来的话，如果取消收藏需要通知收藏页界面刷新
        if (!isFavor && position != -1) {
            EventBus.getDefault().post(new ItemDelete(position));
        }
    }
}
