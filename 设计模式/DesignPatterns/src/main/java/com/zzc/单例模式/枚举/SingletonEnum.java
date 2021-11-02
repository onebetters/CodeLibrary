package com.zzc.单例模式.枚举;

/**
 * 枚举
 * <p>Filename: com.zzc.单例模式.枚举.SingletonEnum.java</p>
 * <p>Date: 2021-11-02 19:40.</p>
 *
 * @author zhangzhichuan
 * @version 0.1.0
 */
public enum SingletonEnum {

    INSTANCE;

    public SingletonEnum getInstance(){
        return INSTANCE;
    }
}
