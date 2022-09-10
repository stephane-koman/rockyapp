package com.rockyapp.rockyappbackend.exceptions;

public class NotFoundException extends Exception{

    private static final long serialVersionUID = 3603915218320209378L;

    public NotFoundException(){}
    public NotFoundException(String message) {
        super(message);
    }
}
