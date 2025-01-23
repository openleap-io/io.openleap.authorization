package io.openleap.authorization.util;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.function.Function;

public class CheckedExceptionHandler {
    private CheckedExceptionHandler() {
    }

    public static <R1, T1, T2> R1 handleCheckedException(
            CheckedBiFunction<R1, T1, T2> function, T1 param1, T2 param2) {
        try {
            return function.apply(param1, param2);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * Overloaded generic checked exception handler being able to accept custom IOException handler
     * */
    public static <R1, T1, T2> R1 handleCheckedException(
            CheckedBiFunction<R1, T1, T2> function,
            T1 param1,
            T2 param2,
            Function<IOException, RuntimeException> ioExceptionHandler) {
        try {
            return function.apply(param1, param2);
        } catch (IOException e) {
            throw ioExceptionHandler.apply(e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <R, T> R handleCheckedException(CheckedFunction<R, T> function, T param) {
        try {
            return function.apply(param);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
