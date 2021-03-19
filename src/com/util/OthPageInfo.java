package com.util;

import java.io.Serializable;
import java.util.List;

/**
 * @program: authority_management
 * @description: 这是一个用来装载除了用户信息以外的domain+page+maxpage
 * @author: zhang jie
 * @create: 2021-03-15 18:24
 */
public class OthPageInfo implements Serializable {

    private Integer page;
    private Integer maxPage;
    private List  list;

    public OthPageInfo() {
    }

    public OthPageInfo(Integer page, Integer maxPage, List list) {
        this.page = page;
        this.maxPage = maxPage;
        this.list = list;
    }

    @Override
    public String toString() {
        return "OPageInfo{" +
                "page=" + page +
                ", maxPage=" + maxPage +
                ", list=" + list +
                '}';
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
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
