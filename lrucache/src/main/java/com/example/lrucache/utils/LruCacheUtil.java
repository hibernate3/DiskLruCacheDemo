package com.example.lrucache.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.lrucache.R;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by wangyuhang on 2017/1/25.
 */

public class LruCacheUtil {
    private static final String TAG = "myLog";

    //LRU缓存
    private LruCache<String, Bitmap> mCache;

    private ListView mListView;

    public LruCacheUtil(ListView mListView) {
        this.mListView = mListView;

        //返回Java虚拟机将尝试使用的最大内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();

        //指定缓存大小
        int cacheSize = maxMemory / 4;

        mCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                //Bitmap的实际大小 注意单位与maxMemory一致
                return value.getByteCount();

                //也可以这样返回 结果是一样的
//                return value.getRowBytes() * value.getHeight();
            }
        };
    }

    /**
     * 通过异步任务的方式加载数据
     *
     * @param iv  图片的控件
     * @param url 图片的URL
     */
    public void showImageByAsyncTask(ImageView iv, final String url) {
        //从缓存中取出图片
        Bitmap bitmap = getBitmapFromCache(url);

        //如果缓存中没有，先设为默认图片
        if (bitmap == null) {
            iv.setImageResource(R.mipmap.ic_launcher);

            Observable observable =  Observable.just(url)
                    .map(new Func1<String, Bitmap>() {
                        @Override
                        public Bitmap call(String s) {
                            Log.i(TAG, s);
                            Bitmap bitmap = getBitmapFromURL(url);

                            //保存到缓存中
                            if (bitmap != null) {
                                putBitmapToCache(url, bitmap);
                            }

                            return bitmap;
                        }
                    }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

            observable.subscribe(new Subscriber<Bitmap>() {
                @Override
                public void onCompleted() {
                    Log.i(TAG, "completed");
                }

                @Override
                public void onError(Throwable e) {
                    Log.e(TAG, "onError: " + e.getMessage());
                }

                @Override
                public void onNext(Bitmap bitmap) {
                    //只有当前的ImageView所对应的URL的图片是一致的,才会设置图片
                    ImageView imageView = (ImageView) mListView.findViewWithTag(url);
                    if (imageView != null && bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                    }
                }
            });
        } else {
            //如果缓存中有 直接设置
            iv.setImageBitmap(bitmap);
        }
    }

    /**
     * 将一个URL转换成bitmap对象
     *
     * @param urlStr 图片的URL
     * @return
     */
    public Bitmap getBitmapFromURL(String urlStr) {
        Bitmap bitmap;
        InputStream is = null;

        try {
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            is = new BufferedInputStream(connection.getInputStream());
            bitmap = BitmapFactory.decodeStream(is);

            connection.disconnect();
            return bitmap;
        } catch (java.io.IOException e) {
            Log.e(TAG, "Exception: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /*--------------------------------LruCache的实现-----------------------------------------*/

    /**
     * 将Bitmap存入缓存
     *
     * @param url    Bitmap对象的key
     * @param bitmap 对象的key
     */
    public void putBitmapToCache(String url, Bitmap bitmap) {
        //如果缓存中没有
        if (getBitmapFromCache(url) == null) {
            //保存到缓存中
            mCache.put(url, bitmap);
        }
    }

    /**
     * 从缓存中获取Bitmap对象
     *
     * @param url Bitmap对象的key
     * @return 缓存中Bitmap对象
     */
    public Bitmap getBitmapFromCache(String url) {
        return mCache.get(url);
    }

    /*--------------------------------LruCache的实现-----------------------------------------*/
}
