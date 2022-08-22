package com.zzk.common.protocol;

import com.google.protobuf.InvalidProtocolBufferException;

/**
 * Function: 封装了消息的编解码， 由于使用了Protobuf ，因此实现很简易
 *
 * @author crossoverJie
 *         Date: 2018/8/1 12:24
 * @since JDK 1.8
 */
public class ProtocolUtil {

    public static void main(String[] args) throws InvalidProtocolBufferException {
        ZIMRequestProto.ZIMReqProtocol protocol = ZIMRequestProto.ZIMReqProtocol.newBuilder()
                .setUsername("zhouzekun")
                .setReqMsg("你好啊")
                .build();

        byte[] encode = encode(protocol);

        ZIMRequestProto.ZIMReqProtocol parseFrom = decode(encode);

        System.out.println(protocol.toString());
        System.out.println(protocol.toString().equals(parseFrom.toString()));
    }

    /**
     * 编码
     * @param protocol
     * @return
     */
    public static byte[] encode(ZIMRequestProto.ZIMReqProtocol protocol){
        return protocol.toByteArray() ;
    }

    /**
     * 解码
     * @param bytes
     * @return
     * @throws InvalidProtocolBufferException
     */
    public static ZIMRequestProto.ZIMReqProtocol decode(byte[] bytes) throws InvalidProtocolBufferException {
        return ZIMRequestProto.ZIMReqProtocol.parseFrom(bytes);
    }
}
