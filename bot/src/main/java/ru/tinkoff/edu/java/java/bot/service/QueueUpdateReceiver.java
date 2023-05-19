package ru.tinkoff.edu.java.java.bot.service;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import ru.tinkoff.edu.java.java.bot.controller.dto.request.LinkUpdate;
import ru.tinkoff.edu.java.java.bot.telegram.impl.creator.BotCreator;

/**
 * прослушивает сообщения из очереди RabbitMQ, указанной в свойстве app.queue-name.
 * Когда сообщение получено, он вызывает метод receiveUpdate() для обработки сообщения
 * и отправки обновлений боту Telegram с помощью метода sendUpdates(), унаследованного
 * от класса AbstractUpdateReceiver.
 */
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

    /**
     * @RabbitListener, который прослушивает сообщения из очереди недоставленных сообщений
     * (${app.queue-name}.dlq) и повторно ставит в очередь ошибочные сообщения.
     * @param failedMessage
     */
    @RabbitListener(queues = "${app.queue-name}.dlq")
    public void processFailedMessagesRequeue(Message failedMessage) {
        System.err.println("Error while receiving update: " + failedMessage);
    }
}
