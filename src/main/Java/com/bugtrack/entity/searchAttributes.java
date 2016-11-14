package com.bugtrack.entity;

/**
 * The searchAttributes provides data structure
 * for entity Attributes of detailed search
 * @version 0.9.9 29 March 2016
 * @author  Sergey Samsonov
 */
public class searchAttributes {

    private String request;

    private String simfield;

    public searchAttributes() {

    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getSimfield() {
        return simfield;
    }

    public void setSimfield(String simfield) {
        this.simfield = simfield;
    }

}
