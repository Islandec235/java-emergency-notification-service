package com.project.ens.handler;

import com.project.ens.exception.ConflictException;
import com.project.ens.exception.NotFoundException;
import com.project.ens.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(final NotFoundException e) {
        log.error("Error 404 {}", e.getMessage());
        return createResponse(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflictException(final ConflictException e) {
        log.error("Error 409 {}", e.getMessage());
        return createResponse(e, HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleServerError(final Throwable e) {
        log.error("Server error 500 {}", e.getMessage());
        return createResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ErrorResponse createResponse(Throwable e, HttpStatus status) {
        String classInit = Arrays.stream(e.getStackTrace())
                .findAny()
                .toString();
        String localMessage = e.getLocalizedMessage() + " \n Class: " + classInit;

        e.printStackTrace();
        StringWriter stringWriter = new StringWriter();
        PrintWriter printerWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printerWriter);

        return new ErrorResponse(status, localMessage, e.getMessage(), stringWriter.toString());
    }
}
