package com.gxuwz.visitor.util;


//将操作放在后台线程中执行，避免主线程的阻塞。

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppExecutors {
    private static final int THREAD_COUNT = 3;

    private final ExecutorService diskIO;

    public AppExecutors() {
        this.diskIO = Executors.newFixedThreadPool(THREAD_COUNT);
    }

    public ExecutorService diskIO() {
        return diskIO;
    }
}
