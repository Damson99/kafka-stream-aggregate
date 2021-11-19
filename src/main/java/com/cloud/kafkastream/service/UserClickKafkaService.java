package com.cloud.kafkastream.service;


import com.cloud.kafkastream.binder.UserClickListenerBinder;
import com.cloud.kafkastream.web.v1.model.UserClick;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.SessionWindows;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;

@Slf4j
@Service
@Profile("user-click-topic")
@EnableBinding(UserClickListenerBinder.class)
public class UserClickKafkaService {

//    =====SESSION=====
//    is period of activity followed by a defined gap of inactivity
//    for example session starts with user click on website and ending is user is idle for five minutes


    @StreamListener("user-click-input-channel")
    public void process(KStream<String, UserClick> inputStream) {

        inputStream.peek((key, value) -> log.info("key: "+key+" created time: "
                +Instant.ofEpochMilli(value.getCreatedTime()).atOffset(ZoneOffset.UTC)))
                .groupByKey()
//                as long as the user clicks on the page, the session continues
//                f the user clicks something within 5 minutes the session will continue otherwise it will end
                .windowedBy(SessionWindows.with(Duration.ofMinutes(5)))
                .count()
                .toStream()
                .foreach((key, value) -> log.info(
                        " userID: "            +key.key()+
                        " window time start: " +Instant.ofEpochMilli(key.window().start()).atOffset(ZoneOffset.UTC)+
                        " window time end: "   +Instant.ofEpochMilli(key.window().end()).atOffset(ZoneOffset.UTC)+
                        " count: "             +value+
                        " window#: "           +key.window().hashCode()
                ));
    }
}
