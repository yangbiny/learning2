package com.reason.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author impassive
 */
public class CustomReentrantLock implements Lock {

  private final Sync sync;

  abstract static class Sync extends AbstractQueuedSynchronizer {

    /**
     * 尝试获取锁。
     */
    protected abstract boolean tryLock(int acquires);


    protected void lock(int acquires) {
      if (!tryLock(acquires)) {
        acquire(acquires);
      }
    }


    @Override
    protected boolean tryRelease(int arg) {
      int state = getState() - arg;
      if (Thread.currentThread() != getExclusiveOwnerThread()) {
        throw new IllegalMonitorStateException();
      }
      boolean free = state == 0;
      if (free) {
        setExclusiveOwnerThread(null);
      }
      setState(state);
      return free;
    }

  }

  private static class NonfairSync extends Sync {

    /**
     * 非公平锁尝试获取锁。
     *
     * <p>
     * 如果当前 线程已经拥有锁，则在锁的基础上，增加锁的数量。
     * </p>
     *
     * <p>
     * 如果当前线程没有获取锁，并且当前锁的数量为0，则尝试获取锁。
     * </p>
     *
     * @param acquires 需要申请的锁的数量
     */
    @Override
    protected final boolean tryAcquire(int acquires) {
      int state = getState();
      // 如果当前 锁的 拥有者是自己的 线程，则可以 重复获取锁
      Thread thread = Thread.currentThread();
      if (state > 0 && getExclusiveOwnerThread() == thread && compareAndSetState(
          state, state + acquires)) {
        return true;
      } else if (state == 0 && compareAndSetState(0, acquires)) { // 如果当前 是无锁 状态，则
        setExclusiveOwnerThread(thread);
        return true;
      }
      return false;
    }


    @Override
    protected int tryAcquireShared(int arg) {
      return super.tryAcquireShared(arg);
    }

    @Override
    protected boolean tryLock(int acquires) {
      return this.tryAcquire(acquires);
    }
  }

  private static class FairSync extends Sync {

    @Override
    protected final boolean tryAcquire(int acquires) {
      Thread thread = Thread.currentThread();
      int c;
      if ((c = getState()) > 0 && getExclusiveOwnerThread() == thread) {
        setState(++c);
        return true;
      }
      // 如果当前没有 锁，并且等待队列也是空的（因为这个是公平锁），则尝试获取锁
      if (c == 0 && !hasQueuedPredecessors() && compareAndSetState(0, acquires)) {
        setExclusiveOwnerThread(thread);
        return true;
      }
      return false;
    }

    @Override
    protected boolean tryLock(int acquires) {
      return this.tryAcquire(acquires);
    }
  }

  public CustomReentrantLock() {
    this.sync = new NonfairSync();
  }

  public CustomReentrantLock(boolean fair) {
    this.sync = fair ? new FairSync() : new NonfairSync();
  }


  @Override
  public void lock() {
    this.sync.lock(1);
  }

  @Override
  public void lockInterruptibly() throws InterruptedException {
    this.sync.acquireInterruptibly(1);
  }

  public boolean tryAcquire(int acquires) {
    if (acquires <= 0) {
      throw new IllegalArgumentException("acquires must be positive");
    }
    return this.sync.tryLock(acquires);
  }


  @Override
  public boolean tryLock() {
    return this.sync.tryLock(1);
  }

  @Override
  public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
    return this.sync.tryAcquireNanos(1, unit.toNanos(time));
  }

  @Override
  public void unlock() {
    this.sync.release(1);
  }

  @Override
  public Condition newCondition() {
    return null;
  }
}
