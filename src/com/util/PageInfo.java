package com.util;

import java.io.Serializable;
import java.util.List;

/**
 * @program: authority_management
 * @description: 用来装载查询返回的结果和每页展示的个数与最大页数
 * @author: zhang jie
 * @create: 2021-03-13 10:40
 */
public class PageInfo implements Serializable {
    private Integer row;
    private Integer maxPage;
    private Integer page;
    private List list;

    public PageInfo(Integer row, Integer maxPage, Integer page,List list) {
        this.row = row;
        this.maxPage = maxPage;
        this.list = list;
        this.page = page;
    }

    public PageInfo() {
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    @Override
    public String toString() {
        return "PageInfo{" +
                "row=" + row +
                ", maxPage=" + maxPage +
                ", page=" + page +
                ", list=" + list +
                '}';
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public Integer getMaxPage() {
        return maxPage;
    }

    public void setMaxPage(Integer maxPage) {
        this.maxPage = maxPage;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }
}
