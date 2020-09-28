package com.hy.rabbit.controller;

import com.hy.rabbit.producer.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SendController {

    @Autowired
    private Producer producer;

    @GetMapping("/send")
    public void send(){
        producer.send();
    }
}
