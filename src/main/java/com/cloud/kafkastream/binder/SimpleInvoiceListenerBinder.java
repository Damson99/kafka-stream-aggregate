package com.cloud.kafkastream.binder;

import com.cloud.kafkastream.web.v1.model.SimpleInvoice;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;

public interface SimpleInvoiceListenerBinder {

    @Input("simple-invoice-input-channel")
    KStream<String, SimpleInvoice> simpleInvoiceInputChannel();
}
