package com.posomo.saltit.domain.restaurant;
//package com.posomo.project.domain.restaurant;
//
//import javax.sql.DataSource;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.jdbc.DataSourceBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
//import org.springframework.transaction.PlatformTransactionManager;
//
//@EnableJpaRepositories(
//        basePackages = "com.posomo.project.domain.restaurant",
//        entityManagerFactoryRef = "restaurantEntityManager",
//        transactionManagerRef = "restaurantTransactionManager"
//)
//@Configuration
//public class DataSourceConfig {
//    @Primary
//    @Bean
//    public LocalContainerEntityManagerFactoryBean restaurantEntityManager(){
//        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
//        em.setDataSource(restaurantDataSource());
//        em.setPackagesToScan(new String[]{"com.posomo.project.domain.restaurant", "com.posomo.project.core"});
//        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
//        return em;
//    }
//
//    @Primary
//    @Bean
//    @ConfigurationProperties(prefix = "spring.restaurant-datasource")
//    public DataSource restaurantDataSource(){
//        return DataSourceBuilder.create().build();
//    }
//
//    @Primary
//    @Bean
//    public PlatformTransactionManager restaurantTransactionManager(){
//        JpaTransactionManager transactionManager = new JpaTransactionManager();
//        transactionManager.setEntityManagerFactory(restaurantEntityManager().getObject());
//        return transactionManager;
//    }
//}
