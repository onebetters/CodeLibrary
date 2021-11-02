package com.zzc.单例模式.双重校验锁;

/**
 * 双重校验锁
 * <p>Filename: com.zzc.单例模式.双重校验锁.DubboCheckSingletion.java</p>
 * <p>Date: 2021-11-02 18:13.</p>
 *
 * @author zhangzhichuan
 * @version 0.1.0
 */
public class DoubleCheckSingleton {
    private volatile static DoubleCheckSingleton instance;

    private DoubleCheckSingleton(){
    }

    public static DoubleCheckSingleton getInstance(){
        if (instance == null) {
            synchronized (DoubleCheckSingleton.class) {
                if (instance == null) {
                    instance = new DoubleCheckSingleton();
                }
            }
        }
        return instance;
    }
}
