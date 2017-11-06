package com.example.leipe.architecture.ui.wx.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.leipe.architecture.R;
import com.example.leipe.architecture.app.Router;
import com.example.leipe.architecture.base.BaseFragment;
import com.example.leipe.architecture.base.http.NetWorkObserver;
import com.example.leipe.architecture.model.http.result.WXHttpResponse;
import com.example.leipe.architecture.model.bean.WXListBean;
import com.example.leipe.architecture.ui.wx.adapter.WXAdapter;
import com.example.leipe.architecture.viewmodel.wx.WXViewModel;

import java.util.List;

/**
 * 微信热门前50条
 * Created by leipe on 2017/6/27.
 */
@Route(path = Router.WX_HOT)
public class WXHotFragment extends BaseFragment {
    final String TAG = this.getClass().getSimpleName();
    final int layout = R.layout.list_fragment;
    TextView tv_loading;
    RecyclerView rl_list;
    Toolbar toolbar;

    WXAdapter mAdapter;
    private WXViewModel viewModel;
    private boolean isLoading = true;

    public static WXHotFragment newInstance() {
        WXHotFragment fragment = new WXHotFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        tv_loading = view.findViewById(R.id.loading_tv);
        rl_list = view.findViewById(R.id.products_list);
        toolbar = view.findViewById(R.id.toolbar);
        setToolbar(toolbar,"微信热门");
        isLoading();
        WXViewModel.Factory factory = new WXViewModel.Factory();
        viewModel = ViewModelProviders.of(this, factory).get(WXViewModel.class);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        mAdapter = new WXAdapter(_mActivity, null);
        rl_list.setLayoutManager(new LinearLayoutManager(_mActivity));
        rl_list.setAdapter(mAdapter);

        viewModel.getWxDataCall()
                .observe(this, new NetWorkObserver<WXHttpResponse<List<WXListBean>>, List<WXListBean>>(_mActivity) {
                    @Override
                    public void onData(List<WXListBean> wxListBeen) {
                        isLoading = false;
                        isLoading();
                        mAdapter.refreshData(wxListBeen);
                    }
                });


        // 异步,还没有活跃的LiveData
        Log.e(TAG + "==onLazyInitView", String.valueOf(viewModel.getWxDataCall().hasActiveObservers()));
    }

    private void isLoading() {
        if (isLoading) {
            tv_loading.setVisibility(View.VISIBLE);
            rl_list.setVisibility(View.GONE);
        } else {
            rl_list.setVisibility(View.VISIBLE);
            tv_loading.setVisibility(View.GONE);
        }
    }


    @Override
    protected int getLayoutId() {
        return layout;
    }


    // 以下生命周期用于测试是否有活跃的LiveData
    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG + "==onStart", String.valueOf(viewModel.getWxDataCall().hasActiveObservers()));
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(TAG + "==onStop", String.valueOf(viewModel.getWxDataCall().hasActiveObservers()));
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG + "==onResume", String.valueOf(viewModel.getWxDataCall().hasActiveObservers()));
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG + "==onPause", String.valueOf(viewModel.getWxDataCall().hasActiveObservers()));
    }
}
