package com.zzc.观察者模式.自定义实现;

import java.util.ArrayList;
import java.util.List;

/**
 * 一个被观察者的类Observable 和 多个观察者Observer
 * <p>Filename: com.zzc.观察者模式.自定义实现.Observable.java</p>
 * <p>Date: 2021-11-02 14:49.</p>
 *
 * @author zhangzhichuan
 * @version 0.1.0
 */
public class Observable {
    private List<Observer> observers = new ArrayList<>();
    private int            state;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
        notifyAllObservers(state);
    }

    /**
     * 添加观察者
     */
    public void addServer(Observer observer) {
        observers.add(observer);
    }

    /**
     * 移除观察者
     */
    public void removeServer(Observer observer) {
        observers.remove(observer);
    }

    /**
     * 通知
     */
    public void notifyAllObservers(int state) {
        for (Observer observer : observers) {
            observer.doEvent();
        }
    }

}
