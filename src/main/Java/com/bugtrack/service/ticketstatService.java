package com.bugtrack.service;

import java.util.ArrayList;
import java.util.List;
import com.bugtrack.config.HibernateUtil;
import com.bugtrack.entity.ticketstat;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

/**
 * The ticketstatService provides data structure
 * and methods to process status of tickets
 * @version 0.9.9 15 July 2016
 * @author  Sergey Samsonov
 */
@Service
public class ticketstatService {

    private SessionFactory sessionFactory;

    public ticketstatService() {
        try{
            this.sessionFactory = HibernateUtil.getSessionFactory();
        }catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public void addEntity(ticketstat ticketstat) {
        if (ticketstat != null) {
            Session session = sessionFactory.openSession();
            Transaction tr = null;
            try {
                tr = session.beginTransaction();
                session.save(ticketstat);
                tr.commit();
            } catch (HibernateException e) {
                if (tr != null) tr.rollback();
                e.printStackTrace();
            } finally {
                session.close();
            }
        }
    }

    public void updateEntity(ticketstat ticketstat) {
        if (ticketstat != null) {
            Session session = sessionFactory.openSession();
            Transaction tr = null;
            try {
                tr = session.beginTransaction();
                session.update(ticketstat);
                tr.commit();
            } catch (HibernateException e) {
                if (tr != null) tr.rollback();
                e.printStackTrace();
            } finally {
                session.close();
            }
        }
    }

    public void delEntity(ticketstat ticketstat) {
        if (ticketstat != null) {
            Session session = sessionFactory.openSession();
            Transaction tr = null;
            try {
                tr = session.beginTransaction();
                session.delete(ticketstat);
                tr.commit();
            } catch (HibernateException e) {
                if (tr != null) tr.rollback();
                e.printStackTrace();
            } finally {
                session.close();
            }
        }
    }

    public List<ticketstat> getAll(){
        List<ticketstat> resultList = new ArrayList<>();
        String hql = "from ticketstat t ORDER BY t.name";
        Session session = sessionFactory.openSession();
        Transaction tr = null;
        try {
            tr = session.beginTransaction();
            Query q = session.createQuery(hql);
            resultList.addAll(q.list());
            tr.commit();
        } catch (HibernateException e) {
            if (tr != null) tr.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return resultList;
    }

    public ticketstat getStatById(Integer id) {
        Session session = sessionFactory.openSession();
        Transaction tr = null;
        ticketstat ticketst = null;
        try {
            tr = session.beginTransaction();
            Criteria statCriteria = session.createCriteria(ticketstat.class);
            statCriteria.add(Restrictions.eq("id", id));
            ticketst = (ticketstat) statCriteria.uniqueResult();
            tr.commit();
        } catch (HibernateException e) {
            if (tr != null) tr.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return ticketst;
    }

    public ticketstat getStatByName(String tickStatName){
        Session session = sessionFactory.openSession();
        Transaction tr = null;
        ticketstat ticketst = null;
        try {
            tr = session.beginTransaction();
            Criteria tickstCriteria = session.createCriteria(ticketstat.class);
            tickstCriteria.add(Restrictions.eq("name", tickStatName));
            ticketst = (ticketstat) tickstCriteria.uniqueResult();
            tr.commit();
        } catch (HibernateException e) {
            if (tr != null) tr.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return ticketst;
    }

}
