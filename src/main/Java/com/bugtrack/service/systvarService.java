package com.bugtrack.service;

import java.util.ArrayList;
import java.util.List;
import com.bugtrack.config.HibernateUtil;
import com.bugtrack.entity.systvar;
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
public class systvarService {

    private SessionFactory sessionFactory;

    public systvarService() {
        try{
            this.sessionFactory = HibernateUtil.getSessionFactory();
        }catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public void saveOrUpdateEntity(systvar systvar) {
        if (systvar != null) {
            Session session = sessionFactory.openSession();
            Transaction tr = null;
            try {
                tr = session.beginTransaction();
                session.saveOrUpdate(systvar);
                tr.commit();
            } catch (HibernateException e) {
                if (tr != null) tr.rollback();
                e.printStackTrace();
            } finally {
                session.close();
            }
        }
    }

    public void updateEntity(systvar systvar) {
        if (systvar != null) {
            Session session = sessionFactory.openSession();
            Transaction tr = null;
            try {
                tr = session.beginTransaction();
                session.update(systvar);
                tr.commit();
            } catch (HibernateException e) {
                if (tr != null) tr.rollback();
                e.printStackTrace();
            } finally {
                session.close();
            }
        }
    }

    public systvar getSystVarByCode(String code) {
        Session session = sessionFactory.openSession();
        Transaction tr = null;
        systvar systvar = null;
        try {
            tr = session.beginTransaction();
            Criteria systvarCriteria = session.createCriteria(systvar.class);
            systvarCriteria.add(Restrictions.eq("code", code));
            systvar = (systvar) systvarCriteria.uniqueResult();
            tr.commit();
        } catch (HibernateException e) {
            if (tr != null) tr.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return systvar;
    }

}
