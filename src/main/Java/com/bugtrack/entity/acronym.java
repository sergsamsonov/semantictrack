package com.bugtrack.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;


/**
 * The acronym provides data structure
 * for entity Acronyms
 * @version 0.9.9 31 March 2016
 * @author  Sergey Samsonov
 */
@Entity
@Table
public class acronym implements Serializable {

    @Id
    private Integer id;

    @Column
    private String acronym;

    private String oldacronym;

    @Column
    private String interpret;

    public acronym() {

    }

    public Integer getId() { return this.id; }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAcronym() { return this.acronym; }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public String getOldacronym() { return this.oldacronym; }

    public void setOldacronym(String oldacronym) {
        this.oldacronym = oldacronym;
    }

    public String getInterpret() { return this.interpret; }

    public void setInterpret(String interpret) {
        this.interpret = interpret;
    }
}

