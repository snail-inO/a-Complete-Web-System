package com.multi.common.utils;

import java.util.List;

public class PagingUtil<T> {
    private int pageSize;//每页显示多少条记录
    private int currentPage;//当前第几页数据
    private int totalRecord;//一共多少条记录
    private List<T> dataList;//要显示的数据
    private int totalPage;//总页数

    public PagingUtil(int pageSize, int currentPage, int totalRecord, List<T> dataList, int totalPage) {
        this.pageSize = pageSize;
        this.currentPage = currentPage;
        this.totalRecord = totalRecord;
        this.dataList = dataList;
        this.totalPage = totalPage;
    }

    public PagingUtil(int pageNum, int pageSize, List<T> sourceList) {
        if (sourceList == null || sourceList.size() == 0 || pageSize == 0 || pageNum <= 0)
            return;

        totalRecord = sourceList.size();
        this.pageSize = pageSize;
        totalPage = totalRecord / pageSize;
        if (totalRecord % pageSize != 0)
            totalPage += 1;
        if (totalPage < pageNum)
            currentPage = totalPage;
        else
            currentPage = pageNum;

        int fromIndex = pageSize * (currentPage - 1);
        int toIndex;
        if (pageSize * currentPage > totalRecord)
            toIndex = totalRecord;
        else
            toIndex = pageSize * currentPage;
        dataList = sourceList.subList(fromIndex, toIndex);
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
}
