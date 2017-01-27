package com.example.lrucache.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.lrucache.R;
import com.example.lrucache.adapter.NewsAdapter;
import com.example.lrucache.bean.JsonResponseBean;
import com.example.lrucache.bean.NewsBean;
import com.example.lrucache.utils.ApiService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.mainListView)
    ListView mainListView;

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
        mSubscription = apiService.getJsonInfo(4, 30)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<JsonResponseBean>() {
                    @Override
                    public void onCompleted() {
                        Log.i("myLog", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("myLog", e.getMessage().toString());
                    }

                    @Override
                    public void onNext(JsonResponseBean jsonResponseBean) {
                        List<NewsBean> newsBeans = new ArrayList<NewsBean>();

                        List<JsonResponseBean.DataBean> dataBeans = jsonResponseBean.getData();

                        for (int i=0; i<dataBeans.size(); i++) {
                            NewsBean newsBean = new NewsBean();

                            newsBean.setNewsTitle(dataBeans.get(i).getName());
                            newsBean.setNewsContent(dataBeans.get(i).getDescription());
                            newsBean.setNewsIconUrl(dataBeans.get(i).getPicSmall());

                            newsBeans.add(newsBean);
                        }

                        NewsAdapter newsAdapter = new NewsAdapter(mainContext, newsBeans, mainListView);
                        mainListView.setAdapter(newsAdapter);
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
