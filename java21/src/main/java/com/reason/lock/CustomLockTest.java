package com.reason.lock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author impassive
 */
public class CustomLockTest {

  public static void main(String[] args) {

    ReentrantLock reentrantLock = new ReentrantLock();

    boolean b = reentrantLock.tryLock();
    System.out.println(b);

    boolean b2 = reentrantLock.tryLock();
    System.out.println(b2);


  }

}
