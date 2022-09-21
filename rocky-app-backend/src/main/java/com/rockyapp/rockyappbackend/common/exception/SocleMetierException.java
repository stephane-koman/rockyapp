package com.rockyapp.rockyappbackend.common.exception;

public abstract class SocleMetierException extends Exception implements SocleException{

    private static final long serialVersionUID = -530014465953971994L;

    private final String code;

    @Override
    public String getCode() {
        return null;
    }

    public SocleMetierException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public SocleMetierException(String code, String message) {
        super(message);
        this.code = code;
    }
}
