package IM.Client.NettyClient;

import IM.Client.IMsgSerializer;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;

public class ClientInitializer extends ChannelInitializer<SocketChannel> {
    private IMsgSerializer _pb;
    
    private static final int MAX_BAG_LEN=1024*1024*5;
    
    private NettyClients _clients;
	
    public ClientInitializer(IMsgSerializer msgSerializer,NettyClients clients) {
    	_pb=msgSerializer;
    	_clients=clients;
    }
    
	@Override
	protected void initChannel(SocketChannel channel) throws Exception {
		// TODO Auto-generated method stub
		channel.pipeline()
		.addLast(new IdleStateHandler(0, 0, 60))
		//.addLast(new ProtobufVarint32FrameDecoder())
		.addLast(new LengthFieldBasedFrameDecoder(MAX_BAG_LEN,0,4,0,4))
        .addLast(new NettyClientHandler(_pb,_clients)); 
	}

}
