package cn.cc.rabbotmq.controller;

import cn.cc.rabbotmq.producer.MsgProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {
    @Autowired
    private MsgProducer producer ;
    @ResponseBody
    @RequestMapping("/send")
    public String send(){
        for (int i = 0; i < 30; i++) {
            producer.sendMessage("chenchao " + i);
        }
        return "sucess";
    }
}
