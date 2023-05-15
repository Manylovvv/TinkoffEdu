package ru.tinkoff.edu.java.java.bot.service;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import ru.tinkoff.edu.java.java.bot.dto.request.LinkUpdate;
import ru.tinkoff.edu.java.java.bot.model.core.BotCreator;

@RabbitListener(queues = "${app.queue-name}")
public class QueueLinkUpdateReceiver extends AbstractLinkUpdateReceiver {
    public QueueLinkUpdateReceiver(BotCreator bot) {
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
