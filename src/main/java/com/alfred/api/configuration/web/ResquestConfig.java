package com.alfred.api.configuration.web;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * Created by jonathan on 5/5/17.
 */

@EnableWebSecurity
public class ResquestConfig extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf()
                .disable()
                .exceptionHandling()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/web-hook/**").permitAll()
                .antMatchers("/build/**").permitAll()
                .antMatchers("/profile/**").permitAll()
                .antMatchers("/machine/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling();
    }
}
