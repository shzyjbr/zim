package com.zzk.client.vo.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OnlineUsersResVO {


    /**
     * code : 9000
     * message : 成功
     * reqNo : null
     * dataBody : [{"userId":1545574841528,"userName":"zhangsan"},{"userId":1545574871143,"userName":"lisi"}]
     */

    private String code;
    private String message;
    private Object reqNo;
    private Set<DataBodyBean> dataBody;


    /**
     * 这个其实就是ZIMUserInfo
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataBodyBean {
        /**
         * uid : 32位uid
         * userName : zhangsan
         */

        private String uid;
        private String userName;
    }

}
