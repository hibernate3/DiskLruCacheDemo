package com.example.disklrucache.activity;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.disklrucache.R;
import com.example.disklrucache.adapter.NewsAdapter;
import com.example.disklrucache.bean.JsonResponseBean;
import com.example.disklrucache.bean.NewsBean;
import com.example.disklrucache.utils.ApiService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.mainListView)
    ListView mainListView;

    private static final String TAG = "myLog";

    private Context mainContext = MainActivity.this;
    private String ENDPOINT = "http://www.imooc.com";

    private Subscription mSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        mSubscription = apiService.getJsonInfo(4, 50)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonResponseBean>() {
                               @Override
                               public void call(JsonResponseBean jsonResponseBean) {
                                   List<NewsBean> newsBeans = new ArrayList<NewsBean>();

                                   List<JsonResponseBean.DataBean> dataBeans = jsonResponseBean.getData();

                                   for (int i = 0; i < dataBeans.size(); i++) {
                                       NewsBean newsBean = new NewsBean();

                                       newsBean.setNewsTitle(dataBeans.get(i).getName());
                                       newsBean.setNewsContent(dataBeans.get(i).getDescription());
                                       newsBean.setNewsIconUrl(dataBeans.get(i).getPicSmall());

                                       newsBeans.add(newsBean);
                                   }

                                   NewsAdapter newsAdapter = new NewsAdapter(mainContext, newsBeans, mainListView);
                                   mainListView.setAdapter(newsAdapter);
                               }
                           },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                Log.e(TAG, throwable.toString());
                            }
                        },
                        new Action0() {
                            @Override
                            public void call() {
                                Log.i(TAG, "get JSON info complete");
                            }
                        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mSubscription != null && mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }
}
