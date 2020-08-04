package com.tkd.springsecurity.basicauth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/private/**").authenticated() // This Tells all apis started with /private should be authenticated
                .antMatchers("/public/**").permitAll() // This Tells all apis started with /public doesn't need to be authenticated
                .anyRequest().authenticated() // This tells every other requests should be authenticated i.e /welcome
                .and().httpBasic();
    }
}
