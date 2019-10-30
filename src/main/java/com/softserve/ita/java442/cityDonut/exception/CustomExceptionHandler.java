package com.softserve.ita.java442.cityDonut.exception;

import lombok.AllArgsConstructor;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
@AllArgsConstructor
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    private ErrorAttributes errorAttributes;

    @ExceptionHandler(RuntimeException.class)
    public final ResponseEntity handleRuntimeException(RuntimeException ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(getErrorAttributes(request));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
    }

    @ExceptionHandler(IncorrectPasswordException.class)
    public final ResponseEntity incorrectPasswordException(WebRequest request){
        ExceptionResponse exceptionResponse = new ExceptionResponse(getErrorAttributes(request));
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exceptionResponse);
    }

    @ExceptionHandler(NotFoundException.class)
    public final ResponseEntity notFoundException(WebRequest request){
        ExceptionResponse exceptionResponse = new ExceptionResponse(getErrorAttributes(request));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionResponse);
    }

    @ExceptionHandler(ProjectNotFoundException.class)
    public final ResponseEntity projectNotFoundException(WebRequest request){
        ExceptionResponse exceptionResponse = new ExceptionResponse(getErrorAttributes(request));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionResponse);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public final ResponseEntity categoryNotFoundException(WebRequest request){
        ExceptionResponse exceptionResponse = new ExceptionResponse(getErrorAttributes(request));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionResponse);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        List<FieldValidationErrorContainer> errorList = new ArrayList<>();
        ex.getBindingResult()
                .getFieldErrors()
                .forEach(fieldError -> errorList.add(new FieldValidationErrorContainer(fieldError)));
        return new ResponseEntity<>(
                errorList,
                HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(BadEmailException.class)
    public final ResponseEntity badEmailException(WebRequest request){
        ExceptionResponse exceptionResponse = new ExceptionResponse(getErrorAttributes(request));
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exceptionResponse);
    }

    @ExceptionHandler(InvalidEmailException.class)
    public final ResponseEntity invalidEmailException(WebRequest request){
        ExceptionResponse exceptionResponse = new ExceptionResponse(getErrorAttributes(request));
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exceptionResponse);
    }


    private Map<String, Object> getErrorAttributes(WebRequest webRequest) {
        return new HashMap<>(errorAttributes.getErrorAttributes(webRequest, true));
    }

}
