package com.atguigu.gmall.payment.controller;

import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaController {

    private static final Logger logger = LoggerFactory.getLogger(KafkaController.class);

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;
    //这里相当于消息生产者，进行消息发送，如果要传输对象，可以序列化成字符串进行发送，接收到后进行反序列化成对象即可
    @RequestMapping("test/kafka")
    public String testKafka(@Param("data") String data) {
        logger.info(kafkaTemplate + "：注入成功");
        kafkaTemplate.send("lichuang",data);
        return "成功";
    }
    //这里相当于消息接收者，进行消息接收后处理
    @KafkaListener(topics = {"lichuang"})
    public void processMessage(String content) {
        logger.info("接收成功呀：内容为：{}",content);
    }
}
