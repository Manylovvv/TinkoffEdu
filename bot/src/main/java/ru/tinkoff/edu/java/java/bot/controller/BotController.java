package ru.tinkoff.edu.java.java.bot.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.edu.java.java.bot.controller.dto.request.LinkUpdate;
import ru.tinkoff.edu.java.java.bot.service.AbstractUpdateReceiver;

/**
 * Первая аннотация - генерирует параметризованный конструктор, который
 * принимает один параметр для каждого поля и инициализирует их с его помощью
 * Вторая - указывает на контроллер, каждый метод которого наследует аннотацию
 */
@AllArgsConstructor
@RestController
public class BotController {
    private AbstractUpdateReceiver linkUpdateReceiver;

    /**
     * POST запрос, который получает объект LinkUpdate в теле запроса.
     * Он вызывает метод ReceiveUpdate объекта linkUpdateReceiver для обработки обновления.
     * @param request
     */
    @PostMapping("/updates")
    public void update(@RequestBody LinkUpdate request) {
        linkUpdateReceiver.receiveUpdate(request);
    }
}
