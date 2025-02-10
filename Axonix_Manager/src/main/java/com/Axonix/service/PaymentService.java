package com.Axonix.service;

import com.Axonix.demo.model.Payment;
import com.Axonix.demo.model.PaymentExample;
import java.util.List;

public interface PaymentService {

    long countByExample(PaymentExample example);

    int deleteByExample(PaymentExample example);

    int deleteById(Integer id);

    int insert(Payment record);

    int insertSelective(Payment record);

    List<Payment> selectByExample(PaymentExample example);

    Payment selectById(Integer id);

    int updateByExampleSelective(Payment record, PaymentExample example);

    int updateByExample(Payment record, PaymentExample example);

    int updateByIdSelective(Payment record);

    int updateById(Payment record);
}
