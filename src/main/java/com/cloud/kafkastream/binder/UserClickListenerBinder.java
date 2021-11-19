package com.cloud.kafkastream.binder;

import com.cloud.kafkastream.web.v1.model.UserClick;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.context.annotation.Profile;

@Profile("user-click-topic")
public interface UserClickListenerBinder {

    @Input("user-click-input-channel")
    KStream<String, UserClick> userClickInputStream();
}
