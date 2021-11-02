package com.zzc.观察者模式.自定义实现.impl;

import com.zzc.观察者模式.自定义实现.Observable;
import com.zzc.观察者模式.自定义实现.Observer;

/**
 * 十进制
 * <p>Filename: com.zzc.观察者模式.自定义实现.impl.OctalObserver.java</p>
 * <p>Date: 2021-11-02 15:09.</p>
 *
 * @author zhangzhichuan
 * @version 0.1.0
 */
public class OctalObserver extends Observer {

    public OctalObserver(Observable observable) {
        this.observable = observable;
        this.observable.addServer(this);
    }

    @Override
    public void doEvent() {
        // 差异化实现
        System.out.println("Octal String: " + Integer.toOctalString( observable.getState() ));
    }
}
