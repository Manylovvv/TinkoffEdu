package ru.tinkoff.edu.java.bot.handler;

import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.tinkoff.edu.java.bot.schemas.response.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

@RestControllerAdvice
public class ControllerExceptionHandler {

    private ApiErrorResponse HandleOutput(String message, Exception exception, HttpStatus httpStatus) {
        return new ApiErrorResponse(
                message,
                String.valueOf(httpStatus.value()),
                exception.getClass().getName(),
                exception.getMessage(),
                Arrays.stream(exception.getStackTrace()).map(String::valueOf).toList()
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse IllegalArgs(IllegalArgumentException Exception) {
        return HandleOutput("There are incorrect parameters in your request!", Exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse TypeMismatch(MethodArgumentTypeMismatchException Exception) {
        return HandleOutput("Type mismatcht!", Exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse IncorrectRequest(HttpRequestMethodNotSupportedException Exception) {
        return  HandleOutput("METOD NOT ALLOWED!", Exception, HttpStatus.BAD_REQUEST);
        //return HandleOutput("METOD NOT ALLOWED!", Exception, HttpStatus.BAD_REQUEST);
    }
}
