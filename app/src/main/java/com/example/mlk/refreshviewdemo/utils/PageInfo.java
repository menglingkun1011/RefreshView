package com.example.mlk.refreshviewdemo.utils;

import java.util.List;

/**
 * 类描述：页面工具类
 */

public class PageInfo {
    //页号
    private int pageNum = 1;

    //每页的数量
    private int pageSize = 20;

    private boolean isCanLoadMore=true;

    public PageInfo() {
    }

    public int getPageNum() {
        return pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void reLoad() {
        pageNum = 1;
        isCanLoadMore=true;
    }

    /**
     * 是否加载更多
     * @param tList 返回的列表数据
     * @return true 可以加载更多，false 不可以加载更多
     **/
    public void setCanLoadMore(List tList) {
        if (tList == null || tList.size() < getPageSize()) {
            isCanLoadMore=false;
        } else {
            isCanLoadMore=true;
        }
    }

    public boolean isCanLoadMore(){
        return isCanLoadMore;
    }

    public void addPage(){
        pageNum+=1;
    }

    /**
     * 设置页面数量
     * @param pageSize
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
