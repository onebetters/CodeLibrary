package com.zzc.观察者模式.guava实现;

import com.google.common.eventbus.Subscribe;

/**
 * 观察者差异化实现
 * <p>Filename: com.zzc.观察者模式.guava实现.EventListener.java</p>
 * <p>Date: 2021-11-02 16:06.</p>
 *
 * @author zhangzhichuan
 * @version 0.1.0
 */
public class EventListener {

    /**
     * 加了订阅，这里标记这个方法是事件处理方法
     */
    @Subscribe
    public void handle(NotifyEvent notifyEvent) {
        System.out.println("发送IM消息" + notifyEvent.getImNo());
        System.out.println("发送短信消息" + notifyEvent.getMobileNo());
        System.out.println("发送Email消息" + notifyEvent.getEmailNo());
    }

    @Subscribe
    public void handle2(NotifyEvent notifyEvent) {
        System.out.println("发送IM消息" + notifyEvent.getImNo());
        System.out.println("发送短信消息" + notifyEvent.getMobileNo());
        System.out.println("发送Email消息" + notifyEvent.getEmailNo());
    }

}
