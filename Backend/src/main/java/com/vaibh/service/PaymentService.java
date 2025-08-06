package com.vaibh.service;

import com.vaibh.domain.PaymentMethod;
import com.vaibh.model.PaymentOrder;
import com.vaibh.model.User;

public interface PaymentService {

    PaymentOrder createOrder(User user, Long amount, PaymentMethod paymentMethod);

    PaymentOrder getPaymentOrderById(Long id) throws Exception;

    Boolean ProccedPaymentOrder (PaymentOrder paymentOrder,
                                 String paymentId);
}
