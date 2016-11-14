package com.bugtrack.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * The tikcet provides data structure
 * for entity Ticket
 * @version 0.9.9 23 July 2016
 * @author  Sergey Samsonov
 */
@Entity
@Table
public class ticket implements Serializable {

    @Id
    private Integer number;

    @Column
    private Integer lemmasnum = 0;

    @Column
    private Integer issuenum = 0;

    @Column
    private Integer issdescnum = 0;

    @Column
    private Integer solutnum = 0;

    @Column
    private Integer soldetnum = 0;

    @Column
    private String issue;

    @Column
    private String issuedescr;

    @Column
    private String solution;

    @Column
    private String solutiondet;

    @Column
    private String lemmas;

    @Column
    private String issuelem;

    @Column
    private String issdesclem;

    @Column
    private String solutionlem;

    @Column
    private String soldetlem;

    @Column
    private Integer user;

    @Column
    private Integer tickstat;

    @Column
    private Integer task;

    @Column
    private Integer ticktype;

    @Column
    private Integer responsible;

    @Column
    private String srcfiles;

    public ticket() {

    }

    public Integer getNumber() {
        return this.number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getLemmasnum() {
        return this.lemmasnum;
    }

    public void setLemmasnum(Integer lemmasnum) {
        this.lemmasnum = lemmasnum;
    }

    public Integer getIssuenum() {
        return this.issuenum;
    }

    public void setIssuenum(Integer issuenum) {
        this.issuenum = issuenum;
    }

    public Integer getIssdescnum() {
        return this.issdescnum;
    }

    public void setIssdescnum(Integer issdescnum) {
        this.issdescnum = issdescnum;
    }

    public Integer getSolutnum() {
        return this.solutnum;
    }

    public void setSolutnum(Integer solutnum) {
        this.solutnum = solutnum;
    }

    public Integer getSoldetnum() {
        return this.soldetnum;
    }

    public void setSoldetnum(Integer soldetnum) {
        this.soldetnum = soldetnum;
    }

    public String getIssue() {
        return this.issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getIssuedescr() {
        return this.issuedescr;
    }

    public void setIssuedescr(String issuedescr) {
        this.issuedescr = issuedescr;
    }

    public String getSolution() {
        return this.solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public String getLemmas() {
        return this.lemmas;
    }

    public void setLemmas(String lemmas) {
        this.lemmas = lemmas;
    }

    public String getIssuelem() {
        return this.issuelem;
    }

    public void setIssuelem(String issuelem) {
        this.issuelem = issuelem;
    }

    public String getIssdesclem() {
        return this.issdesclem;
    }

    public void setIssdesclem(String issdesclem) {
        this.issdesclem = issdesclem;
    }

    public String getSolutionlem() {
        return this.solutionlem;
    }

    public void setSolutionlem(String solutionlem) {
        this.solutionlem = solutionlem;
    }

    public String getSoldetlem() {
        return this.soldetlem;
    }

    public void setSoldetlem(String soldetlem) {
        this.soldetlem = soldetlem;
    }

    public String getSolutiondet() {
        return this.solutiondet;
    }

    public void setSolutiondet(String solutiondet) {
        this.solutiondet = solutiondet;
    }

    public Integer getUser() {
        return this.user;
    }

    public void setUser(Integer user) {
        this.user = user;
    }

    public Integer getTickstat() { return this.tickstat; }

    public void setTickstat(Integer tickstat) { this.tickstat = tickstat; }

    public Integer getTicktype() { return this.ticktype; }

    public void setTicktype(Integer ticktype) { this.ticktype = ticktype; }

    public Integer getTask() { return this.task; }

    public void setTask(Integer task) { this.task = task; }

    public Integer getResponsible() {
        return this.responsible;
    }

    public void setResponsible(Integer responsible) {
        this.responsible = responsible;
    }

    public String getSrcfiles() {
        return this.srcfiles;
    }

    public void setSrcfiles(String srcfiles) {
        this.srcfiles = srcfiles;
    }

}
