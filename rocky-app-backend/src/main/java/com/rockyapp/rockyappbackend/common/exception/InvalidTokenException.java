package com.rockyapp.rockyappbackend.common.exception;

import com.rockyapp.rockyappbackend.utils.helpers.StringHelper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class InvalidTokenException extends Exception {

    private static final long serialVersionUID = -1256646265380008470L;

    private static final String ERROR_MESSAGE = "Invalid % token exception.";

    public InvalidTokenException(String type){
        super(StringHelper.replace(ERROR_MESSAGE, type));
    }
}
