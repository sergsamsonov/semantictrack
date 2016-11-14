package com.bugtrack.config;

import org.hibernate.Session;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.SessionFactory;

/**
 * The HibernateUtil provides static utility methods for Hibernate
 * @version 0.9.9 30 June 2016
 * @author  Sergey Samsonov
 */
public class HibernateUtil {

    private static final SessionFactory sessionFactory;
    private static final Session session;
    static {
        try {
            sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
            session = sessionFactory.openSession();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static Session getSession(){
        return session;
    }
}
