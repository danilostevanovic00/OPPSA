package com.wms.inventory.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ exchange, queue, binding and message converter configuration for the Inventory service.
 */
@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "wms.topic.exchange";

    public static final String RK_ITEM_CREATED = "item.created";
    public static final String RK_STOCK_REPLENISHED = "stock.replenished";
    public static final String RK_STOCK_RESERVED = "stock.reserved";
    public static final String RK_STOCK_RESERVATION_FAILED = "stock.reservation.failed";

    public static final String RK_RESERVE_STOCK = "stock.reserve";
    public static final String RK_CANCEL_RESERVATION = "reservation.cancel";
    public static final String RK_GOODS_SOLD = "goods.sold";

    public static final String Q_RESERVE_STOCK = "inventory.reserve-stock.queue";
    public static final String Q_CANCEL_RESERVATION = "inventory.cancel-reservation.queue";
    public static final String Q_GOODS_SOLD = "inventory.goods-sold.queue";

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE, true, false);
    }

    @Bean
    public Queue reserveStockQueue() {
        return new Queue(Q_RESERVE_STOCK, true);
    }

    @Bean
    public Queue cancelReservationQueue() {
        return new Queue(Q_CANCEL_RESERVATION, true);
    }

    @Bean
    public Queue goodsSoldQueue() {
        return new Queue(Q_GOODS_SOLD, true);
    }

    @Bean
    public Binding bindReserveStock(Queue reserveStockQueue, TopicExchange exchange) {
        return BindingBuilder.bind(reserveStockQueue).to(exchange).with(RK_RESERVE_STOCK);
    }

    @Bean
    public Binding bindCancelReservation(Queue cancelReservationQueue, TopicExchange exchange) {
        return BindingBuilder.bind(cancelReservationQueue).to(exchange).with(RK_CANCEL_RESERVATION);
    }

    @Bean
    public Binding bindGoodsSold(Queue goodsSoldQueue, TopicExchange exchange) {
        return BindingBuilder.bind(goodsSoldQueue).to(exchange).with(RK_GOODS_SOLD);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter(ObjectMapper objectMapper) {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter(objectMapper);
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        typeMapper.setTypePrecedence(DefaultJackson2JavaTypeMapper.TypePrecedence.INFERRED);
        converter.setJavaTypeMapper(typeMapper);
        return converter;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                          Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            Jackson2JsonMessageConverter messageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);
        return factory;
    }
}
