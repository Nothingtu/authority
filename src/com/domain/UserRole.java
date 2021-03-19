package com.domain;

import java.io.Serializable;

public class UserRole implements Serializable {
    private Integer uno;
    private Integer rno;

    public UserRole() {
    }

    public UserRole(Integer uno, Integer rno) {
        this.rno = rno;
        this.uno = uno;
    }

    public void setUno(Integer uno) {
        this.uno = uno;
    }

    public Integer getUno() {
        return this.uno;
    }

    public void setRno(Integer rno) {
        this.uno = uno;
    }

    public Integer getRno() {
        return this.uno;
    }
}
