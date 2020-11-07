package com.inprintech.wheelalignmenttest.core;

import android.app.Application;
import android.content.Context;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by xiayundong on 2020/4/8.
 */
public class RunningContext {
    /**
     * 是否是debug环境
     * // TODO 打包记得修改
     */
    public static boolean debug = false;

    /**
     * 全局静态context
     */
    public static Context sAppContext = null;

    /**
     * 全局线程池
     */
    private static ThreadPoolExecutor sThreadPool = (ThreadPoolExecutor) Executors
            .newCachedThreadPool();

    /**
     * 初始化方法
     *
     * @param app
     */
    public static void init(Application app) {
        if (sAppContext == null) {
            sAppContext = app.getApplicationContext();
        }
    }

    /**
     * 反初始化
     */
    public static void unInit() {
        sThreadPool.shutdown();
    }

    /**
     * 全局线程池
     *
     * @return
     */
    public static ThreadPoolExecutor threadPool() {
        return sThreadPool;
    }
}
