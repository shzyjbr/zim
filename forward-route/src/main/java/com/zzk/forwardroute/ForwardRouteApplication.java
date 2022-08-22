package com.zzk.forwardroute;

import com.zzk.forwardroute.kit.ServerListListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动转发路由
 */
@SpringBootApplication
public class ForwardRouteApplication implements CommandLineRunner {
    private final static Logger LOGGER = LoggerFactory.getLogger(ForwardRouteApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ForwardRouteApplication.class, args);
        LOGGER.info("#########Start forward-route success!###################");
    }

    @Override
    public void run(String... args) throws Exception {
        //监听服务
        Thread thread = new Thread(new ServerListListener());
        thread.setName("zookeeper-listener");
        thread.start() ;

    }
}
