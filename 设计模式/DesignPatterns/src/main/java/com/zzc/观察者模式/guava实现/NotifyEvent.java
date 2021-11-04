package com.zzc.观察者模式.guava实现;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通知事件
 * <p>Filename: com.zzc.观察者模式.guava实现.NotifyEvent.java</p>
 * <p>Date: 2021-11-02 16:13.</p>
 *
 * @author zhangzhichuan
 * @version 0.1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotifyEvent {

    private String mobileNo;

    private String emailNo;

    private String imNo;
}
