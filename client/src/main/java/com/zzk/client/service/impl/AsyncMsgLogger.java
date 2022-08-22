package com.zzk.client.service.impl;

import com.zzk.client.config.AppConfiguration;
import com.zzk.client.service.MsgLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AsyncMsgLogger implements MsgLogger {

    private final static Logger LOGGER = LoggerFactory.getLogger(AsyncMsgLogger.class);

    /**
     * The default buffer size.
     */
    private static final int DEFAULT_QUEUE_SIZE = 16;
    private BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<String>(DEFAULT_QUEUE_SIZE);

    /**
     * 控制workder启动停止
     * 多线程下要用volatile， 使得其他线程对stated的状态实时可见
     * 任何对started的改变都会刷新回主内存
     */
    private volatile boolean started = false;
    /**
     * workder继承了Thread, 是一个线程类
     */
    private Worker worker = new Worker();

    /**
     * 需要取得其中配置的聊天记录存储路径
     * 这里其实用@Value单独取存储路径也可以
     */
    @Autowired
    private AppConfiguration appConfiguration;


    /**
     * 生产者，消息队列任务来源
     * 产生消息队列的任务，该任务是把一个聊天消息进行持久化
     * @param msg
     */
    @Override
    public void log(String msg) {
        //开始消费
        startMsgLogger();
        try {
            //这里是有界队列， 消息队列满了会导致生产者阻塞在这里
            blockingQueue.put(msg);
        } catch (InterruptedException e) {
            LOGGER.error("InterruptedException", e);
        }
    }

    /**
     * zzk:启动一个线程不停从消费队列中取出消息进行持久化
     */
    private class Worker extends Thread {
        @Override
        public void run() {
            while (started) {
                try {
                    //这里使用的是有界队列， 消息队列为空时worker线程会阻塞在这里
                    String msg = blockingQueue.take();
                    writeLog(msg);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }

    }


    /**
     * zzk:处理聊天记录持久化，根据logdir/username/年月日.log进行存放
     * @param msg
     */
    private void writeLog(String msg) {

        LocalDate today = LocalDate.now();
        int year = today.getYear();
        int month = today.getMonthValue();
        int day = today.getDayOfMonth();

        String dir = appConfiguration.getMsgLoggerPath() + appConfiguration.getUserName() + "/";
        String fileName = dir + year + month + day + ".log";

        Path file = Paths.get(fileName);
        boolean exists = Files.exists(Paths.get(dir), LinkOption.NOFOLLOW_LINKS);
        try {
            if (!exists) {
                Files.createDirectories(Paths.get(dir));
            }

            List<String> lines = Arrays.asList(msg);

            Files.write(file, lines, Charset.forName("UTF-8"), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            LOGGER.info("IOException", e);
        }

    }

    /**
     * 开始工作
     */
    private void startMsgLogger() {
        if (started) {
            return;
        }

        worker.setDaemon(true);
        worker.setName("AsyncMsgLogger-Worker");
        started = true;
        worker.start();
    }


    @Override
    public void stop() {
        started = false;
        worker.interrupt();
    }

    /**
     * 聊天记录关键字查询
     * 从对应用户聊天记录持久化目录下读出所有的文件，并查找里面的内容，找到就添加到StringBuilder
     * @param key 关键字
     * @return
     */
    @Override
    public String query(String key) {
        StringBuilder sb = new StringBuilder();

        Path path = Paths.get(appConfiguration.getMsgLoggerPath() + appConfiguration.getUserName() + "/");

        try {
            Stream<Path> list = Files.list(path);
            List<Path> collect = list.collect(Collectors.toList());
            for (Path file : collect) {
                //读出一个文件的所有内容
                List<String> strings = Files.readAllLines(file);
                //逐行检测是否有符合条件的
                for (String msg : strings) {
                    if (msg.trim().contains(key)) {
                        sb.append(msg).append("\n");
                    }
                }

            }
        } catch (IOException e) {
            LOGGER.info("IOException", e);
        }

        return sb.toString().replace(key, "\033[31;4m" + key + "\033[0m");
    }
}
