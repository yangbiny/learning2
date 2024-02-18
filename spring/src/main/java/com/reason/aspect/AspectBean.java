package com.reason.aspect;

import org.springframework.stereotype.Component;

/**
 * @author impassive
 */
@Component
public class AspectBean {

  public AspectBean() {
    System.out.println("AspectBean");
  }

  @MyAnnotation
  public void test() {
    System.out.println("test");
  }

}
