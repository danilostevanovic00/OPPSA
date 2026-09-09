package com.wms.orders.config;

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
 * RabbitMQ exchange, queue, binding and message converter configuration for the Orders service.
 */
@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "wms.topic.exchange";

    public static final String RK_RESERVE_STOCK = "stock.reserve";
    public static final String RK_CANCEL_RESERVATION = "reservation.cancel";
    public static final String RK_GOODS_SOLD = "goods.sold";

    public static final String RK_SHIPMENT_PAYMENT_READY = "shipment.payment-ready";
    public static final String RK_SHIPMENT_PAYMENT_READY_CANCELLED = "shipment.payment-ready-cancelled";

    public static final String RK_ITEM_CREATED = "item.created";
    public static final String RK_STOCK_REPLENISHED = "stock.replenished";
    public static final String RK_STOCK_RESERVED = "stock.reserved";
    public static final String RK_STOCK_RESERVATION_FAILED = "stock.reservation.failed";

    public static final String RK_PAYMENT_CONFIRMED = "payment.confirmed";

    public static final String Q_ITEM_CREATED = "orders.item-created.queue";
    public static final String Q_STOCK_REPLENISHED = "orders.stock-replenished.queue";
    public static final String Q_STOCK_RESERVED = "orders.stock-reserved.queue";
    public static final String Q_STOCK_RESERVATION_FAILED = "orders.stock-reservation-failed.queue";
    public static final String Q_PAYMENT_CONFIRMED = "orders.payment-confirmed.queue";

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE, true, false);
    }

    @Bean public Queue itemCreatedQueue()              { return new Queue(Q_ITEM_CREATED, true); }
    @Bean public Queue stockReplenishedQueue()         { return new Queue(Q_STOCK_REPLENISHED, true); }
    @Bean public Queue stockReservedQueue()            { return new Queue(Q_STOCK_RESERVED, true); }
    @Bean public Queue stockReservationFailedQueue()   { return new Queue(Q_STOCK_RESERVATION_FAILED, true); }
    @Bean public Queue paymentConfirmedQueue()         { return new Queue(Q_PAYMENT_CONFIRMED, true); }

    @Bean
    public Binding bindItemCreated(Queue itemCreatedQueue, TopicExchange exchange) {
        return BindingBuilder.bind(itemCreatedQueue).to(exchange).with(RK_ITEM_CREATED);
    }

    @Bean
    public Binding bindStockReplenished(Queue stockReplenishedQueue, TopicExchange exchange) {
        return BindingBuilder.bind(stockReplenishedQueue).to(exchange).with(RK_STOCK_REPLENISHED);
    }

    @Bean
    public Binding bindStockReserved(Queue stockReservedQueue, TopicExchange exchange) {
        return BindingBuilder.bind(stockReservedQueue).to(exchange).with(RK_STOCK_RESERVED);
    }

    @Bean
    public Binding bindStockReservationFailed(Queue stockReservationFailedQueue, TopicExchange exchange) {
        return BindingBuilder.bind(stockReservationFailedQueue).to(exchange).with(RK_STOCK_RESERVATION_FAILED);
    }

    @Bean
    public Binding bindPaymentConfirmed(Queue paymentConfirmedQueue, TopicExchange exchange) {
        return BindingBuilder.bind(paymentConfirmedQueue).to(exchange).with(RK_PAYMENT_CONFIRMED);
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
