package com.zzk.client.vo.res;

import java.io.Serializable;

public class ZIMServerResVO implements Serializable {

    /**
     * code : 9000
     * message : 成功
     * reqNo : null
     * dataBody : {"ip":"127.0.0.1","port":8081}
     */

    private String code;
    private String message;
    private Object reqNo;
    private ServerInfo dataBody;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getReqNo() {
        return reqNo;
    }

    public void setReqNo(Object reqNo) {
        this.reqNo = reqNo;
    }

    public ServerInfo getDataBody() {
        return dataBody;
    }

    public void setDataBody(ServerInfo dataBody) {
        this.dataBody = dataBody;
    }

    public static class ServerInfo {
        /**
         * ip : 127.0.0.1
         * zimServerPort:21811   监听客户端Netty通信的端口
         * port : 8081
         */
        private String ip ;
        private Integer zimServerPort;
        private Integer httpPort;

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public Integer getZimServerPort() {
            return zimServerPort;
        }

        public void setZimServerPort(Integer zimServerPort) {
            this.zimServerPort = zimServerPort;
        }

        public Integer getHttpPort() {
            return httpPort;
        }

        public void setHttpPort(Integer httpPort) {
            this.httpPort = httpPort;
        }

        @Override
        public String toString() {
            return "ServerInfo{" +
                    "ip='" + ip + '\'' +
                    ", zimServerPort=" + zimServerPort +
                    ", httpPort=" + httpPort +
                    '}';
        }
    }


    @Override
    public String toString() {
        return "ZIMServerResVO{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", reqNo=" + reqNo +
                ", dataBody=" + dataBody +
                '}';
    }
}
