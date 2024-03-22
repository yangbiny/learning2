package com.reason.basic;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author impassive
 */
@ComponentScan
@Configuration
public class Config {


  @Bean
  public ConfigTest1 configTest1() {
    return new ConfigTest1();
  }

  @Bean
  public ConfigTest1 configTest12() {
    return new ConfigTest1();
  }


}
