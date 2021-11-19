package com.cloud.kafkastream.config;

import com.cloud.kafkastream.web.v1.model.UserClick;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.streams.processor.TimestampExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Slf4j
@Configuration
@Profile("user-click-topic")
public class UserClickTimeExtractor implements TimestampExtractor {


    @Override
    public long extract(ConsumerRecord<Object, Object> consumerRecord, long prevTime) {

        UserClick userClick= (UserClick) consumerRecord.value();
        log.info("Click time: {}", userClick.getCreatedTime());
        return ((userClick.getCreatedTime()>0) ? userClick.getCreatedTime() : prevTime);
    }

    @Bean
    public TimestampExtractor userClickTimeExtractor() {
        return new UserClickTimeExtractor();
    }
}
