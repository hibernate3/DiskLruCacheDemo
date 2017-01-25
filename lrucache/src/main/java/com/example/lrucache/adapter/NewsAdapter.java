package com.example.lrucache.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lrucache.R;
import com.example.lrucache.bean.NewsBean;
import com.example.lrucache.utils.LruCacheUtil;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wangyuhang on 2017/1/25.
 */

public class NewsAdapter extends BaseAdapter implements AbsListView.OnScrollListener {

    private Context context;
    private List<NewsBean> list;

    private LruCacheUtil lruCacheUtil;

    private int mStart, mEnd;//滑动的起始位置
    public static String[] urls; //用来保存当前获取到的所有图片的Url地址

    //是否是第一次进入
    private boolean mFirstIn;

    public NewsAdapter(Context context, List<NewsBean> list, ListView lv) {
        this.context = context;
        this.list = list;

        lruCacheUtil = new LruCacheUtil(lv);

        //存入url地址
        urls = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            urls[i] = list.get(i).newsIconUrl;
        }

        mFirstIn = true;

        //注册监听事件
        lv.setOnScrollListener(this);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;

        if (view == null) {
            view = View.inflate(context, R.layout.item_news, null);
        }

        // 得到一个ViewHolder
        viewHolder = ViewHolder.getViewHolder(view);

        //先加载默认图片 防止有的没有图
        viewHolder.iconImage.setImageResource(R.mipmap.ic_launcher);

        String iconUrl = list.get(i).newsIconUrl;

        //当前位置的ImageView与图片的URL绑定
        viewHolder.iconImage.setTag(iconUrl);

        //再加载联网图

        //第二种方式 通过异步任务方式设置 且利用LruCache存储到内存缓存中
        lruCacheUtil.showImageByAsyncTask(viewHolder.iconImage, iconUrl);

        viewHolder.titleText.setText(list.get(i).newsTitle);
        viewHolder.contentText.setText(list.get(i).newsContent);

        return view;
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE) {
            //加载可见项
            lruCacheUtil.loadImages(mStart, mEnd);
        } else {
            //停止加载任务
            lruCacheUtil.cancelAllTask();
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mStart = firstVisibleItem;
        mEnd = firstVisibleItem + visibleItemCount;

        //如果是第一次进入 且可见item大于0 预加载
        if (mFirstIn && visibleItemCount > 0) {
            lruCacheUtil.loadImages(mStart, mEnd);
            mFirstIn = false;
        }
    }

    static class ViewHolder {
        @BindView(R.id.iv_icon)
        ImageView iconImage;
        @BindView(R.id.tv_title)
        TextView titleText;
        @BindView(R.id.tv_content)
        TextView contentText;

        public ViewHolder(View convertView) {
            ButterKnife.bind(this, convertView);
        }

        // 得到一个ViewHolder
        public static ViewHolder getViewHolder(View convertView) {
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            if (viewHolder == null) {
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            }
            return viewHolder;
        }
    }

}
