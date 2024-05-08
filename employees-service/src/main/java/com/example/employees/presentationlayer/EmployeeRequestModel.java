package com.example.employees.presentationlayer;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
public class EmployeeRequestModel {

    private String employeeId;
    private String lastName;
    private String firstName;
    private String jobTitle;
    private String department;
    private BigDecimal salary;

}