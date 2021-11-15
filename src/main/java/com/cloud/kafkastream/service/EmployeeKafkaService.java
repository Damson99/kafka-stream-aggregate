package com.cloud.kafkastream.service;


import com.cloud.kafkastream.binder.EmployeeListenerBinder;
import com.cloud.kafkastream.web.v1.model.Employee;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Profile("employees-topic")
@EnableBinding(EmployeeListenerBinder.class)
public class EmployeeKafkaService {

    @Autowired
    private RecordBuilderService recordBuilderService;

    /*
    1. map()
    2. groupBy()
    3. reduce()
    OR
    1. groupBy()
    2. aggregate()
     */

    @StreamListener("employee-input-channel")
    public void process(KStream<String, Employee> inputStream){

        inputStream.peek((key, value) ->
//                log data
                log.info("Employee- Key: {}, Value: {}", key, value))
//                group key by department ID
                .groupBy((key, value) -> value.getDepartment())
//                aggregate data
                .aggregate(
//                        initial data to aggregate - blank record
                        () -> recordBuilderService.initBlankRecord(),
//                      key: department ID, value: employee, aggregatedValue: departmentAggregate
                        (key, value, aggregatedValue) -> recordBuilderService.aggregate(value, aggregatedValue)
                )
                .toStream().foreach((key, value) -> log.info("DepartmentAggregate: Key: {}, Value: {}", key, value));
    }
}
