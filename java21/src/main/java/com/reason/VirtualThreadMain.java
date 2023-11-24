package com.reason;

import java.lang.Thread.Builder.OfVirtual;

/**
 * @author impassive
 */
public class VirtualThreadMain {

  public static void main(String[] args) {
    OfVirtual ofVirtual = Thread.ofVirtual();
    var thread = ofVirtual.start(() -> {
      System.out.println("Hello, Virtual Thread!");
    });
  }

}
