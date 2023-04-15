package com.posomo.saltit.exception;

import com.posomo.saltit.exception.exceptions.ExampleException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.charset.Charset;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private HttpHeaders getJsonMediaTypeHttpHeader(){
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        return header;
    }

    @ExceptionHandler(ExampleException.class)
    public ResponseEntity<ErrorResponse> handleExampleException(ExampleException e){
        HttpHeaders header = getJsonMediaTypeHttpHeader();
        ErrorResponse errorResponse = ErrorCode.enumToEntity(ErrorCode.METHOD_NOT_ALLOWED);
        return new ResponseEntity<>(errorResponse ,header, HttpStatus.OK);
    }
}
