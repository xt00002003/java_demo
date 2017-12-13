package com.dark.schedule;

import com.dark.schedule.core.Version;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.ACLProvider;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Created by dark on 2017/6/2.
 */
public class ZKManager {

    private static transient Logger log = LoggerFactory.getLogger(ZKManager.class);
    private static CuratorFrameworkFactory.Builder builder=CuratorFrameworkFactory.builder();
    private final Properties properties;
    private final CuratorFramework client;

    private static final String SCHEME="digest";

    public enum keys {
        zkConnectString, rootPath, userName, password, zkSessionTimeout,zkConnectionTimeout, autoRegisterTask, ipBlacklist,namespace
    }

    public ZKManager(Properties aProperties) throws Exception{
        this.properties = aProperties;

        final String authString = this.properties.getProperty(keys.userName.toString())
                + ":"+ this.properties.getProperty(keys.password.toString());
        ACLProvider aclProvider = new ACLProvider() {
            private List<ACL> acl ;
            @Override
            public List<ACL> getDefaultAcl() {
                if(acl ==null){
                    ArrayList<ACL> acl = ZooDefs.Ids.CREATOR_ALL_ACL;
                    acl.clear();
                    try {
                        acl.add(new ACL(ZooDefs.Perms.ALL, new Id(SCHEME, DigestAuthenticationProvider.generateDigest(authString)) ));
                        acl.add(new ACL(ZooDefs.Perms.READ, ZooDefs.Ids.ANYONE_ID_UNSAFE));
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    this.acl = acl;
                }
                return acl;
            }
            @Override
            public List<ACL> getAclForPath(String path) {
                return acl;
            }
        };
        client=builder.connectString(this.properties.getProperty(keys.zkConnectString
                .toString()))
                .authorization(SCHEME,authString.getBytes(Charset.forName("UTF-8")))
                .aclProvider(aclProvider)
                .sessionTimeoutMs(Integer.parseInt(this.properties.getProperty(keys.zkSessionTimeout
                        .toString())))
                .connectionTimeoutMs(Integer.parseInt(this.properties.getProperty(keys.zkConnectionTimeout
                        .toString())))
                .canBeReadOnly(false)
                .retryPolicy(new ExponentialBackoffRetry(1000, Integer.MAX_VALUE))
                .namespace(this.properties.getProperty(keys.zkConnectString
                        .toString()))
                .defaultData(null)
                .build();
    }

    String getRootPath(){
        return this.properties.getProperty(keys.rootPath.toString());
    }

    public List<String> getIpBlacklist(){
        List<String> ips = new ArrayList<String>();
        String list = this.properties.getProperty(keys.ipBlacklist.toString());
        if(StringUtils.isNotEmpty(list)){
            ips = Arrays.asList(list.split(","));
        }
        return ips;
    }
    public String getConnectStr(){
        return this.properties.getProperty(keys.zkConnectString.toString());
    }

    public void close() throws InterruptedException {
        log.info("关闭zookeeper连接");
        client.close();
    }


}
