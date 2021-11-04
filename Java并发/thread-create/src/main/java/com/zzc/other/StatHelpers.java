package com.zzc.other;

import com.zzc.utils.DateTimeUtils;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * @author Administrator
 */
@UtilityClass
public class StatHelpers {

    /**
     * @return 事件源redis key
     */
    public <T> String eventBucketKey( final Class<T> clazz,  final LocalDateTime baseTime) {
        return String.join(StatConstants.KEY_DELIMITER, StatConstants.PREFIX_EVENT, eventType(clazz), DateTimeUtils.parse(baseTime, "yyyyMMdd"));
    }

    private <T> String eventType(final Class<T> clazz) {
        return clazz.getSimpleName();
    }

    /**
     * @return 具体任务结果存储redis key
     */
    public String taskBucketKey( final String taskId, final long periodInMinutes, final long periodOffset, final String customSliceSuffix) {
        final String key = String.join(StatConstants.KEY_DELIMITER, StatConstants.PREFIX_TASK, taskId, String.valueOf(periodInMinutes), String.valueOf(periodOffset));
        return StringUtils.isBlank(customSliceSuffix) ? key : String.join(StatConstants.BUCKET_BIZ_DELIMITER, key, customSliceSuffix);
    }

    public long periodOffset(final long periodInMinutes, final LocalDateTime baseTime) {
        return Duration.between(StatConstants.START_DATE_TIME, baseTime).toMinutes() / periodInMinutes;
    }

    
    public LocalDateTime getPeriodTimeByBucketKey( final String bucketKey) {
        final String keyWithoutBizSuffix = Arrays
                .stream(StringUtils.split(bucketKey, StatConstants.BUCKET_BIZ_DELIMITER))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("非法bucketKey:" + bucketKey));

        final String[] array = StringUtils.split(keyWithoutBizSuffix, StatConstants.KEY_DELIMITER);
        final long periodInMinutes = Long.parseLong(array[array.length - 2]);
        final long periodOffset = Long.parseLong(array[array.length - 1]);
        return StatConstants.START_DATE_TIME.plusMinutes(periodInMinutes * periodOffset);
    }
}
