package com.project.ens.handler;

import com.project.ens.exception.EmailException;
import com.project.ens.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus
    public ErrorResponse handleEmailException(final EmailException e) {

    }
}
