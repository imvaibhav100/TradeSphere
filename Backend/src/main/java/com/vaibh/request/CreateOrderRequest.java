package com.vaibh.request;

import com.vaibh.domain.OrderType;

// ...existing code...
import lombok.Data;

// ...existing code...


@Data
public class CreateOrderRequest {
    private String coinId;
    private double quantity;
    private OrderType orderType;
}
