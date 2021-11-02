package com.zzc.观察者模式.自定义实现;

/**
 * 观察者的差异化实现
 * <p>Filename: com.zzc.观察者模式.自定义实现.Observer.java</p>
 * <p>Date: 2021-11-02 14:46.</p>
 *
 * @author Administrator
 * @version 0.1.0
 */
public abstract class Observer {

    protected Observable observable;

    /**
     * 各观察者差异化实现方法
     */
    public abstract void doEvent();
}
