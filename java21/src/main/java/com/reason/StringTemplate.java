package com.reason;

import static java.lang.StringTemplate.RAW;
import static java.lang.StringTemplate.STR;

public class StringTemplate {

  public static void main(String[] args) {
    String name = "test";
    String s = STR. "My Name is \{ name }" ;
    java.lang.StringTemplate stringTemplate = RAW. "My Name is \{ name }" ;
    System.out.println(s);
  }
}