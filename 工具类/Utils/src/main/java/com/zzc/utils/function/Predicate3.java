package com.zzc.utils.function;

/**
 * @author Administrator
 */
@FunctionalInterface
public interface Predicate3<T1, T2, T3> {

    boolean test(T1 t1, T2 t2, T3 t3);
}
