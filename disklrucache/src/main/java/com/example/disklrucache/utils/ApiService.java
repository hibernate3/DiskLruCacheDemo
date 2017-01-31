package com.example.disklrucache.utils;

import com.example.disklrucache.bean.JsonResponseBean;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by wangyuhang on 2017/1/31.
 */

public interface ApiService {
    @GET("api/teacher")
    Observable<JsonResponseBean> getJsonInfo(@Query("type") int type, @Query("num") int num);
}
