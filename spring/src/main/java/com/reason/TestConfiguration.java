package com.reason;

import com.reason.basic.TestInterface;
import java.util.List;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author impassive
 */
@Configuration
public class TestConfiguration {

  @Bean
  public String test(
      ObjectProvider<TestInterface> testInterfaces
  ) {
    List<TestInterface> list = testInterfaces.stream().toList();
    System.out.println(list);
    return "";
  }

}
