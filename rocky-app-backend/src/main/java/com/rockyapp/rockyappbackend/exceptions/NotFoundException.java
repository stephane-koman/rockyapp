package com.rockyapp.rockyappbackend.exceptions;

public class NotFoundException extends Exception{
    private static final long serialVersionUID = 5159734394687780545L;

    public NotFoundException(){}
    public NotFoundException(String message) {
        super(message);
    }
}
