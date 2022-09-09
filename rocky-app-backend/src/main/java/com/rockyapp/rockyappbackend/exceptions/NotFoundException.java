package com.rockyapp.rockyappbackend.exceptions;

public class NotFoundException extends Exception{
    private static final long serialVersionUID = 5159734394687780545L;
    private static final String ERROR_MESSAGE = "Not found Exception";

    public NotFoundException() {
        super(ERROR_MESSAGE);
    }
}
