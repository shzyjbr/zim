package com.zzk.client.service.impl;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ClientInfo {

    private Info info = new Info();

    public Info get() {
        return info;
    }

    /**
     * 链式调用风格
     */
    public ClientInfo saveUserInfo(String uid, String userName) {
        info.setUid(uid);
        info.setUserName(userName);
        return this;
    }


    public ClientInfo saveServiceInfo(String serviceInfo) {
        info.setServiceInfo(serviceInfo);
        return this;
    }

    public ClientInfo saveToken(String token) {
        info.setToken(token);
        return this;
    }

    public ClientInfo saveStartDate() {
        info.setStartDate(new Date());
        return this;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class Info {
        private String userName;
        private String uid;
        private String serviceInfo;
        private Date startDate;
        private String token;
    }

}
