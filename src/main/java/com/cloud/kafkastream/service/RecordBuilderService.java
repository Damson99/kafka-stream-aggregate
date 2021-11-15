package com.cloud.kafkastream.service;

import com.cloud.kafkastream.web.v1.model.DepartmentAggregate;
import com.cloud.kafkastream.web.v1.model.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.cloud.kafkastream.web.v1.model.Notification;
import com.cloud.kafkastream.web.v1.model.PosInvoice;

@Slf4j
@Service
public class RecordBuilderService {
    public Notification getPosInvoiceNotification(PosInvoice posInvoice){

        Notification notification= Notification.newBuilder()
                .setInvoiceNumber(posInvoice.getInvoiceNumber())
                .setCustomerCardNo(posInvoice.getCustomerCardNo())
                .setTotalAmount(posInvoice.getTotalAmount())
                .setEarnedLoyaltyPoints(posInvoice.getTotalAmount()*0.02)
                .build();
        notification.setTotalLoyaltyPoints(notification.getEarnedLoyaltyPoints());
        return notification;
    }


    public DepartmentAggregate initBlankRecord() {

        return departmentAggregateBuilder(0, 0, 0D);
    }

    public DepartmentAggregate aggregate(Employee emp, DepartmentAggregate aggVal) {

        return departmentAggregateBuilder(
                aggVal.getEmployeeCount() + 1,
                aggVal.getTotalSalary() + emp.getSalary(),
                (aggVal.getAvgSalary() + emp.getSalary()) / aggVal.getEmployeeCount()
        );
    }

    private DepartmentAggregate departmentAggregateBuilder(Integer empCount, Integer totalSalary, Double avgSalary) {

        return DepartmentAggregate.newBuilder()
//                increment value +1
                .setEmployeeCount(empCount)
//                setting total salary
                .setTotalSalary(totalSalary)
//                setting average salary
                .setAvgSalary(avgSalary)
                .build();
    }


}
