package com.zzc.单例模式.静态内部类;

/**
 * 静态内部类
 * <p>Filename: com.zzc.单例模式.静态内部类.StaticClassSingleton.java</p>
 * <p>Date: 2021-11-02 19:31.</p>
 *
 * @author zhangzhichuan
 * @version 0.1.0
 */
public class InnerClassSingleton {

    public InnerClassSingleton() {
    }

    private static class InnerClassSingletonHolder {
        private static final InnerClassSingleton INSTANCE = new InnerClassSingleton();
    }

    public static final InnerClassSingleton getInstance(){
        return InnerClassSingletonHolder.INSTANCE;
    }

}
