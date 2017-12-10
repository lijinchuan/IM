package IM.Client.NettyClient;

import IM.Client.IMsgSerializer;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;

public class ClientInitializer extends ChannelInitializer<SocketChannel> {
    private IMsgSerializer _pb;
	
    public ClientInitializer(IMsgSerializer msgSerializer) {
    	_pb=msgSerializer;
    }
    
	@Override
	protected void initChannel(SocketChannel channel) throws Exception {
		// TODO Auto-generated method stub
		channel.pipeline()
		.addLast(new ProtobufVarint32FrameDecoder())
        .addLast(new NettyClientHandler(_pb)); 
	}

}
