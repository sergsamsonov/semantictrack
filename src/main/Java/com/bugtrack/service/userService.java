package com.bugtrack.service;

import com.bugtrack.config.HibernateUtil;
import com.bugtrack.entity.user;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.SQLQuery;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;


/**
 * The userService provides data structure
 * and methods to process users
 * @version 0.9.9 30 July 2016
 * @author  Sergey Samsonov
 */
@Service
public class userService implements UserDetailsService {

    protected final MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

    protected org.apache.commons.logging.Log logger;

    private SessionFactory sessionFactory;
    private String groupAuthoritiesByUsernameQuery;
    private String usersByUsernameQuery;

    public userService() {
        try{
            this.sessionFactory = HibernateUtil.getSessionFactory();
        }catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public void addEntity(user user) {
        if (user != null) {
            Session session = sessionFactory.openSession();
            Transaction tr = null;
            try {
                tr = session.beginTransaction();
                session.save(user);
                tr.commit();
            } catch (HibernateException e) {
                if (tr != null) tr.rollback();
                e.printStackTrace();
            } finally {
                session.close();
            }
        }
    }

    public void updateEntity(user user) {
        if (user != null) {
            Session session = sessionFactory.openSession();
            Transaction tr = null;
            try {
                tr = session.beginTransaction();
                session.update(user);
                tr.commit();
            } catch (HibernateException e) {
                if (tr != null) tr.rollback();
                e.printStackTrace();
            } finally {
                session.close();
            }
        }
    }

    public void delEntity(user user) {
        if (user != null) {
            Session session = sessionFactory.openSession();
            Transaction tr = null;
            try {
                tr = session.beginTransaction();
                session.delete(user);
                tr.commit();
            } catch (HibernateException e) {
                if (tr != null) tr.rollback();
                e.printStackTrace();
            } finally {
                session.close();
            }
        }
    }

    public List<user> getAll(){
        List<user> resultList = new ArrayList<>();
        String hql = "from user u ORDER BY u.login";
        Session session = sessionFactory.openSession();
        Transaction tr = null;
        try{
            tr = session.beginTransaction();
            Query q = session.createQuery(hql);
            resultList.addAll(q.list());
            tr.commit();
        }catch (HibernateException e) {
            if (tr!=null) tr.rollback();
            e.printStackTrace();
        }finally {
            session.close();
        }
        return resultList;
    }

    public user getUserByLogin(String login){
        Session session = sessionFactory.openSession();
        Transaction tr = null;
        user user = null;
        try {
            tr = session.beginTransaction();
            Criteria userCriteria = session.createCriteria(user.class);
            userCriteria.add(Restrictions.eq("login", login));
            user = (user) userCriteria.uniqueResult();
            tr.commit();
        } catch (HibernateException e) {
            if (tr != null) tr.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return user;
    }

    public user getUserById(Integer userId){
        user user = null;
        Session session = sessionFactory.openSession();
        Transaction tr = null;
        try {
            tr = session.beginTransaction();
            Criteria userCriteria = session.createCriteria(user.class);
            userCriteria.add(Restrictions.eq("id", userId));
            user = (user) userCriteria.uniqueResult();
            tr.commit();
        } catch (HibernateException e) {
            if (tr != null) tr.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return user;
    }

    public String getUsersByUsernameQuery() {
        return usersByUsernameQuery;
    }

    public user loadUserByUsername(String username) throws UsernameNotFoundException {

        List<user> users = loadUsersByUsername(username);

        if (users.size() == 0) {
            logger.debug("Query returned no results for user '" + username + "'");

            throw new UsernameNotFoundException(
                    messages.getMessage("userService.notFound", new Object[]{username}, "Username {0} not found"), username);
        }

        user user = users.get(0);

        Set<GrantedAuthority> dbAuthsSet = new HashSet<GrantedAuthority>();

        dbAuthsSet.addAll(loadGroupAuthorities(user.getUsername()));

        List<GrantedAuthority> dbAuths = new ArrayList<GrantedAuthority>(dbAuthsSet);

        if (dbAuths.size() == 0) {
            logger.debug("User '" + username + "' has no authorities and will be treated as 'not found'");

            throw new UsernameNotFoundException(
                    messages.getMessage("userService.noAuthority",
                            new Object[] {username}, "User {0} has no GrantedAuthority"), username);
        }

        user.setAuthorities(Collections.unmodifiableSet(user.sortAuthorities(dbAuthsSet)));

        return user;
    }

    protected List<user> loadUsersByUsername(String username) {
        Session session = sessionFactory.openSession();
        Transaction tr = null;
        List<user> usersList = new ArrayList<>();
        try{
            tr = session.beginTransaction();
            SQLQuery query = session.createSQLQuery(usersByUsernameQuery);
            query.addEntity(user.class);
            query.setParameter("login", username);
            usersList = query.list();
            usersList.stream()
                     .forEach(u -> {
                         u.setEnabled(u.getEnabledval());
                         u.setLogin(u.getLogin());
                         u.setAccountNonExpired(true);
                         u.setCredentialsNonExpired(true);
                         u.setAccountNonLocked(true);
                         u.setAuthorities(Collections.unmodifiableSet(u.sortAuthorities(AuthorityUtils.NO_AUTHORITIES)));
                     });
            tr.commit();
        }catch (HibernateException e) {
            if (tr!=null) tr.rollback();
            e.printStackTrace();
        }finally {
            session.close();
        }
        return usersList;
    }

    protected List<GrantedAuthority> loadGroupAuthorities(String username) {
        Session session = sessionFactory.openSession();
        Transaction tr = null;
        List<GrantedAuthority> authList = new ArrayList<>();
        try{
            tr = session.beginTransaction();
            SQLQuery query = session.createSQLQuery(groupAuthoritiesByUsernameQuery);
            query.setParameter("login", username);
            query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
            List queryList = query.list();
            for(Object object : queryList) {
                Map row = (Map) object;
                authList.add(new SimpleGrantedAuthority((String) row.get("name")));
            }
            tr.commit();
        }catch (HibernateException e) {
            if (tr!=null) tr.rollback();
            e.printStackTrace();
        }finally {
            session.close();
        }
        return authList;
    }

    public void setGroupAuthoritiesByUsernameQuery(String queryString) {
        groupAuthoritiesByUsernameQuery = queryString;
    }

    public void setUsersByUsernameQuery(String usersByUsernameQueryString) {
        this.usersByUsernameQuery = usersByUsernameQueryString;
    }

}
