package com.neostudy.creditbank.calculator.controller;

import com.neostudy.creditbank.calculator.exception.LaterBirthdateException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AdviceController
{
    @ExceptionHandler(LaterBirthdateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public LaterBirthdateException onLaterBirthdateException(LaterBirthdateException e) {
        e.setMessage("Клиент не может быть младше 18 лет.");
        return e;
    }
}
