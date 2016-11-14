package com.bugtrack.service.ticket;

import java.util.List;
import java.util.ArrayList;

/**
 * The Word provides data structure
 * to process lemmas for specified token
 * @version 0.9.9 18 July 2016
 * @author  Sergey Samsonov
 */
public class Word {
    private String lemma;
    private double idf;
    private List<String> syns  = new ArrayList<>();

    /**
     * Constructs a Word. This will set
     * values for fields of a new Word object
     * @param   lemma    lemma
     * @param   idf      idf value calculated for lemma
     * @param   syns     list of synonyms for lemma
     */
    public Word(String lemma, double idf, List<String> syns) {
        this.lemma = lemma;
        this.idf = idf;
        if (syns == null) {
            this.syns = null;
        } else {
            this.syns.addAll(syns);
        }
    }

    public String getLemma(){
        return lemma;
    }

    public double getIdf(){
        return idf;
    }

    public List<String> getSyns(){
        return syns;
    }

    public void setSyns(List<String> syns){
        if (syns == null) {
            this.syns = null;
        } else {
            this.syns.addAll(syns);
        }
    }
}