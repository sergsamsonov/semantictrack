package com.bugtrack.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * The lemma provides data structure
 * to store system variables
 * @version 0.9.9 27 July 2016
 * @author  Sergey Samsonov
 */
@Entity
@Table
public class systvar {

    @Id
    private String code;

    @Column
    private String descr;

    @Column
    private String value;

    public systvar() {

    }

    public systvar ( String code, String descr, String value) {
        this.code = code;
        this.descr = descr;
        this.value = value;
    }

    public String getCode() { return this.code; }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescr() { return this.descr; }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getValue() { return this.value; }

    public void setValue(String value) {
        this.value = value;
    }

}
