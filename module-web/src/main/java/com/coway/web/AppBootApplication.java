package com.coway.web;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

//@ComponentScan(basePackages = "com.coway")
//@SpringBootApplication

@SpringBootApplication(scanBasePackages={"com.coway"})
public class AppBootApplication  {

    private final static Logger logger = LoggerFactory.getLogger(AppBootApplication.class);

    public static void main(String[] args) {
        logger.info("================= start !!!!");
        SpringApplication.run(AppBootApplication.class, args);
    }

}