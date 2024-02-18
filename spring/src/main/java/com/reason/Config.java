package com.reason;

import com.reason.basic.ConfigTest;
import com.reason.basic.ConfigTest1;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author impassive
 */
@Configuration
public class Config {

  @Bean
  public ConfigTest configTest() {
    return new ConfigTest();
  }

  @Bean
  public ConfigTest1 configTest1() {
    ConfigTest1 configTest1 = new ConfigTest1();
    configTest1.setConfigTest(configTest());
    return configTest1;
  }

}
