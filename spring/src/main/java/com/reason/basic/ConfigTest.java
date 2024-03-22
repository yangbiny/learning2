package com.reason.basic;

import org.springframework.stereotype.Component;

/**
 * @author impassive
 */
@Component
public class ConfigTest {

  private ConfigTest1 configTest1;

  public ConfigTest(ConfigTest1 configTest1) {
    this.configTest1 = configTest1;
  }

  public ConfigTest1 getConfigTest1() {
    return configTest1;
  }
}
