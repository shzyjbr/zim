package com.zzk.client.service;

import org.springframework.stereotype.Component;

@Component
public class ShutDownMsg {
    private volatile boolean isCommand ;

    /**
     * 置为用户主动退出状态
     */
    public void shutdown(){
        isCommand = true ;
    }

    public boolean checkStatus(){
        return isCommand ;
    }
}
