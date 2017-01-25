package com.example.disklrucachedemo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.disklrucachedemo.R;
import com.example.disklrucachedemo.bean.NewsBean;
import com.example.disklrucachedemo.utils.ThreadUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wangyuhang on 2017/1/25.
 */

public class NewsAdapter extends BaseAdapter {

    private Context context;
    private List<NewsBean> list;

    public NewsAdapter(Context context, List<NewsBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_news, null);
        }

        // 得到一个ViewHolder
        viewHolder = ViewHolder.getViewHolder(convertView);

        //先加载默认图片 防止有的没有图
        viewHolder.iconImage.setImageResource(R.mipmap.ic_launcher);

        String iconUrl = list.get(position).newsIconUrl;

        //当前位置的ImageView与图片的URL绑定
        viewHolder.iconImage.setTag(iconUrl);

        //再加载联网图

        //第一种方式 通过子线程设置
        new ThreadUtil().showImageByThread(viewHolder.iconImage, iconUrl);

        viewHolder.titleText.setText(list.get(position).newsTitle);
        viewHolder.contentText.setText(list.get(position).newsContent);

        return convertView;
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
