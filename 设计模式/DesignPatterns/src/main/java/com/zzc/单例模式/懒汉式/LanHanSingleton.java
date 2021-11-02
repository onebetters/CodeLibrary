package com.zzc.单例模式.懒汉式;

/**
 * 懒汉式
 * <p>Filename: com.zzc.单例模式.懒汉式.LanHanSingleton.java</p>
 * <p>Date: 2021-11-02 18:09.</p>
 *
 * @author zhangzhichuan
 * @version 0.1.0
 */
public class LanHanSingleton {

    private static LanHanSingleton instance;

    private LanHanSingleton(){

    }

    public static LanHanSingleton getInstance(){
        if (instance == null) {
            instance = new LanHanSingleton();
        }
        return instance;
    }

}
