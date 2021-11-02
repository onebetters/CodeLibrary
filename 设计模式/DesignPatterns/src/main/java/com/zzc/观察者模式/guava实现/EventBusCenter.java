package com.zzc.观察者模式.guava实现;

import com.google.common.eventbus.EventBus;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 声明一个EventBusCenter类，它类似于自定义实现时被观察者那种角色Observable
 * <p>Filename: com.zzc.观察者模式.guava实现.EventBusCenter.java</p>
 * <p>Date: 2021-11-02 15:47.</p>
 *
 * @author zhangzhichuan
 * @version 0.1.0
 */
@Data
@NoArgsConstructor
public class EventBusCenter {

    private static EventBus eventBus = new EventBus();

    public static EventBus getEventBus() {
        return eventBus;
    }

    /**
     * 添加观察者
     */
    public static void register(Object obj) {
        eventBus.register(obj);
    }

    /**
     * 移除观察者
     */
    public static void unregister(Object obj) {
        eventBus.unregister(obj);
    }

    /**
     * 把消息推给观察者
     */
    public static void post(Object obj) {
        eventBus.post(obj);
    }
}
