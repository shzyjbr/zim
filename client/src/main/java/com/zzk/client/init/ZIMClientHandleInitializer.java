package com.zzk.client.init;

import com.zzk.client.handle.ZIMClientHandle;
import com.zzk.common.protocol.ZIMResponseProto;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;


public class ZIMClientHandleInitializer extends ChannelInitializer<Channel> {

    private final ZIMClientHandle cimClientHandle = new ZIMClientHandle();

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline()

                // 将IdleStateHandler 添加到 ChannelPipeline 中
                //10 秒没发送消息产生写空闲事件，触发心跳包发送
                .addLast(new IdleStateHandler(0, 10, 0))

                //心跳解码
//                .addLast(new HeartbeatEncode())

                // google Protobuf 编解码
                //拆包解码 ProtobufVarint32FrameDecoder可以解决粘包问题
                .addLast(new ProtobufVarint32FrameDecoder())
                .addLast(new ProtobufDecoder(ZIMResponseProto.ZIMResProtocol.getDefaultInstance()))
                //
                //拆包编码 ProtobufVarint32LengthFieldPrepender可以解决粘包问题
                .addLast(new ProtobufVarint32LengthFieldPrepender())
                .addLast(new ProtobufEncoder())
                .addLast(cimClientHandle)
        ;
    }
}
