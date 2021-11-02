package com.zzc.观察者模式.自定义实现.impl;

import com.zzc.观察者模式.自定义实现.Observable;
import com.zzc.观察者模式.自定义实现.Observer;

/**
 * 十六进制
 * <p>Filename: com.zzc.观察者模式.自定义实现.impl.HexaObserver.java</p>
 * <p>Date: 2021-11-02 15:15.</p>
 *
 * @author zhangzhichuan
 * @version 0.1.0
 */
public class HexaObserver extends Observer {

    public HexaObserver(Observable observable) {
        this.observable = observable;
        this.observable.addServer(this);
    }

    @Override
    public void doEvent() {
        System.out.println( "Hex String: " + Integer.toHexString( observable.getState() ).toUpperCase() );
    }
}
