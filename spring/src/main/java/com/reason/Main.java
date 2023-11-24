package com.reason;


import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

  public static void main(String[] args) {
    ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(
        ConfigConfiguration.class
    );
    context.refresh();
  }
}