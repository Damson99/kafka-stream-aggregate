package com.cloud.kafkastream.service;


import com.cloud.kafkastream.binder.WordListenerBinder;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Slf4j
@Service
@Profile("streaming-words-topic")
@EnableBinding(WordListenerBinder.class)
public class WordKafkaService
{

    @StreamListener("words-input-channel")
    public void process(KStream<String, String> inputStream){

        KStream<String, String> wordsStream = inputStream
                .flatMapValues(value -> Arrays.asList(value.toLowerCase().split(" ")));

/*
      * grouping is always performed by the key
        of you already comes with the desired key, you can use groupByKey()
      * kafka has
      - Key Preserving API - preserve Key in records and avoid the shuffle/sort in kafka cluster
        which prevents partitioning in kafka cluster
        and
      - Key Changing API - change the key from value in KStream/KTable for example groupBy method which
        perform the shuffle/sort in kafka cluster and making the partitioning - this is bad for performance,
        but sometimes it's required
 */
        wordsStream
                .groupBy((key, value) -> value)
                .count()
                .toStream()
                .peek((key, value) -> log.info("Word: {} appeared {} times", key, value));
    }

}
