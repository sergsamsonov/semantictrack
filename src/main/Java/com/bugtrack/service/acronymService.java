package com.bugtrack.service;

import com.bugtrack.config.HibernateUtil;
import com.bugtrack.entity.acronym;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

/**
 * The acronymService provides data structure
 * and methods to process acronyms
 * @version 0.9.9 15 July 2016
 * @author  Sergey Samsonov
 */
@Service
public class acronymService {

    private SessionFactory sessionFactory;

    public acronymService() {
        try{
            this.sessionFactory = HibernateUtil.getSessionFactory();
        }catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public void addEntity(acronym acronym) {
        if (acronym != null) {
            Session session = sessionFactory.openSession();
            Transaction tr = null;
            try {
                tr = session.beginTransaction();
                session.save(acronym);
                tr.commit();
            } catch (HibernateException e) {
                if (tr != null) tr.rollback();
                e.printStackTrace();
            } finally {
                session.close();
            }
        }
    }

    public void updateEntity(acronym acronym) {
        if (acronym != null) {
            Session session = sessionFactory.openSession();
            Transaction tr = null;
            try {
                tr = session.beginTransaction();
                session.update(acronym);
                tr.commit();
            } catch (HibernateException e) {
                if (tr != null) tr.rollback();
                e.printStackTrace();
            } finally {
                session.close();
            }
        }
    }

    public void delEntity(acronym acronym) {
        if (acronym != null) {
            Session session = sessionFactory.openSession();
            Transaction tr = null;
            try {
                tr = session.beginTransaction();
                session.delete(acronym);
                tr.commit();
            } catch (HibernateException e) {
                if (tr != null) tr.rollback();
                e.printStackTrace();
            } finally {
                session.close();
            }
        }
    }

    public List<acronym> getAll(){
        List<acronym> resultList = new ArrayList<>();
        String hql = "from acronym a ORDER BY a.acronym";
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

    public acronym getAcronymByName(String acron) {
        Session session = sessionFactory.openSession();
        Transaction tr = null;
        acronym acronym = null;
        try {
            tr = session.beginTransaction();
            Criteria acrCriteria = session.createCriteria(acronym.class);
            acrCriteria.add(Restrictions.eq("acronym", acron));
            acronym = (acronym) acrCriteria.uniqueResult();
            tr.commit();
        } catch (HibernateException e) {
            if (tr != null) tr.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return acronym;
    }

    public acronym getAcronymById(Integer id) {
        Session session = sessionFactory.openSession();
        Transaction tr = null;
        acronym acronym = null;
        try {
            tr = session.beginTransaction();
            Criteria acrCriteria = session.createCriteria(acronym.class);
            acrCriteria.add(Restrictions.eq("id", id));
            acronym = (acronym) acrCriteria.uniqueResult();
            tr.commit();
        } catch (HibernateException e) {
            if (tr != null) tr.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return acronym;
    }

    public String getInterpretByName(String acrName) {
        Session session = sessionFactory.openSession();
        Transaction tr = null;
        acronym acronym = null;
        try {
            tr = session.beginTransaction();
            Criteria acrCriteria = session.createCriteria(acronym.class);
            acrCriteria.add(Restrictions.ilike("acronym", acrName));
            acronym = (acronym) acrCriteria.uniqueResult();
            tr.commit();
        } catch (HibernateException e) {
            if (tr != null) tr.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return ((acronym == null)?"":acronym.getInterpret());
    }

}
