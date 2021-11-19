package com.cloud.kafkastream.config;

import com.cloud.kafkastream.web.v1.model.SimpleInvoice;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.streams.processor.TimestampExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SimpleInvoiceTimeExtractor implements TimestampExtractor {
    @Override
    public long extract(ConsumerRecord<Object, Object> consumerRecord, long prevTime) {
//        reading timestamp from the record. Also can read timestamp from system time
//        or record metadata
        SimpleInvoice simpleInvoice = (SimpleInvoice) consumerRecord.value();
        return ((simpleInvoice.getCreatedTime()>0) ? simpleInvoice.getCreatedTime() : prevTime);
    }

    @Bean TimestampExtractor invoiceTimesExtractor(){
        return new SimpleInvoiceTimeExtractor();
    }
}
