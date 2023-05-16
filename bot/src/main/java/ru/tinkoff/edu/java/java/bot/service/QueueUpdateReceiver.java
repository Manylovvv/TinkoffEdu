package ru.tinkoff.edu.java.java.bot.service;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import ru.tinkoff.edu.java.java.bot.telegram.impl.creator.BotCreator;
import ru.tinkoff.edu.java.java.bot.controller.dto.request.LinkUpdate;

@RabbitListener(queues = "${app.queue-name}")
public class QueueUpdateReceiver extends AbstractUpdateReceiver {
    public QueueUpdateReceiver(BotCreator bot) {
        super(bot);
    }

    @RabbitHandler
    @Override
    public void receiveUpdate(LinkUpdate request) {
        this.sendUpdates(request);
    }

    @RabbitListener(queues = "${app.queue-name}.dlq")
    public void processFailedMessagesRequeue(Message failedMessage) {
        System.err.println("Error while receiving update: " + failedMessage);
    }
}
