package com.github.stazxr.zblog.bas.exception;

import com.github.stazxr.zblog.bas.exception.code.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BooleanSupplier;

/**
 * 异常工具类
 *
 * @author SunTao
 * @since 2026-02-05
 */
public final class ThrowUtils {
    private static final Logger log = LoggerFactory.getLogger(ThrowUtils.class);

    private ThrowUtils() { }

    public static void throwIf(boolean condition, ErrorCode code, Object... args) {
        if (condition) {
            throw new ServiceException(code, args);
        }
    }

    public static void throwIf(BooleanSupplier supplier, ErrorCode code, Object... args) {
        if (supplier.getAsBoolean()) {
            throw new ServiceException(code, args);
        }
    }

    public static void throwSystemIf(boolean condition, ErrorCode code, Object... args) {
        if (condition) {
            throw new SystemException(code, args);
        }
    }

    public static void throwSystemIf(BooleanSupplier supplier, ErrorCode code, Object... args) {
        if (supplier.getAsBoolean()) {
            throw new SystemException(code, args);
        }
    }

    public static <T> T requireNonNull(T obj, ErrorCode code, Object... args) {
        if (obj == null) {
            throw new ServiceException(code, args);
        }
        return obj;
    }

    @SuppressWarnings("all")
    public static <T> T requirePresent(Optional<T> optional, ErrorCode code, Object... args) {
        if (!optional.isPresent()) {
            throw new ServiceException(code, args);
        }
        return optional.get();
    }

    public static void throwIfNull(Object o, ErrorCode code, Object... args) {
        if (o == null) {
            throw new ServiceException(code, args);
        }
    }

    public static void throwIfBlank(String str, ErrorCode code, Object... args) {
        if (str == null || str.trim().isEmpty()) {
            throw new ServiceException(code, args);
        }
    }

    public static void throwIfEmpty(Collection<?> c, ErrorCode code, Object... args) {
        if (c == null || c.isEmpty()) {
            throw new ServiceException(code, args);
        }
    }

    public static void throwIfEmpty(Map<?, ?> map, ErrorCode code, Object... args) {
        if (map == null || map.isEmpty()) {
            throw new ServiceException(code, args);
        }
    }

    public static void throwIfEquals(Object a, Object b, ErrorCode code, Object... args) {
        if (Objects.equals(a, b)) {
            throw new ServiceException(code, args);
        }
    }

    public static void throwIfNonEquals(Object a, Object b, ErrorCode code, Object... args) {
        if (!Objects.equals(a, b)) {
            throw new ServiceException(code, args);
        }
    }

    public static Condition when(boolean condition) {
        return new Condition(condition);
    }

    public static class Condition {
        private final boolean condition;

        private Condition(boolean condition) {
            this.condition = condition;
        }

        public void service(ErrorCode code, Object... args) {
            if (condition) {
                throw new ServiceException(code, args);
            }
        }

        public void system(ErrorCode code, Object... args) {
            if (condition) {
                throw new SystemException(code, args);
            }
        }

        public void system(String message) {
            if (condition) {
                throw new SystemException(message);
            }
        }

        public void system(String message, Throwable cause) {
            if (condition) {
                throw new SystemException(message, cause);
            }
        }
    }

    public static void service(ErrorCode code, Object... args) {
        throw new ServiceException(code, args);
    }

    public static void system(ErrorCode code, Object... args) {
        throw new SystemException(code, args);
    }

    public static void system(String message) {
        throw new SystemException(message);
    }

    public static void system(String message, Throwable cause) {
        throw new SystemException(message, cause);
    }
}
