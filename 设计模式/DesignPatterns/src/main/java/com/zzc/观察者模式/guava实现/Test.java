package com.zzc.观察者模式.guava实现;

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
        EventListener eventListener = new EventListener();

        EventBusCenter.register(eventListener);

        EventBusCenter.post(NotifyEvent.builder()
                                       .emailNo("956126298@qq.com")
                                       .imNo("110Im")
                                       .mobileNo("15895933503")
                                       .build());
    }
}
