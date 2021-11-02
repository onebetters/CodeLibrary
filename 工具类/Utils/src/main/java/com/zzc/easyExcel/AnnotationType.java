package com.zzc.easyExcel;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * @author Administrator
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum AnnotationType {

    SUM("sum", "求和"),

    MERGER("merger", "合并单元格"),

    EXCLUDE("exclude", "剔除字段");

    private final String code;
    private final String desc;

    public static AnnotationType of(final String code) {
        final String nameUse = StringUtils.trim(code);
        return Arrays
                .stream(values())
                .filter(e -> StringUtils.equalsIgnoreCase(e.name(), nameUse) || StringUtils.equalsIgnoreCase(e.getCode(), nameUse))
                .findFirst()
                .orElse(null);
    }

}
