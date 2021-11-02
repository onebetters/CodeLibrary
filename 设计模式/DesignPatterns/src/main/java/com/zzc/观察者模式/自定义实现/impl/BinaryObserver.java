package com.zzc.观察者模式.自定义实现.impl;

import com.zzc.观察者模式.自定义实现.Observable;
import com.zzc.观察者模式.自定义实现.Observer;

import javax.security.auth.Subject;

/**
 * 二进制
 * <p>Filename: com.zzc.观察者模式.自定义实现.impl.BinaryObserver.java</p>
 * <p>Date: 2021-11-02 15:03.</p>
 *
 * @author zhangzhichuan
 * @version 0.1.0
 */
public class BinaryObserver extends Observer {

    public BinaryObserver(Observable observable){
        this.observable = observable;
        this.observable.addServer(this);
    }

    @Override
    public void doEvent() {
        // 差异化实现内容
        System.out.println( "Binary String: " + Integer.toBinaryString(observable.getState()) );
    }
}
