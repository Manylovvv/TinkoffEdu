package ru.tinkoff.edu.java.scrapper.configuration.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**Аннотация, которая определяет класс конфигурационным и содержит бины*/
@Configuration
/**
 * Аннотация, которая указывает, что данный класс будет создан только в том случае,
 * если в файле application.properties установлено свойство app.use-queue равное true.
 */
@ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "true")
public class RabbitMQConfiguration {
    /**Аннотация, которая указывает, что значение свойства app.queue-name будет внедрено в переменную queueName*/
    @Value("${app.queue-name}")
    private String queueName;
    /**Аннотация, которая указывает, что значение свойства app.exchange-name будет внедрено в переменную exchangeName*/
    @Value("${app.exchange-name}")
    private String exchangeName;

    /**
     * Аннотация для методов в конфигурационном файле Спринга.
     * Она указывает на то, что метод должен быть оберткой над объектом-бином Спринга
     * DirectExchange - это тип обмена сообщениями в RabbitMQ,
     * который маршрутизирует сообщения по ключу маршрутизации
     */
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(exchangeName, false, false);
    }

    /**
     * Queue - это очередь сообщений в RabbitMQ, которая хранит сообщения до тех пор, пока они не будут обработаны
     * Метод Указывает, что очередь не является долговечной, т.е. она будет удалена при остановке брокера сообщений.
     * Метод withArgument() добавляет аргументы для очереди, в данном случае x-dead-letter-exchange,
     * который указывает на обмен сообщениями,в который будут направлены сообщения, которые не удалось обработать
     */
    @Bean
    public Queue queue() {
        return QueueBuilder.nonDurable(queueName).withArgument("x-dead-letter-exchange", exchangeName + ".dlq").build();
    }

    /**
     * Binding - это связь между обменом сообщениями и очередью, которая
     * определяет, какие сообщения будут направлены в очередь
     */
    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(directExchange()).with(queueName);
    }

    /**
     * MessageConverter - это интерфейс, который определяет преобразование
     * сообщений из объектов Java в сообщения RabbitMQ и наоборот
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}

