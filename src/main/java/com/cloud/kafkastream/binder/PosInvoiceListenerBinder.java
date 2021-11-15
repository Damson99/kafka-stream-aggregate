package com.cloud.kafkastream.binder;

import com.cloud.kafkastream.web.v1.model.Notification;
import com.cloud.kafkastream.web.v1.model.PosInvoice;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.context.annotation.Profile;

@Profile("loyalty-topic")
public interface PosInvoiceListenerBinder
{

    @Input("pos-invoice-input-channel")
    KStream<String, PosInvoice> posInvoiceInputStream();

    @Output("notification-output-channel")
    KStream<String, Notification> notificationOutputStream();
}
