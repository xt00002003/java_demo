package com.dark.schedule.util;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;

import java.nio.charset.Charset;

/**
 * Created by dark on 2017/6/2.
 * 使用Curator 对zookeeper进行操作
 */
public class ZKUtilsDemo {

    private static final CuratorFramework client;
    static {
        // 连接时间 和重试次数
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        client= CuratorFrameworkFactory.newClient("127.0.0.1:2181", retryPolicy);
        client.start();
    }

    public static void createNode(String path,String value)throws Exception{
        Stat stat=client.checkExists().forPath(path);
        if (null==stat){
            client.create().creatingParentContainersIfNeeded().forPath(path,value.getBytes(Charset.forName("utf-8")));
        }

    }

    public static void getDataNode(String path) throws Exception{
        Stat stat = client.checkExists().forPath(path);
        System.out.println(stat);
        byte[] datas = client.getData().forPath(path);
        System.out.println(new String(datas));
    }



}
