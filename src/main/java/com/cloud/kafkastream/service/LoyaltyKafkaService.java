package com.cloud.kafkastream.service;

import com.cloud.kafkastream.binder.PosInvoiceListenerBinder;
import com.cloud.kafkastream.web.v1.model.Notification;
import com.cloud.kafkastream.web.v1.model.PosInvoice;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Profile("loyalty-topic")
@EnableBinding(PosInvoiceListenerBinder.class)
public class LoyaltyKafkaService {

/*
 want to calculate the sum of loyalty points by the customer id so
 I must to change a key value - previously was the store id, after the groupBy
 and aggregate key value will be customer id
 */

    @Autowired
    RecordBuilderService recordBuilderService;

    private final static String PRIME="PRIME";

    @StreamListener("pos-invoice-input-channel")
    @SendTo("notification-output-channel")
    KStream<String, Notification> process(KStream<String, PosInvoice> inputStream) {

        KStream<String, Notification> notificationKStream = inputStream
                .filter((key, value) -> value.getCustomerType().equalsIgnoreCase(PRIME))
//                this is where the repartition comes in - new value for key
                .map((key, value) -> new KeyValue<>(value.getCustomerCardNo(), recordBuilderService.getPosInvoiceNotification(value)))
                .groupByKey()
                .reduce((aggregateValue, newValue) ->
                {
                    newValue.setTotalLoyaltyPoints(newValue.getEarnedLoyaltyPoints()+aggregateValue.getTotalLoyaltyPoints());
                    return newValue;
                }).toStream();

        notificationKStream.foreach((key, value) -> log.info(String.format("Notification Key: %s, Value: %s", key, value)));
        return notificationKStream;
    }
}
