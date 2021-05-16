package com.any.scheduled;

import com.any.pojo.Miss;
import com.any.send.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.util.Date;

/**
 * 功能描述: 定时任务执行
 *
 * @param:
 * @return:
 * @Date: 2020/12/23 10:37
 */
@Component
public class MyScheduled {

    @Autowired
    private SendMessage sendMessage;

    /*每天下午5:20执行此方法*/
    @Scheduled(cron ="0 20 17 * * *")
    public void dsrw(){
        String message = sendMessage.getOneS();
        sendMessage.sendMessage("来自Sometimes的消息！❤",message);
        System.out.println(new Date().toLocaleString() +" 发送消息：" + message);
    }
}
