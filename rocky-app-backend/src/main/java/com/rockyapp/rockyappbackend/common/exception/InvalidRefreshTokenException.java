package com.rockyapp.rockyappbackend.common.exception;

public class InvalidRefreshTokenException extends Exception {

    private static final long serialVersionUID = -1256646265380008470L;

    private static final String ERROR_MESSAGE = "Invalid refresh token exception.";

    public InvalidRefreshTokenException(){
        super(ERROR_MESSAGE);
    }
}
