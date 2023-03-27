package ru.tinkoff.edu.java.bot.handler;

import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.tinkoff.edu.java.exception.IncorrectParametersException;
import ru.tinkoff.edu.java.response.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

@RestControllerAdvice
public class ControllerExceptionHandler {

    private ApiErrorResponse HandleOutput(String message, Exception exception) {
        return new ApiErrorResponse(
                message,
                String.valueOf(HttpStatus.NOT_FOUND.value()),
                exception.getClass().getName(),
                exception.getMessage(),
                Arrays.stream(exception.getStackTrace()).map(String::valueOf).toList());
    }

    @ExceptionHandler(IncorrectParametersException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleIncorrectRequest(IncorrectParametersException Exception) {
        return HandleOutput("There are incorrect parameters in your request!", Exception);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleIncorrectRequest(MethodArgumentTypeMismatchException Exception) {
        return HandleOutput("Type mismatcht!", Exception);
    }

}
