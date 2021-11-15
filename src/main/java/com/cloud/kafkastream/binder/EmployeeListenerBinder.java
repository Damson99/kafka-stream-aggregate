package com.cloud.kafkastream.binder;

import com.cloud.kafkastream.web.v1.model.Employee;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.context.annotation.Profile;

@Profile("employees-topic")
public interface EmployeeListenerBinder {

    @Input("employee-input-topic")
    KStream<String, Employee> employeeInputStream();
}
