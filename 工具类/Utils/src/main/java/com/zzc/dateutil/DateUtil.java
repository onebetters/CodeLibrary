package com.zzc.dateutil;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

/**
 * 时间工具类，这是方便业务写的工具方法，
 */
@UtilityClass
public class DateUtil {

    public enum TimeInterval {
        year, month, week, day, hour, minute, second
    }

    public static final long MILLIS_PER_HOUR = 3600L;
    public static final long MILLIS_PER_DAY = 86400L;
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final FastDateFormat MONTH_FORMAT = FastDateFormat.getInstance("yyyy-MM");
    public static final FastDateFormat DATE_FORMAT = FastDateFormat.getInstance(DATE_PATTERN);
    public static final FastDateFormat DATE_TIME_FORMAT = FastDateFormat.getInstance(DATE_TIME_PATTERN);
    public static final FastDateFormat DATE_TIME_FORMAT_TRUNC = FastDateFormat.getInstance("yyyyMMddHHmmss");

    /**
     * 接口应答中的时间字段格式：yyyy-MM-dd'T'HH:mm:ssZ，如：2017-07-07T10:10:52+08
     * - 统一使用该格式保存到搜索平台上，便于搜索平台数据查询SDK处理
     */
    private String API_RESPONSE_DATE_TIME_FORMATTER_PATTERN = "yyyy-MM-dd'T'HH:mm:ssZ";
    private DateTimeFormatter API_RESPONSE_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(API_RESPONSE_DATE_TIME_FORMATTER_PATTERN);

    private static final String DATE_TIMESTAMP_PATTERN_TIME_ZONE = DATE_PATTERN + "'T'HH:mm:ss'+0800'";

    private static final String DATE_TIMESTAMP_PATTERN_TIME_SHORT_ZONE = DATE_PATTERN + "'T'HH:mm:ss.S'+08'";

    /**
     * 北京时间时区（东八区）
     */
    private ZoneOffset ZONE_OFFSET_BEIJING = ZoneOffset.ofHours(8);

    public static Date yesterdayStart() {
        return atStartOfDay(DateUtils.addDays(new Date(), -1));
    }

    public static Date last7DayStart() {
        return atStartOfDay(DateUtils.addDays(new Date(), -7));
    }

    public static Date last30DayStart() {
        return atStartOfDay(DateUtils.addDays(new Date(), -30));
    }

    public static Date lastMonthStart() {
        return atStartOfMonth(DateUtils.addMonths(new Date(), -1));
    }

    public static Date currentMonthStart() {
        return atStartOfMonth(new Date());
    }

    public static Date last3MonthStart() {
        return atStartOfMonth(DateUtils.addMonths(new Date(), -3));
    }

    public static Date last6MonthStart() {
        return atStartOfMonth(DateUtils.addMonths(new Date(), -6));
    }

    /**
     * 返回该月 第一天 开始时间
     */
    public static Date atStartOfMonth(Date month) {
        Calendar cale = Calendar.getInstance();
        cale.setTime(month);

        cale.set(Calendar.DAY_OF_MONTH, cale.getActualMinimum(Calendar.DAY_OF_MONTH));
        cale.set(Calendar.HOUR_OF_DAY, 0);
        cale.set(Calendar.MINUTE, 0);
        cale.set(Calendar.SECOND, 0);
        cale.set(Calendar.MILLISECOND, 0);
        return cale.getTime();
    }

    /**
     * 返回该月 最后一天 开始时间
     */
    public static Date atEndOfMonth(Date month) {
        Calendar cale = Calendar.getInstance();
        cale.setTime(month);

        cale.set(Calendar.DAY_OF_MONTH, cale.getActualMaximum(Calendar.DAY_OF_MONTH));
        cale.set(Calendar.HOUR_OF_DAY, 0);
        cale.set(Calendar.MINUTE, 0);
        cale.set(Calendar.SECOND, 0);
        cale.set(Calendar.MILLISECOND, 0);
        return cale.getTime();
    }

    /**
     * 返回该周 第一天 开始时间
     */
    public static Date atStartOfWeek(Date week) {
        Calendar cale = Calendar.getInstance();
        cale.setFirstDayOfWeek(Calendar.MONDAY);
        cale.setTime(week);

        Calendar firstDate = Calendar.getInstance();

        firstDate.setFirstDayOfWeek(Calendar.MONDAY);
        firstDate.setTime(week);

        if (cale.get(Calendar.WEEK_OF_YEAR) == 1 && cale.get(Calendar.MONTH) == 11) {
            firstDate.set(Calendar.YEAR, cale.get(Calendar.YEAR) + 1);
        }

        int typeNum = cale.get(Calendar.WEEK_OF_YEAR);
        firstDate.set(Calendar.WEEK_OF_YEAR, typeNum);
        firstDate.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        firstDate.set(Calendar.HOUR_OF_DAY, 0);
        firstDate.set(Calendar.MINUTE, 0);
        firstDate.set(Calendar.SECOND, 0);
        firstDate.set(Calendar.MILLISECOND, 0);
        return firstDate.getTime();
    }

    /**
     * 返回该周 最后一天 开始时间
     */
    public static Date atEndOfWeek(Date week) {
        Calendar cale = Calendar.getInstance();
        cale.setFirstDayOfWeek(Calendar.MONDAY);
        cale.setTime(week);

        Calendar lastDate = Calendar.getInstance();

        lastDate.setFirstDayOfWeek(Calendar.MONDAY);
        lastDate.setTime(week);

        if (cale.get(Calendar.WEEK_OF_YEAR) == 1 && cale.get(Calendar.MONTH) == 11) {
            lastDate.set(Calendar.YEAR, cale.get(Calendar.YEAR) + 1);
        }

        int typeNum = cale.get(Calendar.WEEK_OF_YEAR);
        lastDate.set(Calendar.WEEK_OF_YEAR, typeNum);
        lastDate.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        lastDate.set(Calendar.HOUR_OF_DAY, 0);
        lastDate.set(Calendar.MINUTE, 0);
        lastDate.set(Calendar.SECOND, 0);
        lastDate.set(Calendar.MILLISECOND, 0);
        return lastDate.getTime();
    }

    /**
     * 返回该天 开始时间
     */
    public static Date atStartOfDay(Date day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(day);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 返回该天 开始时间
     */
    public static Date atEndOfDay(Date day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(day);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }

    /**
     * 返回该天 开始时间
     */
    public static Date atStartOfSecond(Date day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(day);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }

    public static Date lastWeekStart() {
        final LocalDate date = LocalDate.now().minusWeeks(1);
        final LocalDate lastMonday = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        return localDateToDate(lastMonday);
    }

    /**
     * 将时间转换为时间字符串
     *
     * @param localDateTime 时间
     * @return e.g. 2018-08-11T00:00:00+0800
     */
    public static String localDateTimeToString(LocalDateTime localDateTime) {
        // 注意：当时间为null时必须返回null，因为搜索引擎建索引时需要把该字段当作时间类型
        // 如果返回空字符串，则会被当作字符串类型，影响时间范围搜索、甚至导致连接建索引失败
        if (Objects.isNull(localDateTime)) {
            return null;
        }
        return API_RESPONSE_DATE_TIME_FORMATTER.format(localDateTime.atZone(ZONE_OFFSET_BEIJING));
    }

    /**
     * 将日期转换为时间字符串
     *
     * @param localDate 日期
     * @return e.g. 2018-08-11T00:00:00+0800
     */
    public static String localDateToString(LocalDate localDate) {
        // 注意：当时间为null时必须返回null，因为搜索引擎建索引时需要把该字段当作时间类型
        // 如果返回空字符串，则会被当作字符串类型，影响时间范围搜索、甚至导致连接建索引失败
        if (Objects.isNull(localDate)) {
            return null;
        }
        LocalDateTime localDateTime = localDate.atStartOfDay();

        return API_RESPONSE_DATE_TIME_FORMATTER.format(localDateTime.atZone(ZONE_OFFSET_BEIJING));
    }

    /**
     * 获取指定时间
     *
     * @param aDate
     * @param format
     * @return
     */
    public static String getDateString(Date aDate, String format) {
        if (aDate != null) {
            SimpleDateFormat df = new SimpleDateFormat(format);
            return df.format(aDate);
        }
        return null;
    }

    public static String getDateTimeString(Date date) {
        return getDateString(date, DATE_TIME_PATTERN);
    }

    /**
     * 获取指定时间,精确到秒(YYYY-MM-DD'T'HH:MI:SS''+0800Z')
     *
     * @param aDate 指定时间
     * @return
     */
    public static String getDateTimeWithTimeZone(Date aDate) {
        String returnValue = null;

        if (aDate != null) {
            SimpleDateFormat df = new SimpleDateFormat(DATE_TIMESTAMP_PATTERN_TIME_ZONE, Locale.CHINESE);
            returnValue = df.format(aDate);
        }
        return returnValue;
    }

    /**
     * 时间戳(毫秒)转LocalDateTime
     *
     * @param ms 时间戳(毫秒)
     * @return
     */
    public static LocalDateTime timestampToLocalDateTime(long ms) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(ms), ZONE_OFFSET_BEIJING);
    }

    /**
     * 日期字符串转化成Date (YYYY-MM-DD'T'HH:MI:SS''+0800Z')
     *
     * @param strDate 日期字符串
     * @return Date
     */
    public static Date getDateWithTimeZone(String strDate) {

        SimpleDateFormat df = new SimpleDateFormat(DATE_TIMESTAMP_PATTERN_TIME_ZONE, Locale.CHINESE);
        try {
            return df.parse(strDate);
        } catch (ParseException e) {
            throw new RuntimeException("日期字符串转换异常", e);
        }
    }

    /**
     * 日期字符串转化成Date (YYYY-MM-DD'T'HH:MI:ss.S''+08Z')
     *
     * @param strDate 日期字符串 e.g. 2017-12-21T16:45:51.0+08
     * @return Date
     */
    public static Date getDateWithShortTimeZone(String strDate) {

        SimpleDateFormat df = new SimpleDateFormat(DATE_TIMESTAMP_PATTERN_TIME_SHORT_ZONE, Locale.CHINESE);
        try {
            return df.parse(strDate);
        } catch (ParseException e) {
            throw new RuntimeException("日期字符串转换异常", e);
        }
    }

    public static Date localDateToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public Date toDate(final LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static long between(Date startDate, Date endDate, TimeInterval timeInterval) {
        LocalDateTime start = toLocalDateTime(startDate);
        LocalDateTime end = toLocalDateTime(endDate);
        switch (timeInterval) {
            case year:
                return ChronoUnit.YEARS.between(start, end);
            case month:
                return ChronoUnit.MONTHS.between(start, end);
            case week:
                return ChronoUnit.WEEKS.between(start, end);
            case day:
                return ChronoUnit.DAYS.between(start, end);
            case hour:
                return ChronoUnit.HOURS.between(start, end);
            case minute:
                return ChronoUnit.MINUTES.between(start, end);
            case second:
                return ChronoUnit.SECONDS.between(start, end);
            default:
                return endDate.getTime() - startDate.getTime();
        }
    }

    public LocalDate toLocalDate(final Date time) {
        return time.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDateTime toLocalDateTime(final Date time) {
        return time.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public Date long2Date(long dataLong) {
        return new Date(dataLong);
    }
}
