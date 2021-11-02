package com.zzc.观察者模式.自定义实现;

import com.zzc.观察者模式.自定义实现.impl.BinaryObserver;
import com.zzc.观察者模式.自定义实现.impl.HexaObserver;
import com.zzc.观察者模式.自定义实现.impl.OctalObserver;

/**
 * TODO
 * <p>Filename: com.zzc.观察者模式.自定义实现.Tset.java</p>
 * <p>Date: 2021-11-02 15:28.</p>
 *
 * @author zhangzhichuan
 * @version 0.1.0
 */
public class Test {

    public static void main(String[] args) {
        Observable observable = new Observable();

        new BinaryObserver(observable);
        new OctalObserver(observable);
        new HexaObserver(observable);

        for (int i = 8; i < 17; i = i+2) {
            observable.setState(i);
        }
    }
}
