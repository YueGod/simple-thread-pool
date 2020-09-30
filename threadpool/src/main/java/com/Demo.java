package com;


import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * @author 屈子威
 * @date 2020/9/7 3:25
 * @description 线程池Demo
 */
@Slf4j
public class Demo {
    public static SimpleThreadPoolExecutor executor = new SimpleThreadPoolExecutor(2, 4);
    static ExecutorService service = Executors.newFixedThreadPool(10);
    static ForkJoinPool forkJoinPool = new ForkJoinPool();
    public static void main(String[] args) throws InterruptedException, ExecutionException {
//        for (int i = 0; i < 10; i++) {
//            threadPool();
////            Thread.sleep(500);
//        }
        threadPool();
    }

    public static void threadPool() throws ExecutionException, InterruptedException {

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
        Future<Integer> submit = executor.submit(() -> {
            log.info("具有返回值的任务！");
            Thread.sleep(2000);
            return 0;
        });
        log.info("获取到的值{}",submit.get());

    }
}

@Slf4j
class Demo2{
    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            log.info("123");
        });
        thread.start();
        thread.start();
    }
}
