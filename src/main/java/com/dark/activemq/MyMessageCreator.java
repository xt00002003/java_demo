package com.dark.activemq;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import com.alibaba.fastjson.JSON;
//import com.spfood.wos.workOrder.intf.domain.ReceiveTask;
//import com.spfood.wos.workOrder.intf.domain.ReceiveTaskGoods;
import org.springframework.jms.core.MessageCreator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2017/3/1.
 */
public class MyMessageCreator implements MessageCreator {
    public int n = 0;
    private static String str1 = "这个是第 ";
    private static String str2 = " 个测试消息！";
    private String str = "";
    @Override
    public Message createMessage(Session paramSession) throws JMSException {
        System.out.println("MyMessageCreator  n=" + n);
        if (n == 9) {
            //在这个例子中表示第9次调用时，发送结束消息
            return paramSession.createTextMessage("end");
        }
        str = str1 + n + str2;

//        ReceiveTask receiveTask=new ReceiveTask();
//        receiveTask.setReccode("LL00"+ new  Random().nextInt());
//        receiveTask.setTasktime(new Date());
//        ReceiveTaskGoods receiveTaskGoods=new ReceiveTaskGoods();
//        receiveTaskGoods.setName("清白江");
//        receiveTaskGoods.setStandard("测试中文");
//        List<ReceiveTaskGoods> receiveTaskGoodsList=new ArrayList<>();
//        receiveTaskGoodsList.add(receiveTaskGoods);
//        receiveTask.setGoodsList(receiveTaskGoodsList);
//        String result=JSON.toJSONString(receiveTask);
        return paramSession.createTextMessage(str);
    }
}
