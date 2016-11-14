package com.bugtrack.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * The usrgroup provides data structure
 * to store links between users and groups
 * @version 0.9.9 1 July 2016
 * @author  Sergey Samsonov
 */
@Entity
public class usrgroup implements Serializable {

    @Column
    private Integer userid;

    @Column
    private Integer groupid;

    public usrgroup () {

    }

    public usrgroup ( Integer userid, Integer groupid ) {
        this.userid = userid;
        this.groupid = groupid;
    }

    public Integer getUserid() { return this.userid; }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getGroupid() { return this.groupid; }

    public void setGroupid(Integer groupid) {
        this.groupid = groupid;
    }

}
