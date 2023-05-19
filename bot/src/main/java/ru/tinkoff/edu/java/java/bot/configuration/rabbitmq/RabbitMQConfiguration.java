package ru.tinkoff.edu.java.java.bot.configuration.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Аннотация указывает, что класс содержит методы определения @Bean
 * Класс также имеет аннотацию @ConditionalOnProperty,
 * указывающую, что класс следует загружать только в том случае, если установлено определенное свойство
 * В этом случае свойство очереди использования в пространстве имен приложения,
 * и его значение должно быть равно true для загрузки этого класса.
 */
@Configuration
@ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "true")
public class RabbitMQConfiguration {
    @Value("${app.queue-name}")
    private String queueName;
    @Value("${app.exchange-name}")
    private String exchangeName;

    /**
     * создает новый объект DirectExchange с именем, указанным в поле exchangeName, и возвращает его.
     * - queue(): создает новый объект Queue с именем, указанным в поле queueName, и устанавливает
     * аргумент для указания обмена недоставленными сообщениями.
     * @return - объект Queue.
     */
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(exchangeName, false, false);
    }

    /**
     * создает новый объект Queue с именем, указанным в поле queueName,
     * и устанавливает аргумент для указания обмена недоставленными сообщениями
     */
    @Bean
    public Queue queue() {
        return QueueBuilder.nonDurable(queueName).withArgument("x-dead-letter-exchange", exchangeName + ".dlx").build();
    }

    /**
     * создает новый объект Binding, который связывает функцию queue() с объектом directExchange() с именем queueName
     * @return - объект Binding.
     */
    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(directExchange()).with(queueName);
    }

    /**
     * создает новый объект FanoutExchange с именем, основанным на поле exchangeName, и возвращает его.
     */
    @Bean
    public FanoutExchange deadDirectExchange() {
        return new FanoutExchange(exchangeName + ".dlx", false, false);
    }

    /**
     * создает новый объект Queue с именем, основанным на поле queueName, и возвращает его.
     */
    @Bean
    public Queue deadQueue() {
        return QueueBuilder.nonDurable(queueName + ".dlq").build();
    }

    /**
     * создает новый объект Binding, который связывает deadQueue() с объектом deadDirectExchange()
     * @return - Bindng
     */
    @Bean
    public Binding deadBinding() {
        return BindingBuilder.bind(deadQueue()).to(deadDirectExchange());
    }

    /**
     * создает новый объект Jackson2JsonMessageConverter, который представляет собой преобразователь
     * сообщений, который может преобразовывать объекты Java в формат JSON.
     * @return - MessageConverter
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
