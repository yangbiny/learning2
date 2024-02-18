package com.reason.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * @author impassive
 */
@Aspect
@Component
public class MyAspect {


  @Before("@annotation(MyAnnotation))")
  public void before(){
    System.out.println("aspect before");
  }



}
