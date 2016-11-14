package com.bugtrack.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.bugtrack.config.HibernateUtil;
import com.bugtrack.entity.grpermiss;
import com.bugtrack.service.permissionService;
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
 * The grpermissService provides data structure
 * and methods to process links between groups and permissions
 * @version 0.9.9 15 July 2016
 * @author  Sergey Samsonov
 */
@Service
public class grpermissService {

    private SessionFactory sessionFactory;

    private permissionService permissService = new permissionService();

    public grpermissService() {
        try{
            this.sessionFactory = HibernateUtil.getSessionFactory();
        }catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public void addEntity(grpermiss grpermiss) {
        if (grpermiss != null) {
            Session session = sessionFactory.openSession();
            Transaction tr = null;
            try {
                tr = session.beginTransaction();
                session.save(grpermiss);
                tr.commit();
            } catch (HibernateException e) {
                if (tr != null) tr.rollback();
                e.printStackTrace();
            } finally {
                session.close();
            }
        }
    }

    public void delEntity(grpermiss grpermiss) {
        if (grpermiss != null) {
            Session session = sessionFactory.openSession();
            Transaction tr = null;
            try {
                tr = session.beginTransaction();
                session.delete(grpermiss);
                tr.commit();
            } catch (HibernateException e) {
                if (tr != null) tr.rollback();
                e.printStackTrace();
            } finally {
                session.close();
            }
        }
    }

    public Boolean permissUsageCheck(Integer permissId) {
        List<grpermiss> grperList = new ArrayList<>();
        Session session = sessionFactory.openSession();
        Transaction tr = null;
        try {
            tr = session.beginTransaction();
            Criteria grperCriteria = session.createCriteria(grpermiss.class);
            grperCriteria.add(Restrictions.eq("permissid", permissId));
            grperList = grperCriteria.list();
            tr.commit();
        } catch (HibernateException e) {
            if (tr != null) tr.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return ((grperList != null) && (grperList.size() > 0));
    }

    public List<String> getPerListByGroupId(Integer groupId) {
        List<grpermiss> grperList = new ArrayList<>();
        Session session = sessionFactory.openSession();
        Transaction tr = null;
        try {
            tr = session.beginTransaction();
            Criteria grperCriteria = session.createCriteria(grpermiss.class);
            grperCriteria.add(Restrictions.eq("groupid", groupId));
            grperList = grperCriteria.list();
            tr.commit();
        } catch (HibernateException e) {
            if (tr != null) tr.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return grperList == null ? null : grperList.stream()
                .map(p -> Integer.toString(p.getPermissid()))
                .collect(toList());
    }

    public String getPerNamesByGroupId(Integer groupId) {
        List<grpermiss> grperList = new ArrayList<>();
        Session session = sessionFactory.openSession();
        Transaction tr = null;
        try {
            tr = session.beginTransaction();
            Criteria grperCriteria = session.createCriteria(grpermiss.class);
            grperCriteria.add(Restrictions.eq("groupid", groupId));
            grperList = grperCriteria.list();
            tr.commit();
        } catch (HibernateException e) {
            if (tr != null) tr.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return grperList == null ? "" : grperList.stream()
                .map(grper -> permissService.getPerNameByPerId(grper.getPermissid()))
                .collect(Collectors.joining(","));
    }

    public void delGrpermissByGroupId(Integer groupId) {
        String hql = "delete from grpermiss where groupid = :groupid";
        Session session = sessionFactory.openSession();
        Transaction tr = null;
        try {
            tr = session.beginTransaction();
            session.createQuery(hql).setInteger("groupid", groupId).executeUpdate();
            tr.commit();
        } catch (HibernateException e) {
            if (tr != null) tr.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

}
