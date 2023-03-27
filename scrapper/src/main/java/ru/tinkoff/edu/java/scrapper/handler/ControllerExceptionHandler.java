package ru.tinkoff.edu.java.scrapper.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.tinkoff.edu.java.exception.IncorrectParametersException;
import ru.tinkoff.edu.java.response.ErrorResponse;
import ru.tinkoff.edu.java.exception.ResourceNotFoundException;

import java.util.Arrays;

@RestControllerAdvice
public class ControllerExceptionHandler {

    private ErrorResponse HandleOutput(String message, Exception exception) {
        return new ErrorResponse(
                message,
                String.valueOf(HttpStatus.NOT_FOUND.value()),
                exception.getClass().getName(),
                exception.getMessage(),
                Arrays.stream(exception.getStackTrace()).map(String::valueOf).toList()
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse ResourceNotFoundException(ResourceNotFoundException Exception) {
        return HandleOutput("Requested resource not found", Exception);
    }

    @ExceptionHandler({IncorrectParametersException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse IncorrectRequestParamsException(IncorrectParametersException Exception) {
        return HandleOutput("This parameters is not correct", Exception);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse IncorrectParamsException(MethodArgumentTypeMismatchException Exception) {
        return HandleOutput("This parameters is not correct", Exception);
    }

    @ExceptionHandler({MissingServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse MissingServletRequestParameterException(MissingServletRequestParameterException Exception) {
        return HandleOutput("There are missing servlet query parameters!", Exception);
    }


}
