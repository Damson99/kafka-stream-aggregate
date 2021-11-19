package com.cloud.kafkastream.service;


import com.cloud.kafkastream.binder.SimpleInvoiceListenerBinder;
import com.cloud.kafkastream.web.v1.model.SimpleInvoice;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;

@Slf4j
@Profile("simple-invoice-topic")
@Service
@EnableBinding(SimpleInvoiceListenerBinder.class)
public class SimpleInvoiceKafkaService {
//    =====WINDOWING KINDS=====

//    HOPPING/SLIDING WINDOWS
//    Fixed in size
//    overlapping
//    no gaps
//    the advance interval specifies how much a window moves forward relative to the previous one -
//    - windows can overlapping


//    TUMBLING WINDOWS
//    fixed in size
//    no overlap
//    no gaps
    @Profile("")
    @StreamListener("simple-invoice-topic")
    public void process(KStream<String, SimpleInvoice> inputStream) {

        inputStream.peek((key, value) -> log.info("Key: {}, created time: {}",
                key, Instant.ofEpochMilli(value.getCreatedTime()).atOffset(ZoneOffset.UTC)))
//                group by store id key
                .groupByKey()
//                TUMBLING WINDOW
                .windowedBy(TimeWindows.of(Duration.ofMinutes(5))
//                        HOPPING/SLIDING WINDOWS
//                        .advanceBy(Duration.ofMinutes(1))
                )
                .count()
                .toStream()
                .foreach((key, value) -> log.info(
                        "StoreID: "+key.key()+
                        " Window start: "+
                        Instant.ofEpochMilli(key.window().start())
                            .atOffset(ZoneOffset.UTC)+
                        " Window end: "+
                        Instant.ofEpochMilli(key.window().end())
                            .atOffset(ZoneOffset.UTC)+
                        " Count: "+value+
                        " Window#: "+key.window().hashCode()
                ));
    }
}
