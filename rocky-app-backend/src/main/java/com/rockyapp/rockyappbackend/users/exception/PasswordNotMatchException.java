package com.rockyapp.rockyappbackend.users.exception;

public class PasswordNotMatchException extends Exception{

    private static final long serialVersionUID = -1358230003277955875L;
    private static final String ERROR_MESSAGE = "Les mots de passe ne correspondent pas.";

    public PasswordNotMatchException(){
        super(ERROR_MESSAGE);
    }
}
