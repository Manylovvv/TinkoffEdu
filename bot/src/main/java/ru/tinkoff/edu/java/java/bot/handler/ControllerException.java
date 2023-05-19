package ru.tinkoff.edu.java.java.bot.handler;

import java.util.Arrays;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.tinkoff.edu.java.java.bot.controller.dto.response.ApiErrorResponse;

/**
 * обрабатывает исключения, генерируемые другими контроллерами в приложении
 */
@RestControllerAdvice
public class ControllerException {

    /**
     * обрабатывает два типа исключений: HttpMessageNotReadableException
     * и MethodArgumentNotValidException.
     * @return - Он возвращает объект ApiErrorResponse
     * с сообщением об ошибке, кодом ошибки, именем исключения, сообщением об исключении и трассировкой стека.
     * - "Неправильные параметры запроса": строковое сообщение о том, что параметры запроса неверны.
     * - "400": строковый код ошибки, указывающий на то, что запрос был неверным.
     * - ex.getClass().getName(): строка, представляющая имя класса исключения, которое было
     * перехвачено при возникновении ошибки.
     * - ex.getMessage(): строка, представляющая сообщение, связанное с перехваченным исключением.
     * - Arrays.stream(ex.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()):
     * список строк, представляющих трассировку стека перехваченного исключения.
     *
     * Метод Arrays.stream(ex.getStackTrace()) возвращает массив объектов StackTraceElement, представляющих
     * трассировку стека исключения. Затем метод map(StackTraceElement::toString) преобразует каждый объект
     * StackTraceElement в строковое представление. Наконец, метод collect(Collectors.toList()) собирает
     * строковые представления в список строк.
     */
    @ExceptionHandler(value = {
        HttpMessageNotReadableException.class,
        MethodArgumentNotValidException.class
    })
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ApiErrorResponse resourceNotFoundException(Exception ex) {
        return new ApiErrorResponse(
            "Неправильные параметры запроса",
            "400",
            ex.getClass().getName(),
            ex.getMessage(),
            Arrays.stream(ex.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()));
    }
}
