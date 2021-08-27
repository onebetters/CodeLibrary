package com.zzc.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author Administrator
 */
@UtilityClass
public class LocationUtils {

    private static final double EARTH_RADIUS = 6378.137;

    private double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 通过经纬度获取距离(单位：km)
     */
    public String getKmDistance(String lat1, String lng1, String lat2, String lng2) {
        BigDecimal distance = getDistance(lat1, lng1, lat2, lng2);
        if (Objects.isNull(distance)) {
            return "";
        }
        return distance.toString() + "km";
    }

    /**
     * 通过经纬度获取距离(单位：km)
     */
    public String getMiDistance(String lat1, String lng1, String lat2, String lng2) {
        BigDecimal distance = getDistance(lat1, lng1, lat2, lng2);
        if (Objects.isNull(distance)) {
            return "";
        }
        return distance.multiply(BigDecimal.valueOf(1000)) + "m";
    }

    /**
     * 通过经纬度获取距离(单位：km)
     */
    private BigDecimal getDistance(String lat1, String lng1, String lat2, String lng2) {

        if (StringUtils.isBlank(lat1) || StringUtils.isBlank(lng1) || StringUtils.isBlank(lat2) || StringUtils.isBlank(lng2)) {
            return null;
        }

        double radLat1 = rad(Double.parseDouble(lat1));
        double radLat2 = rad(Double.parseDouble(lat2));
        double a = radLat1 - radLat2;
        double b = rad(Double.parseDouble(lng1)) - rad(Double.parseDouble(lng2));
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000d) / 10000d;
        return BigDecimal.valueOf(s).setScale(1, BigDecimal.ROUND_HALF_UP);
    }

    //    public static void main(String[] args) {
    //        System.out.println(getDistance("31.99226","118.7787","32.060255","118.766674"));
    //    }
}
