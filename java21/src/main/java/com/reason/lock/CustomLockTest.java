package com.reason.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author impassive
 */
public class CustomLockTest {

  public static void main(String[] args) throws InterruptedException {

    CustomReentrantLock reentrantLock = new CustomReentrantLock();
    Condition condition = reentrantLock.newCondition();

    reentrantLock.lock();

    reentrantLock.lock();

    reentrantLock.lock();

    int locks = reentrantLock.locks();
    System.out.println(locks);

    new Thread(() -> {
      reentrantLock.lock();
      try {
        System.out.println("wait");
        condition.await();
        System.out.println("do something");
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      } finally {
        reentrantLock.unlock();
      }
    }).start();

    Thread.sleep(10 * 1000);

    new Thread(() -> {
      reentrantLock.lock();
      try {
        System.out.println("signal");
        condition.signal();
      } finally {
        reentrantLock.unlock();
      }

    }).start();


  }

}
