package com.example.mlk.refreshviewdemo.view;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.mlk.refreshviewdemo.R;

import java.util.List;

import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * 作者：mlk on 2018/6/3 17:16
 */
public class RefreshRecy extends FrameLayout {

    private PtrFrameLayout ptrFrameLayout;
    private RecyclerView recyclerView;
    private BaseQuickAdapter adapter;
    private Handler handler = new Handler();
    private int pageSize;
    private boolean isLoadMore;

    public RefreshRecy(@NonNull Context context) {
        this(context,null);
    }

    public RefreshRecy(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public RefreshRecy(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        View view = View.inflate(context, R.layout.refresh_recy, null);
        ptrFrameLayout = view.findViewById(R.id.ptrFramelayout);
        recyclerView = view.findViewById(R.id.recyclerView);
        ptrFrameLayout.setEnabled(false);//默认不开启下拉刷新
        addView(view);
    }

    public void setAdapter(BaseQuickAdapter adapter){
        recyclerView.setAdapter(adapter);
        this.adapter = adapter;
    }

    public BaseQuickAdapter getAdapter() {
        return adapter;
    }

    private void initRefreshHead() {
        // header
//        final StoreHouseHeader header = new StoreHouseHeader(getContext());
//        header.setPadding(0, 30, 0, 30);
//        header.initWithString("Alibaba");
//        header.setTextColor(R.color.colorAccent);
//        ptrFrameLayout.addPtrUIHandler(header);
//        ptrFrameLayout.setHeaderView(header);

        PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(getContext());
        ptrFrameLayout.addPtrUIHandler(header);
        ptrFrameLayout.setHeaderView(header);
        ptrFrameLayout.setEnabled(true);
    }

    private void initListener() {
        //下拉刷新
        ptrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                frame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(onRefreshListener != null){
                            onRefreshListener.onRefresh();
                        }
                        ptrFrameLayout.refreshComplete();
                    }
                }, 1800);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                // 默认实现，根据实际情况做改动
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });

        //上拉加载
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(onRefreshListener != null){
                            onRefreshListener.onLoadMore();
                        }
                    }
                },1800);
            }
        },recyclerView);

    }

    public void setLinearLayoutManager(int orientation){
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),orientation,false));
    }

    public void setGridLayoutManager(int spanCount,int orientation){
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),spanCount,orientation,false));
    }

    public void setStaggeredGridLayoutManager(int spanCount,int orientation){
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(spanCount,orientation));
    }

    public void closeLoadMord(){
        if(adapter != null) adapter.setOnLoadMoreListener(null,recyclerView);
    }

    public void loadMoreData(List data,List newList){
        if(newList == null || newList.size() <= 0){
            adapter.notifyDataSetChanged();
            adapter.loadMoreEnd();
            isLoadMore = false;
            return;
        }
        isLoadMore = true;
        data.addAll(newList);
        adapter.notifyDataSetChanged();
        adapter.loadMoreComplete();
    }

    public void downRefreshData(List data,List newList){
        isLoadMore = true;
        data.clear();
        if(newList != null || newList.size() > 0){
            data.addAll(newList);
//            adapter.loadMoreComplete();
            adapter.notifyDataSetChanged();
        }
    }

    public void setOnRefreshListener(RefreshRecy.onRefreshListener onRefreshListener, int pageSize) {
        initRefreshHead();
        initListener();
        this.onRefreshListener = onRefreshListener;
        this.pageSize = pageSize;
    }

    private onRefreshListener onRefreshListener;

    public interface onRefreshListener{

        /**
         * 下拉刷新
         */
        void onRefresh();

        /**
         * 上拉加载
         */
        void onLoadMore();

    }

}
