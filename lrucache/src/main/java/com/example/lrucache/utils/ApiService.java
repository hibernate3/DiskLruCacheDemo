package com.example.lrucache.utils;

import com.example.lrucache.bean.JsonBean;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by wangyuhang on 2017/1/27.
 */

public interface ApiService {
    @GET("api/teacher")
    Observable<JsonBean> getJsonInfo (@Query("type") int type, @Query("num") int num);
}
