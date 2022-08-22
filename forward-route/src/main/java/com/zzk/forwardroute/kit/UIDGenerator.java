package com.zzk.forwardroute.kit;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UIDGenerator {
    public String getUID() {
        //目前使用UUID来做， 后期可以作为一个扩展点更改，所以单独写一个类
        String uid = UUID.randomUUID().toString().replaceAll("-", "");
        return uid;
    }


}
