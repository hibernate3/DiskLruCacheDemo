package com.example.disklrucache.activity;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.disklrucache.R;
import com.example.disklrucache.adapter.NewsAdapter;
import com.example.disklrucache.bean.NewsBean;
import com.example.disklrucache.utils.GetJsonUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.mainListView)
    ListView mainListView;

    private Context mainContext = MainActivity.this;
    private String url = "http://www.imooc.com/api/teacher?type=4&num=30";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //开启异步任务
        GetJsonTask getJsonTask = new GetJsonTask();
        getJsonTask.execute(url);
    }

    class GetJsonTask extends AsyncTask<String, Void, List<NewsBean>> {
        @Override
        protected List<NewsBean> doInBackground(String... strings) {
            return GetJsonUtil.getJson(strings[0]);
        }

        @Override
        protected void onPostExecute(List<NewsBean> newsBeen) {
            super.onPostExecute(newsBeen);

            NewsAdapter newsAdapter = new NewsAdapter(mainContext, newsBeen, mainListView);
            mainListView.setAdapter(newsAdapter);
        }
    }
}
