package com.bugtrack.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.bugtrack.config.HibernateUtil;
import com.bugtrack.entity.usrgroup;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import static java.util.stream.Collectors.toList;

/**
 * The usrgroupService provides data structure
 * and methods to process links between users and groups
 * @version 0.9.9 15 July 2016
 * @author  Sergey Samsonov
 */
@Service
public class usrgroupService {

    private SessionFactory sessionFactory;

    private groupService groupService = new groupService();

    public usrgroupService() {
        try{
            this.sessionFactory = HibernateUtil.getSessionFactory();
        }catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public void addEntity(usrgroup usrgroup) {
        if (usrgroup != null) {
            Session session = sessionFactory.openSession();
            Transaction tr = null;
            try {
                tr = session.beginTransaction();
                session.save(usrgroup);
                tr.commit();
            } catch (HibernateException e) {
                if (tr != null) tr.rollback();
                e.printStackTrace();
            } finally {
                session.close();
            }
        }
    }

    public void delEntity(usrgroup usrgroup) {
        if (usrgroup != null) {
            Session session = sessionFactory.openSession();
            Transaction tr = null;
            try {
                tr = session.beginTransaction();
                session.delete(usrgroup);
                tr.commit();
            } catch (HibernateException e) {
                if (tr != null) tr.rollback();
                e.printStackTrace();
            } finally {
                session.close();
            }
        }
    }

    public Boolean groupUsageCheck(Integer groupId) {
        List<usrgroup> usrgrList = new ArrayList<>();
        Session session = sessionFactory.openSession();
        Transaction tr = null;
        try {
            tr = session.beginTransaction();
            Criteria usgrCriteria = session.createCriteria(usrgroup.class);
            usgrCriteria.add(Restrictions.eq("groupid", groupId));
            usrgrList = usgrCriteria.list();
            tr.commit();
        } catch (HibernateException e) {
            if (tr != null) tr.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return ((usrgrList != null) && (usrgrList.size() > 0));
    }

    public List<String> getGrListByUserId(Integer userId) {
        List<usrgroup> usrgrList = new ArrayList<>();
        Session session = sessionFactory.openSession();
        Transaction tr = null;
        try {
            tr = session.beginTransaction();
            Criteria usgrCriteria = session.createCriteria(usrgroup.class);
            usgrCriteria.add(Restrictions.eq("userid", userId));
            usrgrList = usgrCriteria.list();
            tr.commit();
        } catch (HibernateException e) {
            if (tr != null) tr.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return usrgrList == null ? null : usrgrList.stream()
                                                   .map(p -> Integer.toString(p.getGroupid()))
                                                   .collect(toList());
    }

    public String getGrNamesByUserId(Integer userId) {
        List<usrgroup> usrgrList = new ArrayList<>();
        Session session = sessionFactory.openSession();
        Transaction tr = null;
        try {
            tr = session.beginTransaction();
            Criteria usgrCriteria = session.createCriteria(usrgroup.class);
            usgrCriteria.add(Restrictions.eq("userid", userId));
            usrgrList = usgrCriteria.list();
            tr.commit();
        } catch (HibernateException e) {
            if (tr != null) tr.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return usrgrList == null ? "" : usrgrList.stream()
                                                 .map(ug -> groupService.getGrNameByGrId(ug.getGroupid()))
                                                 .collect(Collectors.joining(","));
    }

    public void delUsrgroupsByUserId(Integer userId) {
        String hql = "delete from usrgroup where userid = :userid";
        Session session = sessionFactory.openSession();
        Transaction tr = null;
        try {
            tr = session.beginTransaction();
            session.createQuery(hql).setInteger("userid", userId).executeUpdate();
            tr.commit();
        } catch (HibernateException e) {
            if (tr != null) tr.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

}
