package com.domain;

import java.io.Serializable;
import java.util.List;

public class Function implements Serializable {
    private Integer fno;
    private String fname;
    //功能请求
    private String fhref;
    //功能标识 1菜单功能 2按钮功能
    private Integer flag;
    //菜单功能显示的位置
    private String ftarget;

    @Override
    public String toString() {
        return "Function{" +
                "fno=" + fno +
                ", fname='" + fname + '\'' +
                ", fhref='" + fhref + '\'' +
                ", flag=" + flag +
                ", ftarget='" + ftarget + '\'' +
                ", pno=" + pno +
                ", createtime='" + createtime + '\'' +
                ", del=" + del +
                ", yl1='" + yl1 + '\'' +
                ", yl2='" + yl2 + '\'' +
                ", pfn=" + pfn +
                ", children=" + children +
                '}';
    }

    //父级菜单的编号
    private Integer pno;

    private String createtime;
    private Integer del;

    public List<Function> getChildren() {
        return children;
    }

    public void setChildren(List<Function> children) {
        this.children = children;
    }

    private String yl1;
    private String yl2;

    private Function pfn;

    private List<Function> children;

    public Function getPfn() {
        return pfn;
    }

    public void setPfn(Function pfn) {
        this.pfn = pfn;
    }

    public void setFno(Integer fno) {
        this.fno = fno;
    }

    public Integer getFno() {
        return this.fno;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getFname() {
        return this.fname;
    }

    public void setFhref(String fhref) {
        this.fhref = fhref;
    }

    public String getFhref() {
        return this.fhref;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public Integer getFlag() {
        return this.flag;
    }

    public void setFtarget(String ftarget) {
        this.ftarget = ftarget;
    }

    public String getFtarget() {
        return this.ftarget;
    }

    public void setPno(Integer pno) {
        this.pno = pno;
    }

    public Integer getPno() {
        return this.pno;
    }

    public void setDel(Integer del) {
        this.del = del;
    }

    public Integer getDel() {
        return this.del;
    }

    public void setYl1(String yl1) {
        this.yl1 = yl1;
    }

    public String getYl1() {
        return this.yl1;
    }

    public void setYl2(String yl2) {
        this.yl2 = yl2;
    }

    public String getYl2() {
        return this.yl2;
    }

    public Function() {
    }

    public Function(Integer fno, String fname, String fhref, Integer flag,
                    String ftarget, Integer pno, String createtime,
                    Integer del, String yl1, String yl2, Function pfn, List<Function> children) {
        this.fno = fno;
        this.fname = fname;
        this.fhref = fhref;
        this.flag = flag;
        this.ftarget = ftarget;
        this.pno = pno;
        this.createtime = createtime;
        this.del = del;
        this.yl1 = yl1;
        this.yl2 = yl2;
        this.pfn = pfn;
        this.children = children;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getCreatetime() {
        return this.createtime;
    }
}
