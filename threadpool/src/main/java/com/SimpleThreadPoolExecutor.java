package com;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author 屈子威
 * @date 2020/9/7 0:43
 * @description 简单的线程池实现
 */
@Slf4j
public class SimpleThreadPoolExecutor {

    class Worker implements Runnable {

        Runnable runnable;
        /**
         * 线程的性质
         * true：核心线程池.false：非核心线程池
         */
        boolean property;

        /**
         * 定义的一个线程
         */
        Thread thread;

        /**
         * 线程运行状态
         * -1：任务执行完毕
         * 0：任务还未执行完毕
         * 1：任务抛出了异常
         */
        int state;

        public Worker(Runnable runnable, boolean property) {
            this.runnable = runnable;
            this.property = property;
            this.thread = new Thread(this);
        }

        @Override
        public void run() {
            runWorker(this);
        }
    }

    LinkedBlockingQueue<Runnable> workerQueue = new LinkedBlockingQueue<>();

    /**
     * 核心线程池大小
     */
    int coreSize;

    /**
     * 线程池线程数量最大大小
     */
    int maxSize;

    /**
     * 当前核心线程数量
     */
    AtomicInteger ctl = new AtomicInteger(0);

    /**
     * 当前总线程数量
     */
    AtomicInteger size = new AtomicInteger(0);

    public SimpleThreadPoolExecutor(int coreSize, int maxSize) {
        this.coreSize = coreSize;
        this.maxSize = maxSize;
    }

    public void execute(Runnable runnable) {
        addWorker(runnable);
    }
    /**
     * 具有返回值的
     */
    public <V> Future<V> submit(Callable<V> callable){
        RunnableFuture<V> futureTask = new FutureTask<>(callable);
        execute(futureTask);
        return futureTask;
    };

    public void addWorker(Runnable runnable) {
        Worker worker = null;
        final ReentrantLock lock = new ReentrantLock(true);
        lock.lock();
        try {
            //如果当先核心线程数小于coreSize则创建为核心线程
            if (ctl.get() < coreSize) {
                worker = new Worker(runnable, true);
                ctl.getAndAdd(1);
                size.getAndAdd(1);
                //如果当前线程数大于等于coreSize或者当前线程总数量小于 maxSize则创建为非核心线程
            } else if (ctl.get() >= coreSize && size.get() < maxSize) {
                worker = new Worker(runnable, false);
                size.getAndAdd(1);
                //如果线程池满则将任务放入任务队列,返回false说明任务队列也满了则执行拒绝策略
            } else if (workerQueue.size() < 99999999) {
                workerQueue.put(runnable);
            }else {
                throw new RuntimeException("线程池和队列已满！执行拒绝策略！");
            }
        } catch (RuntimeException | InterruptedException e) {
            log.info("{}",e.getMessage());
        } finally {
            lock.unlock();
        }
        if (worker != null){
            final Thread t = worker.thread;
            t.start();
        }
    }

    public void runWorker(Worker worker) {
        final ReentrantLock lock = new ReentrantLock();

        //获取第一个任务
        Runnable task = worker.runnable;
        while (task != null || (task = getTask(worker.property)) != null) {
            lock.lock();
            try {
                task.run();
            } catch (Exception e) {
                //如果执行异常则跳出循环。

                break;
            } finally {
                lock.unlock();
                task = null;
            }
        }
        if (worker.property) {
            ctl.getAndDecrement();
        }
        size.getAndDecrement();

    }

    /**
     * 获取任务
     */
    public Runnable getTask(boolean property) {
        Runnable r = null;
        try {
            r = workerQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (r!=null){
            return r;
        }
        return null;
    }
}
