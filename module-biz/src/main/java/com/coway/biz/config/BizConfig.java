package com.coway.biz.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.aspectj.AnnotationTransactionAspect;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by foresight on 17. 8. 2.
 */
@Configuration
@PropertySource(value={
        "classpath:property/biz-config.properties"
})
@EnableTransactionManagement
@EnableJpaAuditing
@ComponentScan(basePackages = "com.coway.biz")
@EnableJpaRepositories(basePackages = { "com.coway.biz.repository" }, transactionManagerRef = "transactionManager")
public class BizConfig implements EnvironmentAware {

    @Autowired
    Environment env;

    @Override
    public void setEnvironment(Environment environment) {
        this.env = environment;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
        propertySourcesPlaceholderConfigurer.setFileEncoding("UTF-8");
        return propertySourcesPlaceholderConfigurer;
    }

    // Spring Security
    //---------------------------------------------------------------------
//    @Value("classpath:oauth2-schema/schema.sql")
//    private Resource schemaScript;
//
//    @Bean
//    public DataSourceInitializer dataSourceInitializer(DataSource dataSource) {
//        DataSourceInitializer initializer = new DataSourceInitializer();
//        initializer.setDataSource(dataSource);
//        initializer.setDatabasePopulator(databasePopulator());
//        return initializer;
//    }
//
//    private DatabasePopulator databasePopulator() {
//        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
//        populator.addScript(schemaScript);
//        return populator;
//    }
    //---------------------------------------------------------------------

    @Bean(name="dataSource", destroyMethod = "shutdown")
    public DataSource dataSource() {

//        spring.datasource.url=jdbc:h2:file:~/cowayDB
//        spring.datasource.driver-class-name=org.h2.Driver
//        spring.datasource.username=coway
//        spring.datasource.password=1111

        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(env.getProperty("spring.datasource.url"));
        dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
        dataSource.setUsername(env.getProperty("spring.datasource.username"));
        dataSource.setPassword(env.getProperty("spring.datasource.password"));

        int connectionCount = Integer.parseInt(env.getProperty("spring.datasource.connection-count"));
        dataSource.setMaximumPoolSize(connectionCount);
        dataSource.setMinimumIdle(connectionCount);

        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() throws Exception {
        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setPackagesToScan("com.coway.biz");
        entityManagerFactory.setDataSource(dataSource());
        entityManagerFactory.setJpaVendorAdapter(hibernateJpaVendorAdapter());
        entityManagerFactory.setJpaProperties(getHibernateProperties());
        return entityManagerFactory;
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public HibernateJpaVendorAdapter hibernateJpaVendorAdapter() {
        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        hibernateJpaVendorAdapter.setGenerateDdl(Boolean.TRUE);
        boolean showSql = Boolean.valueOf(env.getProperty("spring.jpa.show-sql"));
        hibernateJpaVendorAdapter.setShowSql(showSql);
        return hibernateJpaVendorAdapter;
    }

    @Bean
    public PlatformTransactionManager transactionManager() throws Exception {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setDataSource(dataSource());
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        AnnotationTransactionAspect.aspectOf().setTransactionManager(transactionManager);
        AnnotationTransactionAspect.aspectOf().setTransactionManagerBeanName("transactionManager");
        return transactionManager;
    }

    protected Properties getHibernateProperties() throws Exception {
        final Properties properties = new Properties();

//         properties.setProperty("hibernate.hbm2ddl.auto", "create-drop");

//        spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
//        spring.jpa.hibernate.ddl-auto=update
        properties.setProperty("spring.jpa.database-platform",      env.getProperty("spring.jpa.database-platform"));
        properties.setProperty("spring.jpa.hibernate.ddl-auto",     env.getProperty("spring.jpa.hibernate.ddl-auto"));
        properties.setProperty("spring.jpa.show-sql",               env.getProperty("spring.jpa.show-sql"));
        properties.setProperty("spring.jpa.hibernate.format_sql",   env.getProperty("spring.jpa.hibernate.format_sql"));

//        properties.setProperty("hibernate.show_sql",        "true");
//        properties.setProperty("hibernate.format_sql",      "true");
//        properties.setProperty("hibernate.implicit_naming_strategy", "org.hibernate.boot.model.naming.ImplicitNamingStrategyLegacyHbmImpl");


//        properties.put(HIBERNATE_DIALECT,               env.getProperty(HIBERNATE_DIALECT));
//        properties.put(HIBERNATE_HDM2DDL_AUTO,          env.getProperty(HIBERNATE_HDM2DDL_AUTO));
//        properties.put(HIBERNATE_SHOW_SQL,              env.getProperty(HIBERNATE_SHOW_SQL));
//        properties.put(HIBERNATE_FORMAT_SQL,            env.getProperty(HIBERNATE_FORMAT_SQL));
////        properties.put(HIBERNATE_EJB_NAMING_STRATEGY,   env.getProperty(HIBERNATE_EJB_NAMING_STRATEGY));
//        properties.put(HIBERNATE_NAMING_PHYSICAL_STRATEGY,   env.getProperty(HIBERNATE_NAMING_PHYSICAL_STRATEGY));
//        properties.put(HIBERNATE_NAMING_IMPLICIT_STRATEGY,   env.getProperty(HIBERNATE_NAMING_IMPLICIT_STRATEGY));

//        EhCacheProvider.setCacheManager(ehCacheManagerFactoryBean().getObject());
//        properties.put(HIBERNATE_CACHE_REGION_FACTORY_CLASS, "com.kfms.base.utils.EhCacheProvider");
//        properties.put(HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE, true);
//        properties.put(HIBERNATE_CACHE_USE_QUERY_CACHE, true);
//        properties.put("hibernate.jdbc.batch_size", "50");

//        properties.put("hibernate.generate_statistics", true);
//        properties.put("hibernate.cache.use_structured_entries", true);
//        grid();
//        properties.put(HIBERNATE_CACHE_REGION_FACTORY_CLASS, "org.gridgain.grid.cache.hibernate.GridHibernateRegionFactory");
//        properties.put("org.gridgain.hibernate.grid_name", "hibernate-grid");
//        properties.put("org.gridgain.hibernate.default_access_type", "READ_ONLY");

        return properties;
    }
}
