package com.zzk.client;

import com.zzk.client.scanner.Scan;
import com.zzk.client.service.impl.ClientInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ZimClientApplication implements CommandLineRunner {

    private final static Logger LOGGER = LoggerFactory.getLogger(ZimClientApplication.class);

    @Autowired
    private ClientInfo clientInfo;

    public static void main(String[] args) {
        SpringApplication.run(ZimClientApplication.class, args);
        LOGGER.info("启动 Client 服务成功");
    }

    /**
     * 客户端做消息持久化、向网关发起登录并获取一台IM-Server、注销下线、连接IM-Server、断线重连等
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        Scan scan = new Scan() ;
        Thread thread = new Thread(scan);
        thread.setName("scan-thread");
        thread.start();
        clientInfo.saveStartDate();
    }
}
