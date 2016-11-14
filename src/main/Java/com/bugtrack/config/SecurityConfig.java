package com.bugtrack.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * The SecurityConfig provides a mechanism for users to ensure the order
 * in which servlet container initialization occurs
 * @version 0.9.9 30 June 2016
 * @author  Sergey Samsonov
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsService userDetailsService;

   @Autowired
    public void registerGlobalAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
            .antMatchers("/acronyms/**").access("hasRole('ACRON')")
            .antMatchers("/admin/**").access("hasRole('ADMIN')")
            .antMatchers("/eval/**").access("hasRole('EVAL')")
            .antMatchers("/tickets/tasks","/tickets/tasks",
                         "/tickets/detsearch","/tickets/search","/tickets/getsearchres").access("hasRole('TICKBRW')")
            .antMatchers("/tickets/create","/tickets/save.htm").access("hasRole('TICKCR')")
            .antMatchers("/tickets/update","/tickets/save.htm").access("hasRole('TICKEDIT')")
        .and()
            .formLogin()
            .loginPage("/login")
            .loginProcessingUrl("/j_spring_security_check")
            .failureUrl("/login?error")
            .usernameParameter("j_username")
            .passwordParameter("j_password")
            .permitAll()
        .and()
            .exceptionHandling().accessDeniedPage("/err403")
        .and()
            .logout()
            .permitAll()
            .logoutUrl("/logout")
            .logoutSuccessUrl("/login?logout")
            .invalidateHttpSession(true)
        .and()
            .csrf()
            .disable();

    }

    @Bean(name="passwordEncoder")
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}