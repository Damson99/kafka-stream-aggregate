package com.cloud.kafkastream.service;


import com.cloud.kafkastream.binder.EmployeeListenerBinder;
import com.cloud.kafkastream.web.v1.model.Employee;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KeyValue;
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

//    my input Stream is coming with null key so I have to accept stream as a KStream
//    and convert it to a KTable - KTable must have a key with value employeeId,
//    because it doesn't change
    @StreamListener("employee-input-channel")
    public void process(KStream<String, Employee> inputStream) {

        inputStream.map((key, value) -> KeyValue.pair(value.getId(), value))
                .peek((key, value) -> log.info("Employee: Key: {}, Value: {}", key, value))
                .toTable()
//                want to calculate data by department name: key is department name
                .groupBy((key, value) -> KeyValue.pair(value.getDepartment(), value))
                .aggregate(
//                        init blank record
                        () -> recordBuilderService.initBlankRecord(),
//                        when employee with new id is coming the new record is added
                        (key, value, aggValue) -> recordBuilderService.adder(value, aggValue),
//                        when employee with the same id is coming old record is subtracted here
//                        and new record is added above
                        (key, value, aggValue) -> recordBuilderService.subtractor(value, aggValue)
//                convert to KStream as a output
                ).toStream()
            .foreach((key, value) -> log.info("DepartmentAggregate: Key: {}, Value: {}", key, value));
    }


//    @StreamListener("employee-input-channel")
    public void incorrectProcess(KStream<String, Employee> inputStream) {

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
                        (key, value, aggregatedValue) -> recordBuilderService.adder(value, aggregatedValue)
                )
                .toStream().foreach((key, value) -> log.info("DepartmentAggregate: Key: {}, Value: {}", key, value));
/*
        * if we swap employee's department we will get incorrect result from current KStream
        * you can use KStream aggregation to produce correct results when your use case represents
        an actual stream. If your case is an update stream, you must model your solution using a
        KTable, because a new record with the same id must not be treated as an additional record,
        but it should update the earlier record for the same key (id), so you must use KTable to
        compute your aggregates in such cases
*/
    }
}
