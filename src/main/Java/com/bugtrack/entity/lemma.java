package com.bugtrack.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * The lemma provides data structure
 * for entity Lemma
 * @version 0.9.9 20 April 2016
 * @author  Sergey Samsonov
 */
@Entity
@Table
public class lemma implements Serializable {

    @Id
    private String lemma;

    @Column
    private Integer ticknum;

    public lemma() {

    }

    public lemma ( String lemma, Integer ticknum ) {
        this.lemma = lemma;
        this.ticknum = ticknum;
    }

    public String getLemma() { return this.lemma; }

    public void setLemma(String lemma) {
        this.lemma = lemma;
    }

    public Integer getTicknum() { return this.ticknum; }

    public void setTicknum(Integer ticknum) {
        this.ticknum = ticknum;
    }

}
