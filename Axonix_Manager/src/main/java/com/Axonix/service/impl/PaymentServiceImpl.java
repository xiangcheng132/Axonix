package com.Axonix.service.impl;

import com.Axonix.demo.mapper.PaymentMapper;
import com.Axonix.demo.model.Payment;
import com.Axonix.demo.model.PaymentExample;
import com.Axonix.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentMapper paymentMapper;

    @Override
    public long countByExample(PaymentExample example) {
        return paymentMapper.countByExample(example);
    }

    @Override
    public int deleteByExample(PaymentExample example) {
        return paymentMapper.deleteByExample(example);
    }

    @Override
    public int deleteById(Integer id) {
        return paymentMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Payment record) {
        return paymentMapper.insert(record);
    }

    @Override
    public int insertSelective(Payment record) {
        return paymentMapper.insertSelective(record);
    }

    @Override
    public List<Payment> selectByExample(PaymentExample example) {
        return paymentMapper.selectByExample(example);
    }

    @Override
    public Payment selectById(Integer id) {
        return paymentMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByExampleSelective(Payment record, PaymentExample example) {
        return paymentMapper.updateByExampleSelective(record, example);
    }

    @Override
    public int updateByExample(Payment record, PaymentExample example) {
        return paymentMapper.updateByExample(record, example);
    }

    @Override
    public int updateByIdSelective(Payment record) {
        return paymentMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateById(Payment record) {
        return paymentMapper.updateByPrimaryKey(record);
    }
}
