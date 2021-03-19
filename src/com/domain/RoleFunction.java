package com.domain;

import java.io.Serializable;

public class RoleFunction implements Serializable {
    private Integer fno;
    private Integer rno;

    public RoleFunction() {
    }

    public RoleFunction(Integer fno, Integer rno) {
        this.rno = rno;
        this.fno = fno;
    }

    public void setFno(Integer uno) {
        this.fno = uno;
    }

    public Integer getFno() {
        return this.fno;
    }

    public void setRno(Integer rno) {
        this.rno = rno;
    }

    public Integer getRno() {
        return this.rno;
    }
}
