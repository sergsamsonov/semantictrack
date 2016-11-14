package com.bugtrack.service;

import java.util.ArrayList;
import java.util.List;
import com.bugtrack.config.HibernateUtil;
import com.bugtrack.entity.task;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

/**
 * The taskService provides data structure
 * and methods to process tasks
 * @version 0.9.9 15 July 2016
 * @author  Sergey Samsonov
 */
@Service
public class taskService {

    private SessionFactory sessionFactory;

    public taskService() {
        try{
            this.sessionFactory = HibernateUtil.getSessionFactory();
        }catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public void addEntity(task task) {
        if (task != null) {
            Session session = sessionFactory.openSession();
            Transaction tr = null;
            try {
                tr = session.beginTransaction();
                session.save(task);
                tr.commit();
            } catch (HibernateException e) {
                if (tr != null) tr.rollback();
                e.printStackTrace();
            } finally {
                session.close();
            }
        }
    }

    public void updateEntity(task task) {
        if (task != null) {
            Session session = sessionFactory.openSession();
            Transaction tr = null;
            try {
                tr = session.beginTransaction();
                session.update(task);
                tr.commit();
            } catch (HibernateException e) {
                if (tr != null) tr.rollback();
                e.printStackTrace();
            } finally {
                session.close();
            }
        }
    }

    public void delEntity(task task) {
        if (task != null) {
            Session session = sessionFactory.openSession();
            Transaction tr = null;
            try {
                tr = session.beginTransaction();
                session.delete(task);
                tr.commit();
            } catch (HibernateException e) {
                if (tr != null) tr.rollback();
                e.printStackTrace();
            } finally {
                session.close();
            }
        }
    }

    public List<task> getAll(){
        List<task> resultList = new ArrayList<>();
        String hql = "from task t ORDER BY t.name";
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

    public task getTaskById(Integer id) {
        Session session = sessionFactory.openSession();
        Transaction tr = null;
        task task = null;
        try {
            tr = session.beginTransaction();
            Criteria taskCriteria = session.createCriteria(task.class);
            taskCriteria.add(Restrictions.eq("id", id));
            task = (task) taskCriteria.uniqueResult();
            tr.commit();
        } catch (HibernateException e) {
            if (tr != null) tr.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return task;
    }

    public task getTaskByName(String taskName){
        Session session = sessionFactory.openSession();
        Transaction tr = null;
        task task = null;
        try {
            tr = session.beginTransaction();
            Criteria taskCriteria = session.createCriteria(task.class);
            taskCriteria.add(Restrictions.eq("name", taskName));
            task = (task) taskCriteria.uniqueResult();
            tr.commit();
        } catch (HibernateException e) {
            if (tr != null) tr.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return task;
    }
}
