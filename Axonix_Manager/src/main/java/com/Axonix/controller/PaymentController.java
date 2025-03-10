package com.Axonix.controller;

import com.Axonix.demo.model.Payment;
import com.Axonix.demo.model.PaymentExample;
import com.Axonix.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
@CrossOrigin(origins = "http://localhost:5173")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/count")
    public long count(PaymentExample example) {
        return paymentService.countByExample(example);
    }

    @DeleteMapping("/{id}")
    public int deleteById(@PathVariable Integer id) {
        return paymentService.deleteById(id);
    }

    @PostMapping("/insert")
    public int insert(@RequestBody Payment record) {
        return paymentService.insert(record);
    }

    @PostMapping("/selective")
    public int insertSelective(@RequestBody Payment record) {
        return paymentService.insertSelective(record);
    }

    @PostMapping("/list")
    public List<Payment> getList(@RequestBody PaymentExample example) {
        return paymentService.selectByExample(example);
    }

    @GetMapping("/{id}")
    public Payment getById(@PathVariable Integer id) {
        return paymentService.selectById(id);
    }

    @PutMapping("/{id}")
    public int updateById(@PathVariable Integer id, @RequestBody Payment record) {
        record.setId(id);
        return paymentService.updateById(record);
    }

    @PatchMapping("/{id}/selective")
    public int updateByIdSelective(@PathVariable Integer id, @RequestBody Payment record) {
        record.setId(id);
        return paymentService.updateByIdSelective(record);
    }
}
