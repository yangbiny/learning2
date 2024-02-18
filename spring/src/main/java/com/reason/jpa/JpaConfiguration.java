package com.reason.jpa;

import jakarta.persistence.EntityManagerFactory;
import java.util.Properties;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * @author impassive
 */
@Configuration
@ComponentScan
@EnableJpaRepositories(basePackages = {
    "com.reason.jpa"
})
public class JpaConfiguration {

  @Bean
  public DataSource datasource() {
    return new SingleConnectionDataSource(
        "jdbc:mysql://10.200.68.3:3306/fafa?characterEncoding=utf-8",
        "adm",
        "oK1@cM2]dB2!",
        true
    );

  }

  @Bean
  public Properties additionalProperties() {
    Properties hibernateProperties = new Properties();
    hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL57Dialect");
    hibernateProperties.setProperty(
        "hibernate.physical_naming_strategy",
        "org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy"
    );
    return hibernateProperties;
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory(
      DataSource dataSource,
      Properties additionalProperties
  ) {
    LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
    bean.setPackagesToScan("com.reason.jpa");
    bean.setDataSource(dataSource);
    bean.setJpaProperties(additionalProperties);
    HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
    jpaVendorAdapter.setPrepareConnection(true);
    jpaVendorAdapter.setShowSql(true);
    jpaVendorAdapter.setGenerateDdl(false);
    bean.setJpaVendorAdapter(jpaVendorAdapter);
    return bean;
  }

  @Bean
  public PlatformTransactionManager transactionManager(
      EntityManagerFactory entityManagerFactory) {
    JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
    jpaTransactionManager.setEntityManagerFactory(entityManagerFactory);
    return jpaTransactionManager;
  }

}
