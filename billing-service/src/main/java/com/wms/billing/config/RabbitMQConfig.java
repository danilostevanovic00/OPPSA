package com.wms.billing.config;

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
 * RabbitMQ exchange, queue, binding and message converter configuration for the Billing service.
 */
@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "wms.topic.exchange";

    public static final String RK_PAYMENT_CONFIRMED = "payment.confirmed";

    public static final String RK_SHIPMENT_PAYMENT_READY = "shipment.payment-ready";
    public static final String RK_SHIPMENT_PAYMENT_READY_CANCELLED = "shipment.payment-ready-cancelled";

    public static final String Q_SHIPMENT_PAYMENT_READY = "billing.shipment-payment-ready.queue";
    public static final String Q_SHIPMENT_PAYMENT_READY_CANCELLED = "billing.shipment-payment-ready-cancelled.queue";

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE, true, false);
    }

    @Bean public Queue shipmentPaymentReadyQueue()          { return new Queue(Q_SHIPMENT_PAYMENT_READY, true); }
    @Bean public Queue shipmentPaymentReadyCancelledQueue() { return new Queue(Q_SHIPMENT_PAYMENT_READY_CANCELLED, true); }

    @Bean
    public Binding bindShipmentPaymentReady(Queue shipmentPaymentReadyQueue, TopicExchange exchange) {
        return BindingBuilder.bind(shipmentPaymentReadyQueue).to(exchange).with(RK_SHIPMENT_PAYMENT_READY);
    }

    @Bean
    public Binding bindShipmentPaymentReadyCancelled(Queue shipmentPaymentReadyCancelledQueue, TopicExchange exchange) {
        return BindingBuilder.bind(shipmentPaymentReadyCancelledQueue).to(exchange).with(RK_SHIPMENT_PAYMENT_READY_CANCELLED);
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

