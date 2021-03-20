package com.example.event;

import com.alibaba.fastjson.JSON;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>Date: 2020-06-03 14:47.</p>
 *
 * @author <a href="mailto:baixiaolin@qianmi.com">OF2510-白晓林</a>
 * @version 0.1.0
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractEvent implements Event, Serializable {
    private static final long serialVersionUID = 5245147150686405792L;

    @Builder.Default
    private LocalDateTime baseTime = LocalDateTime.now();

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
