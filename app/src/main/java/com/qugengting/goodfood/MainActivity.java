package com.qugengting.goodfood;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.common.library.pullableview.PullToRefreshLayout;
import com.common.library.pullableview.PullableListView;
import com.common.library.util.Utils;
import com.qugengting.goodfood.adapter.WeekSelectionAdapter;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.lv_meishi)
    PullableListView listView;
    @BindView(R.id.refresh_view)
    PullToRefreshLayout ptrl;
    protected ProgressDialog dialog;
    private WeekSelectionAdapter adapter;
    private static final String TAG = MainActivity.class.getSimpleName();
    private List<String> list = new ArrayList<>();
    private static final String OK = "ok";
    private static final String ERROR = "error";
    private boolean isFirstIn = true;
    private static final String DEFAULT_MAIN_URL = "http://home.meishichina.com/show-top-type-recipe.html";
    private String mNextPageUrl = "";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        listView.setCanPullDown(false);
        adapter = new WeekSelectionAdapter(this, list);
        listView.setAdapter(adapter);
        ptrl.setOnRefreshListener(new MyRefreshListener());
        start(null);
    }

    public class MyRefreshListener implements PullToRefreshLayout.OnRefreshListener {

        @Override
        public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
            //start(pullToRefreshLayout);
        }

        @Override
        public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
            start(pullToRefreshLayout);
        }
    }

    public void start(final PullToRefreshLayout pullToRefreshLayout) {
        if (isFirstIn) {
            dialog = new ProgressDialog(this);
            dialog.setMessage("加载中...");
            dialog.show();
        }
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
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        if (pullToRefreshLayout != null) {
                            pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        if (pullToRefreshLayout != null) {
                            pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                        }
                        Utils.makeText(MainActivity.this, "网络错误");
                    }

                    @Override
                    public void onNext(String s) {
                        adapter.notifyDataSetChanged();
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
            if (isFirstIn) {
                connection = Jsoup.connect(DEFAULT_MAIN_URL);
            } else {
                connection = Jsoup.connect(mNextPageUrl);
            }
            connection.header("User-Agent", USER_AGENT);
            doc = connection.get();
            //“椒麻鸡”和它对应的图片都在<div class="pic">中
            Elements titleAndPics = doc.select("div.pic");
            //原料在<p class="subcontent">中
            Elements burdens = doc.select("p.subcontent");
            //所需链接在<div class="detail">中的<a>标签里面
            Elements detail = doc.select("div.detail");
            for (int i = 0; i < titleAndPics.size(); i++) {
                //使用Element.select(String selector)查找元素，使用Node.attr(String key)方法取得一个属性的值
                String title = titleAndPics.get(i).select("a").attr("title");
                String pic = titleAndPics.get(i).select("a").select("img").attr("data-src");
                String url = detail.get(i).select("a").first().attr("href");
                //对于一个元素中的文本，可以使用Element.text()方法
                String burden = burdens.get(i).text();
                Log.e(TAG, "tvTitle:" + title);
                Log.e(TAG, "pic:" + pic);
                Log.e(TAG, "url:" + url);
                Log.e(TAG, "burden:" + burden);
                list.add(title + "=====" + pic + "=====" + burden + "=====" + url);
            }
            Elements pageElements = doc.select("div.ui-page-inner");
            Element nowPageElement = pageElements.select("a.now_page").get(0);
            Element nextPageElement = nowPageElement.nextElementSibling();
            mNextPageUrl = nextPageElement.attr("href");
            isFirstIn = false;
            long b = System.currentTimeMillis();
            Log.e(TAG, "所用时间：" + (b - a) + "毫秒");
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return ERROR;
        }
        return OK;
    }
}
