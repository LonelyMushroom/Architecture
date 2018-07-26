package com.minister.architecture.repository;

import com.lmroom.baselib.http.RetrofitUtil;
import com.minister.architecture.app.MyApp;
import com.minister.architecture.model.bean.GankItemBean;
import com.minister.architecture.model.http.GankApi;
import com.minister.architecture.model.http.result.GankHttpResponse;
import com.minister.architecture.util.ApiException;
import com.minister.architecture.util.NetWorkUtils;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by 被咯苏州 on 2017/9/4.
 */
public class GankRepository {

    private static class GankHolder {
        private static final GankRepository INSTALL = new GankRepository();
    }

    private GankApi gankApi;

    private GankRepository() {
        this.gankApi = RetrofitUtil.getInstall().getRetrofit(GankApi.HOST)
                .create(GankApi.class);
    }

    /**
     * Singleton install
     * @return WeatherRepository
     */
    public static final GankRepository getInstall() {
        return GankHolder.INSTALL;
    }

    /**
     * 获取干货信息
     * @param tech 搜索条件
     * @param num 每页数量
     * @param page 页码
     */
    public Flowable<GankHttpResponse<List<GankItemBean>>> getTechList(String tech, int num, int page) {
        return gankApi.getTechList(tech, num, page);
    }

    /**
     * 获取美女图片
     * @param num 每页数量
     * @param page 页码
     */
    public Flowable<GankHttpResponse<List<GankItemBean>>> getGirlList(int num, int page) {
        if (NetWorkUtils.isNetworkConnected(MyApp.getInstance())) {
            return gankApi.getGirlList(num, page);
        }else {
            return Flowable.error(new ApiException("没有网络哟"));
        }

    }

    /**
     * 随机妹子图片
     * @param num 数量
     */
    public Flowable<GankHttpResponse<List<GankItemBean>>> getRandomGirl(int num){
        return gankApi.getRandomGirl(num);
    }
}
