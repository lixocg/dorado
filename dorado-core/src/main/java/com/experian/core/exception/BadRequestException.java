package com.experian.core.exception;


import org.springframework.http.HttpStatus;

@SuppressWarnings("serial")
public class BadRequestException extends AbstractHttpException {


  public BadRequestException(String str) {
    super(str);
    setHttpStatus(HttpStatus.BAD_REQUEST);
  }
}
