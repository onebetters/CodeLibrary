package com.zzc.utils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Administrator
 */
@UtilityClass
public class AmountUtils {

    @Nonnull
    public BigDecimal divideNonNull(@Nullable final Number divider, @Nullable final Number divisor) {
        return divideNonNull(divider, divisor, 2);
    }

    @Nonnull
    @SuppressWarnings("WeakerAccess")
    public BigDecimal divideNonNull(@Nullable final Number divider, @Nullable final Number divisor, final int scale) {
        return Optional.ofNullable(divide(divider, divisor, scale)).orElse(BigDecimal.ZERO);
    }

    @Nullable
    @SuppressWarnings("WeakerAccess")
    public BigDecimal divide(@Nullable final Number divider, @Nullable final Number divisor, final int scale) {
        if (Objects.isNull(divider) || Objects.isNull(divisor) || new BigDecimal(divisor.toString()).equals(BigDecimal.ZERO)) {
            return null;
        }
        return new BigDecimal(divider.toString()).divide(new BigDecimal(divisor.toString()), scale, RoundingMode.HALF_UP);
    }

    @Nonnull
    public BigDecimal scaleNonnull(@Nullable final Number source, @Nonnegative final int scale) {
        return scaleNonnull(Optional.ofNullable(source).map(n -> BigDecimal.valueOf(n.doubleValue())).orElse(BigDecimal.ZERO), scale, RoundingMode.HALF_UP);
    }

    @Nonnull
    public BigDecimal scaleNonnull(@Nullable final BigDecimal source, @Nonnegative final int scale) {
        return scaleNonnull(source, scale, RoundingMode.HALF_UP);
    }

    @Nonnull
    public BigDecimal scaleNonnull(@Nullable final BigDecimal source, @Nonnegative final int scale, @Nonnull final RoundingMode roundingMode) {
        return scale(Optional.ofNullable(source).orElse(new BigDecimal(0)), scale, roundingMode);
    }

    public BigDecimal scale(@Nullable final BigDecimal source, @Nonnegative final int scale) {
        return scale(source, scale, RoundingMode.HALF_UP);
    }

    public BigDecimal scale(@Nullable final BigDecimal source, @Nonnegative final int scale, @Nonnull final RoundingMode roundingMode) {
        return Optional.ofNullable(source).map(s -> s.setScale(scale, roundingMode)).orElse(null);
    }

    public String humanize(@Nullable final Number number) {
        return humanize(number, 1);
    }

    /**
     * @param number 原始值
     * @param scale  小数保留位数。如1，将显示成 1.2万+。不能大于4
     * @return 转换后的显示值. 以万、亿...为单位，根据小数位数及剩余的余数值，显示成如 1.2万、1万、1.2万+、1万+。不满足1万的，直接显示成原始值(需要处理小数位数的，可入参前自行处理)
     */
    @SuppressWarnings("WeakerAccess")
    public String humanize(@Nullable final Number number, final int scale) {
        if (Objects.isNull(number)) {
            return "0";
        }
        final int exponent = (int) Math.log10(number.doubleValue());
        final QuantityUnits units =
                Arrays.stream(QuantityUnits.values()).reduce((f, s) -> exponent >= s.getExponent() ? s : f).filter(u -> exponent >= u.getExponent()).orElse(null);
        if (Objects.nonNull(units)) {
            final int divisor4Rounding = (int) Math.pow(units.getBase(), units.getExponent());
            final int divisor4Scale = (int) Math.pow(units.getBase(), Math.max(units.getExponent() - scale, 0));
            final int rounding = number.intValue() / divisor4Rounding; // 整数位
            final int scaleRemainder = (number.intValue() / divisor4Scale) % (int) Math.pow(units.getBase(), scale); // 小数位
            final double remainder = number.doubleValue() % divisor4Scale; // 余数位
            return rounding + (scaleRemainder > 0 ? "." + StringUtils.leftPad("" + scaleRemainder, scale, "0") : "") + units.getName() + (remainder > 0 ? "+" : "");
        }
        return number.toString();
    }

    @Getter
    @ToString
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private enum QuantityUnits {

        TEN_THOUSAND(4, "万"), HUNDRED_MILLION(8, "亿"), TRILLION(12, "兆"), TEN_QUADRILLION(16, "京"), HUNDRED_QUINTILLION(20, "垓");

        private final int    base = 10;
        private final int    exponent;
        private final String name;
    }
}
