package com.zzk.client.scanner;

import com.vdurmont.emoji.EmojiParser;
import com.zzk.client.config.AppConfiguration;
import com.zzk.client.service.EchoService;
import com.zzk.client.service.MsgHandle;
import com.zzk.client.service.MsgLogger;
import com.zzk.client.util.SpringBeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class Scan implements Runnable {

    private final static Logger LOGGER = LoggerFactory.getLogger(Scan.class);

    /**
     * 系统参数
     */
    private AppConfiguration configuration;

    private MsgHandle msgHandle ;

    private MsgLogger msgLogger ;

    private EchoService echoService ;

    public Scan() {
        this.configuration = SpringBeanFactory.getBean(AppConfiguration.class);
        this.msgHandle = SpringBeanFactory.getBean(MsgHandle.class) ;
        this.msgLogger = SpringBeanFactory.getBean(MsgLogger.class) ;
        this.echoService = SpringBeanFactory.getBean(EchoService.class) ;
    }

    @Override
    public void run() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            String msg = sc.nextLine();

            //检查消息
            if (msgHandle.checkMsg(msg)) {
                continue;
            }

            //系统内置命令
            if (msgHandle.innerCommand(msg)){
                continue;
            }

            //真正的发送消息
            msgHandle.sendMsg(msg) ;

            //写入聊天记录
            msgLogger.log(msg) ;

            echoService.echo(EmojiParser.parseToUnicode(msg));
        }
    }

}
