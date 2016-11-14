package com.bugtrack.service;

import java.util.ArrayList;
import java.util.List;
import com.bugtrack.config.HibernateUtil;
import com.bugtrack.entity.ticktype;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

/**
 * The ticktypeService provides data structure
 * and methods to process types of tickets
 * @version 0.9.9 15 July 2016
 * @author  Sergey Samsonov
 */
@Service
public class ticktypeService {

    private SessionFactory sessionFactory;

    public ticktypeService() {
        try{
            this.sessionFactory = HibernateUtil.getSessionFactory();
        }catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public void addEntity(ticktype ticktype) {
        if (ticktype != null) {
            Session session = sessionFactory.openSession();
            Transaction tr = null;
            try {
                tr = session.beginTransaction();
                session.save(ticktype);
                tr.commit();
            } catch (HibernateException e) {
                if (tr != null) tr.rollback();
                e.printStackTrace();
            } finally {
                session.close();
            }
        }
    }

    public void updateEntity(ticktype ticktype) {
        if (ticktype != null) {
            Session session = sessionFactory.openSession();
            Transaction tr = null;
            try {
                tr = session.beginTransaction();
                session.update(ticktype);
                tr.commit();
            } catch (HibernateException e) {
                if (tr != null) tr.rollback();
                e.printStackTrace();
            } finally {
                session.close();
            }
        }
    }

    public void delEntity(ticktype ticktype) {
        if (ticktype != null) {
            Session session = sessionFactory.openSession();
            Transaction tr = null;
            try {
                tr = session.beginTransaction();
                session.delete(ticktype);
                tr.commit();
            } catch (HibernateException e) {
                if (tr != null) tr.rollback();
                e.printStackTrace();
            } finally {
                session.close();
            }
        }
    }

    public List<ticktype> getAll(){
        List<ticktype> resultList = new ArrayList<>();
        String hql = "from ticktype t ORDER BY t.name";
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

    public ticktype getTypeById(Integer id) {
        Session session = sessionFactory.openSession();
        Transaction tr = null;
        ticktype ticktype = null;
        try {
            tr = session.beginTransaction();
            Criteria typeCriteria = session.createCriteria(ticktype.class);
            typeCriteria.add(Restrictions.eq("id", id));
            ticktype = (ticktype) typeCriteria.uniqueResult();
            tr.commit();
        } catch (HibernateException e) {
            if (tr != null) tr.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return ticktype;
    }

    public ticktype getTypeByName(String tickTypeName){
        Session session = sessionFactory.openSession();
        Transaction tr = null;
        ticktype ticktype = null;
        try {
            tr = session.beginTransaction();
            Criteria typeCriteria = session.createCriteria(ticktype.class);
            typeCriteria.add(Restrictions.eq("name", tickTypeName));
            ticktype = (ticktype) typeCriteria.uniqueResult();
            tr.commit();
        } catch (HibernateException e) {
            if (tr != null) tr.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return ticktype;
    }

}
