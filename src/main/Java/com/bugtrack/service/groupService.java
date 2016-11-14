package com.bugtrack.service;

import java.util.ArrayList;
import java.util.List;
import com.bugtrack.entity.group;
import com.bugtrack.config.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

/**
 * The groupService provides data structure
 * and methods to process groups
 * @version 0.9.9 15 July 2016
 * @author  Sergey Samsonov
 */
@Service
public class groupService {

    private SessionFactory sessionFactory;

    public groupService() {
        try{
            this.sessionFactory = HibernateUtil.getSessionFactory();
        }catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public void addEntity(group group) {
        if (group != null) {
            Session session = sessionFactory.openSession();
            Transaction tr = null;
            try {
                tr = session.beginTransaction();
                session.save(group);
                tr.commit();
            } catch (HibernateException e) {
                if (tr != null) tr.rollback();
                e.printStackTrace();
            } finally {
                session.close();
            }
        }
    }

    public void updateEntity(group group) {
        if (group != null) {
            Session session = sessionFactory.openSession();
            Transaction tr = null;
            try {
                tr = session.beginTransaction();
                session.update(group);
                tr.commit();
            } catch (HibernateException e) {
                if (tr != null) tr.rollback();
                e.printStackTrace();
            } finally {
                session.close();
            }
        }
    }

    public void delEntity(group group) {
        if (group != null) {
            Session session = sessionFactory.openSession();
            Transaction tr = null;
            try {
                tr = session.beginTransaction();
                session.delete(group);
                tr.commit();
            } catch (HibernateException e) {
                if (tr != null) tr.rollback();
                e.printStackTrace();
            } finally {
                session.close();
            }
        }
    }

    public List<group> getAll(){
        List<group> resultList = new ArrayList<>();
        String hql = "from group g ORDER BY g.name";
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

    public group getGroupById(Integer groupId){
        Session session = sessionFactory.openSession();
        Transaction tr = null;
        group group = null;
        try {
            tr = session.beginTransaction();
            Criteria groupCriteria = session.createCriteria(group.class);
            groupCriteria.add(Restrictions.eq("id", groupId));
            group = (group) groupCriteria.uniqueResult();
            tr.commit();
        } catch (HibernateException e) {
            if (tr != null) tr.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return group;
    }

    public String getGrNameByGrId(Integer grId){
        group group = this.getGroupById(grId);
        return (group == null)?"":group.getName();
    }

    public group getGroupByName(String grName){
        Session session = sessionFactory.openSession();
        Transaction tr = null;
        group group = null;
        try {
            tr = session.beginTransaction();
            Criteria groupCriteria = session.createCriteria(group.class);
            groupCriteria.add(Restrictions.eq("name", grName));
            group = (group) groupCriteria.uniqueResult();
            tr.commit();
        } catch (HibernateException e) {
            if (tr != null) tr.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return group;
    }

}
