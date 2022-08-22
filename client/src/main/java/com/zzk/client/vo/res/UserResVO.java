package com.zzk.client.vo.res;

/**
 * @author zzk
 * @date 2022/8/14 13:26
 * @desctiption
 */
public class UserResVO {
    private String code;
    private String message;
    private String token;
    private ZIMServerResVO.ServerInfo dataBody;

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ZIMServerResVO.ServerInfo getDataBody() {
        return dataBody;
    }

    public void setDataBody(ZIMServerResVO.ServerInfo dataBody) {
        this.dataBody = dataBody;
    }

    @Override
    public String toString() {
        return "UserRepVO{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", token='" + token + '\'' +
                ", dataBody=" + dataBody +
                '}';
    }
}
