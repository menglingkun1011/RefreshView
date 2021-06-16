package com.example.mlk.refreshviewdemo.view;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.mlk.refreshviewdemo.R;
import com.example.mlk.refreshviewdemo.loadmore.CustomLoadMoreView;

import java.util.List;

import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * 作者：mlk on 2018/6/3 17:16
 */
public class RefreshRecy extends FrameLayout {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private BaseQuickAdapter adapter;
    private Handler handler = new Handler();
    private int pageSize;
    private boolean isLoadMore;
    private View empytView;

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
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.blue,R.color.red);
        recyclerView = view.findViewById(R.id.recyclerView);
        empytView = LayoutInflater.from(context).inflate(R.layout.empty_layout, null);
        addView(view);
    }

    public void setAdapter(BaseQuickAdapter adapter){
        recyclerView.setAdapter(adapter);
        adapter.setLoadMoreView(new CustomLoadMoreView());
        //设置empty view
        adapter.setEmptyView(empytView);
        this.adapter = adapter;
    }

    public BaseQuickAdapter getAdapter() {
        return adapter;
    }

    private void initRefreshHead() {
    }

    private void initListener() {
        //下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(onRefreshListener != null){
                            onRefreshListener.onRefresh();
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },1800);
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

    /**
     *关闭上啦加载功能
     */
    public void closeLoadMord(){
        if(adapter != null) adapter.setOnLoadMoreListener(null,recyclerView);
    }

    /**
     * 加载更多数据
     * @param newList
     */
    public void loadMoreData(List newList){
        if(newList == null || newList.size() <= 0){
            adapter.notifyDataSetChanged();
            adapter.loadMoreEnd();
            isLoadMore = false;
            return;
        }
        if(newList.size() < pageSize){
            adapter.addData(newList);
            adapter.loadMoreEnd();
            isLoadMore = false;
            return;
        }
        isLoadMore = true;
        adapter.addData(newList);
        adapter.loadMoreComplete();
    }

    /**
     * 下拉刷新数据
     * @param newList
     */
    public void downRefreshData(List newList){
        isLoadMore = true;
        if(newList != null || newList.size() > 0){
            adapter.setNewData(newList);
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
