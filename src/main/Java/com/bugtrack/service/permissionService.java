package com.bugtrack.service;

import java.util.ArrayList;
import java.util.List;
import com.bugtrack.config.HibernateUtil;
import com.bugtrack.entity.permission;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

/**
 * The permissionService provides data structure
 * and methods to process permissions
 * @version 0.9.9 15 July 2016
 * @author  Sergey Samsonov
 */
@Service
public class permissionService {

    private SessionFactory sessionFactory;

    public permissionService() {
        try{
            this.sessionFactory = HibernateUtil.getSessionFactory();
        }catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public void addEntity(permission permiss) {
        if (permiss != null) {
            Session session = sessionFactory.openSession();
            Transaction tr = null;
            try {
                tr = session.beginTransaction();
                session.save(permiss);
                tr.commit();
            } catch (HibernateException e) {
                if (tr != null) tr.rollback();
                e.printStackTrace();
            } finally {
                session.close();
            }
        }
    }

    public void updateEntity(permission permiss) {
        if (permiss != null) {
            Session session = sessionFactory.openSession();
            Transaction tr = null;
            try {
                tr = session.beginTransaction();
                session.update(permiss);
                tr.commit();
            } catch (HibernateException e) {
                if (tr != null) tr.rollback();
                e.printStackTrace();
            } finally {
                session.close();
            }
        }
    }

    public void delEntity(permission permiss) {
        if (permiss != null) {
            Session session = sessionFactory.openSession();
            Transaction tr = null;
            try {
                tr = session.beginTransaction();
                session.delete(permiss);
                tr.commit();
            } catch (HibernateException e) {
                if (tr != null) tr.rollback();
                e.printStackTrace();
            } finally {
                session.close();
            }
        }
    }

    public List<permission> getAll(){
        List<permission> resultList = new ArrayList<>();
        String hql = "from permission p ORDER BY p.name";
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

    public permission getPermissById(Integer perId){
        Session session = sessionFactory.openSession();
        Transaction tr = null;
        permission permiss = null;
        try {
            tr = session.beginTransaction();
            Criteria perCriteria = session.createCriteria(permission.class);
            perCriteria.add(Restrictions.eq("id", perId));
            permiss = (permission) perCriteria.uniqueResult();
            tr.commit();
        } catch (HibernateException e) {
            if (tr != null) tr.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return permiss;
    }

    public String getPerNameByPerId(Integer perId){
        permission permiss = this.getPermissById(perId);
        return (permiss == null)?"":permiss.getName();
    }

    public permission getPermissByName(String permissName){
        Session session = sessionFactory.openSession();
        Transaction tr = null;
        permission permiss = null;
        try {
            tr = session.beginTransaction();
            Criteria perCriteria = session.createCriteria(permission.class);
            perCriteria.add(Restrictions.eq("name", permissName));
            permiss = (permission) perCriteria.uniqueResult();
            tr.commit();
        } catch (HibernateException e) {
            if (tr != null) tr.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return permiss;
    }

}
