package com;


import lombok.extern.slf4j.Slf4j;

/**
 * @author 屈子威
 * @date 2020/9/7 3:25
 * @description 线程池Demo
 */
@Slf4j
public class Demo {
    public static SimpleThreadPoolExecutor executor = new SimpleThreadPoolExecutor(2, 4);

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 10000; i++) {
            threadPool();
//            Thread.sleep(500);
        }

//        threadPool();
    }

    public static void threadPool() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                log.info("第一个任务！！！");
            }
        });
        executor.execute(new Runnable() {
            @Override
            public void run() {
                log.info("第二个任务！！！");
            }
        });
        executor.execute(new Runnable() {
            @Override
            public void run() {
                log.info("第三个任务！！！");
            }
        });
    }
}
