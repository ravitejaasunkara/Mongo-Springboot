package com.ravi.mongo.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private Integer order_id;
    private Double amount;
    private String order_date;
    private String customer_name;
    private String customer_email;
    private String product_name;
    private String payment_method;
    private Double payment_amount;
}
