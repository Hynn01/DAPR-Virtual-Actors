package com.example.wdm.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="no enough credit or no enough stock")  // 404
public class OrderException extends RuntimeException {
    // ...
 }