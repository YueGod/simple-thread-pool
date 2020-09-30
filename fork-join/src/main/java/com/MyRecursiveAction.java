package com;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.concurrent.RecursiveAction;

/**
 * @author 屈子威
 * @date 2020/9/9 1:02
 * @description 没有返回值的递归调用
 */
@Slf4j
public class MyRecursiveAction extends RecursiveAction {
    private final int threshold;
    private int[] arrayToSort;

    public MyRecursiveAction(int[] arrayToSort, final int threshold) {
        this.arrayToSort = arrayToSort;
        this.threshold = threshold;
    }

    @Override
    protected void compute() {
        if (arrayToSort.length <= threshold) {
            Arrays.sort(arrayToSort);
            return;
        }

        int midpoint = arrayToSort.length / 2;
        int[] leftArray = Arrays.copyOfRange(arrayToSort, 0, midpoint);
        int[] rightArray = Arrays.copyOfRange(arrayToSort, midpoint, arrayToSort.length);

        MyRecursiveAction left = new MyRecursiveAction(leftArray,threshold);
        MyRecursiveAction right = new MyRecursiveAction(rightArray,threshold);

        left.fork();
        right.fork();

        left.join();
        right.join();
    }
}
