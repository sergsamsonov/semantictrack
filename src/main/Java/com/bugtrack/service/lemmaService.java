package com.bugtrack.service;

import java.util.ArrayList;
import java.util.List;
import com.bugtrack.config.HibernateUtil;
import com.bugtrack.entity.lemma;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import static java.util.stream.Collectors.summingInt;
import static java.util.stream.Collectors.toList;

/**
 * The lemmaService provides data structure
 * and methods to process lemmas
 * @version 0.9.9 15 July 2016
 * @author  Sergey Samsonov
 */
@Service
public class lemmaService {

    private SessionFactory sessionFactory;

    public lemmaService() {
        try{
            this.sessionFactory = HibernateUtil.getSessionFactory();
        }catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public void addEntity(lemma lemma) {
        if (lemma != null) {
            Session session = sessionFactory.openSession();
            Transaction tr = null;
            try {
                tr = session.beginTransaction();
                session.save(lemma);
                tr.commit();
            } catch (HibernateException e) {
                if (tr != null) tr.rollback();
                e.printStackTrace();
            } finally {
                session.close();
            }
        }
    }

    public void updateEntity(lemma lemma) {
        if (lemma != null) {
            Session session = sessionFactory.openSession();
            Transaction tr = null;
            try {
                tr = session.beginTransaction();
                session.update(lemma);
                tr.commit();
            } catch (HibernateException e) {
                if (tr != null) tr.rollback();
                e.printStackTrace();
            } finally {
                session.close();
            }
        }
    }

    public void delEntity(lemma lemma) {
        if (lemma != null) {
            Session session = sessionFactory.openSession();
            Transaction tr = null;
            try {
                tr = session.beginTransaction();
                session.delete(lemma);
                tr.commit();
            } catch (HibernateException e) {
                if (tr != null) tr.rollback();
                e.printStackTrace();
            } finally {
                session.close();
            }
        }
    }

    public List<lemma> getLemmasByList(List<String> lemmasList){
        if (lemmasList == null) {
            return null;
        }
        Session session = sessionFactory.openSession();
        Transaction tr = null;
        List<lemma> lemmas = new ArrayList<>();
        try {
            tr = session.beginTransaction();
            Criteria lemmasCriteria = session.createCriteria(lemma.class);
            lemmasCriteria.add(Restrictions.in("lemma", lemmasList));
            lemmas.addAll(lemmasCriteria.list());
            tr.commit();
        } catch (HibernateException e) {
            if (tr != null) tr.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return lemmas;
    }

    public lemma getLemmaObjByLemma(String lemmastr) {
        Session session = sessionFactory.openSession();
        Transaction tr = null;
        lemma lemma = null;
        try {
            tr = session.beginTransaction();
            Criteria lemmasCriteria = session.createCriteria(lemma.class);
            lemmasCriteria.add(Restrictions.eq("lemma", lemmastr));
            lemma = (lemma) lemmasCriteria.uniqueResult();
            tr.commit();
        } catch (HibernateException e) {
            if (tr != null) tr.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return lemma;
    }

    public void delAllLemmas() {
        String hql = "delete from lemma";
        Session session = sessionFactory.openSession();
        Transaction tr = null;
        try {
            tr = session.beginTransaction();
            session.createQuery(hql).executeUpdate();
            tr.commit();
        } catch (HibernateException e) {
            if (tr != null) tr.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

}
