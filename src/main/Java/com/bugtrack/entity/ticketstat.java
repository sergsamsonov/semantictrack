package com.bugtrack.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * The ticketstat provides data structure
 * for entity Status of ticket
 * @version 0.9.9 31 July 2016
 * @author  Sergey Samsonov
 */
@Entity
@Table
public class ticketstat implements Serializable {

    @Id
    private Integer id;

    @Column
    private String name;

    private String  oldname;

    @Column
    private String description;

    public ticketstat() {

    }

    public Integer getId() { return this.id; }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() { return this.name; }

    public void setName(String name) {
        this.name = name;
    }

    public String getOldname() { return this.oldname; }

    public void setOldname(String oldname) {
        this.oldname = oldname;
    }

    public String getDescription() { return this.description; }

    public void setDescription(String description) {
        this.description = description;
    }

}

