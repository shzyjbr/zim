package com.zzk.client.service;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.zzk.client.thread.ReConnectJob;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

@Component
public final class ReConnectManager {

    private ScheduledExecutorService scheduledExecutorService;

    /**
     * 进行重连
     * Trigger reconnect job
     *
     * @param ctx
     */
    public void reConnect(ChannelHandlerContext ctx) {
        buildExecutor();
        //每隔10秒进行重连
        scheduledExecutorService.scheduleAtFixedRate(new ReConnectJob(ctx), 0, 10, TimeUnit.SECONDS);
    }

    /**
     * Close reconnect job if reconnect success.
     */
    public void reConnectSuccess() {
        scheduledExecutorService.shutdown();
    }


    /***
     * build an thread executor
     * @return
     */
    private ScheduledExecutorService buildExecutor() {
        if (scheduledExecutorService == null || scheduledExecutorService.isShutdown()) {
            ThreadFactory sche = new ThreadFactoryBuilder()
                    .setNameFormat("reConnect-job-%d")
                    .setDaemon(true)
                    .build();
            scheduledExecutorService = new ScheduledThreadPoolExecutor(1, sche);
            return scheduledExecutorService;
        } else {
            return scheduledExecutorService;
        }
    }
}
