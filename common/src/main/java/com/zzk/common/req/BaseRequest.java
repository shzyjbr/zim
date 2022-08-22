package com.zzk.common.req;


public class BaseRequest {


    /**
     * reqNo: 唯一请求号
     */

    private String reqNo;

    /**
     * 当前请求的时间戳
     */
    private int timeStamp;



    public BaseRequest() {
        this.setTimeStamp((int)(System.currentTimeMillis() / 1000));
    }

    public String getReqNo() {
        return reqNo;
    }

    public void setReqNo(String reqNo) {
        this.reqNo = reqNo;
    }

    public int getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(int timeStamp) {
        this.timeStamp = timeStamp;
    }

}
