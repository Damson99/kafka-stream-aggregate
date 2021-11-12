package com.cloud.kafkastreamaggregate.service;


import com.cloud.kafkastreamaggregate.binder.WordListenerBinder;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Slf4j
@Service
@EnableBinding(WordListenerBinder.class)
public class WordListenerService
{

    @StreamListener("words-input-channel")
    public void process(KStream<String, String> inputStream){

        KStream<String, String> wordsStream = inputStream
                .flatMapValues(value -> Arrays.asList(value.toLowerCase().split(" ")));

//        grouping is always performed by the key
//        of you already comes with the desired key, you can use groupByKey()
        wordsStream
                .groupBy((key, value) -> value)
                .count()
                .toStream()
                .peek((key, value) -> log.info("Word: {} appeared {} times", key, value));
    }

}
