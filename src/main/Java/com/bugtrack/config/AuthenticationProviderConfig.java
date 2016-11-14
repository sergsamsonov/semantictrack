package com.bugtrack.config;

import com.bugtrack.service.userService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;

/**
 * The AuthenticationProviderConfig provides
 * an authentication mechanism for the application
 * @version 0.9.9 30 July 2016
 * @author  Sergey Samsonov
 */
@Configuration
public class AuthenticationProviderConfig {
    @Bean(name = "userDetailsService")
    public UserDetailsService userDetailsService() {
        userService userService = new userService();
        userService.setUsersByUsernameQuery("select * from users where login = :login");
        userService.setGroupAuthoritiesByUsernameQuery(
                "SELECT g.groupid as groupid, g.groupname as groupname, p.name as name " +
                "        FROM users u " +
                "        JOIN user_group ug ON u.userid = ug.userid " +
                "        JOIN groups g ON ug.groupid = g.groupid " +
                "        JOIN group_permission gp ON g.groupid = gp.groupid " +
                "        JOIN permission p ON gp.permissionid = p.permissionid " +
                "        WHERE u.login = :login");
        return userService;
    }
}

