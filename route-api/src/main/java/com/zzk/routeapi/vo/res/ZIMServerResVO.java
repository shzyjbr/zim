package com.zzk.routeapi.vo.res;

import com.zzk.common.pojo.RouteInfo;

import java.io.Serializable;

public class ZIMServerResVO implements Serializable {

    private String ip ;
    private Integer zimServerPort;
    private Integer httpPort;

    public ZIMServerResVO() {
    }

    public ZIMServerResVO(RouteInfo routeInfo) {
        this.ip = routeInfo.getIp();
        this.zimServerPort = routeInfo.getCimServerPort();
        this.httpPort = routeInfo.getHttpPort();
    }

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
        return "ZIMServerResVO{" +
                "ip='" + ip + '\'' +
                ", zimServerPort=" + zimServerPort +
                ", httpPort=" + httpPort +
                '}';
    }
}
