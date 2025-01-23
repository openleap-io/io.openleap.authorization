package io.openleap.authorization.util;

@FunctionalInterface
public interface CheckedFunction<R, T> {
    R apply(T t) throws Exception;
}
