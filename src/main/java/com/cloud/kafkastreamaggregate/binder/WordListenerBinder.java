package com.cloud.kafkastreamaggregate.binder;

import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;

public interface WordListenerBinder {

    @Input("words-input-channel")
    KStream<String, String> wordsInputStream();
}
