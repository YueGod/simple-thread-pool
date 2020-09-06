package com.qzw;

/**
 * @author 屈子威
 * @date 2020/9/7 3:25
 * @description 线程池Demo
 */
public class Demo {

    public static void main(String[] args) {
        SimpleThreadPoolExecutor executor = new SimpleThreadPoolExecutor(2, 4);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("手写线程池！！！！");
            }
        });
    }
}
