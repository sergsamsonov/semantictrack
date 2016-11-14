package com.bugtrack.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * The grpermission provides data structure
 * to store links between users and groups
 * @version 0.9.9 1 July 2016
 * @author  Sergey Samsonov
 */
public class grpermiss implements Serializable {

    @Column
    private Integer groupid;

    @Column
    private Integer permissid;

    public grpermiss () {

    }

    public grpermiss ( Integer groupid, Integer permissid ) {
        this.groupid = groupid;
        this.permissid = permissid;
    }

    public Integer getGroupid() { return this.groupid; }

    public void setGroupid(Integer groupid) {
        this.groupid = groupid;
    }

    public Integer getPermissid() { return this.permissid; }

    public void setPermissid(Integer permissid) {
        this.permissid = permissid;
    }

}
