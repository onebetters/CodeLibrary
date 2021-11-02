package com.zzc.单例模式.饿汉模式;

/**
 * 饿汉模式
 * <p>Filename: com.zzc.单例模式.饿汉模式.EHanSingletion.java</p>
 * <p>Date: 2021-11-02 18:10.</p>
 *
 * @author zhangzhichuan
 * @version 0.1.0
 */
public class EHanSingletion {

    private static EHanSingletion instance = new EHanSingletion();

    private EHanSingletion(){

    }

    public static EHanSingletion getInstance() {
        return instance;
    }
}
