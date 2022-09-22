package com.rockyapp.rockyappbackend.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class ValidationException extends SocleMetierException{

    private static final long serialVersionUID = 7829717343832983630L;

    private static final String ERROR_CODE = "VALIDATION_ERROR";


    public ValidationException(String message) {
        super(ERROR_CODE, message);
    }

    public ValidationException(String code, String message) {
        super(code, message);
    }
}
