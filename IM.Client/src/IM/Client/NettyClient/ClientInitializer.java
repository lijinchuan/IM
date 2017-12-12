package IM.Client.NettyClient;

import IM.Client.IMsgSerializer;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;

public class ClientInitializer extends ChannelInitializer<SocketChannel> {
    private IMsgSerializer _pb;
	
    public ClientInitializer(IMsgSerializer msgSerializer) {
    	_pb=msgSerializer;
    }
    
	@Override
	protected void initChannel(SocketChannel channel) throws Exception {
		// TODO Auto-generated method stub
		channel.pipeline()
		.addLast(new IdleStateHandler(0, 0, 60))
		.addLast(new ProtobufVarint32FrameDecoder())
        .addLast(new NettyClientHandler(_pb)); 
	}

}
