package com.cloud.kafkastream.binder;

import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.context.annotation.Profile;

@Profile("streaming-words-topic")
public interface WordListenerBinder {

    @Input("words-input-channel")
    KStream<String, String> wordsInputStream();
}
