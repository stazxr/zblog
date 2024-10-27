package com.github.stazxr.zblog.bas.context.exception;

/**
 * Enum defining error codes and messages related to context exceptions.
 *
 * @author SunTao
 * @since 2024-07-02
 */
public enum ContextErrorCode {
    /**
     * Loading default context tags error.
     */
    ZCXT001("ZCXT001", "Loading default context tags error"),

    /**
     * Muses context properties are null while creating Muses context.
     */
    ZCXT002("ZCXT002", "ContextProperties are null while creating Context");

    private final String code;
    private final String message;

    ContextErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * Gets the error code.
     *
     * @return The error code
     */
    public String getCode() {
        return code;
    }

    /**
     * Gets the error message.
     *
     * @return The error message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Returns a string representation of the error in the format [code] message.
     *
     * @return The string representation of the error
     */
    @Override
    public String toString() {
        return "[" + getCode() + "] " + getMessage();
    }
}
