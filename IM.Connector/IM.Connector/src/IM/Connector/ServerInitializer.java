package IM.Connector;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class ServerInitializer extends ChannelInitializer<SocketChannel> {

	private static final MsgProtoSerializer pbser=new MsgProtoSerializer();
	
	@Override
	public void exceptionCaught(ChannelHandlerContext context, Throwable throwable) throws Exception {
		// TODO Auto-generated method stub
		//System.out.println("exceptionCaught:"+context.name()+","+throwable.toString());
		super.exceptionCaught(context, throwable);
	}

	@Override
	protected void initChannel(SocketChannel channel) throws Exception {
		// TODO Auto-generated method stub
		//System.out.print("initChannel");
		channel.pipeline().addLast(new ClientMsgHandler(pbser));
		//channel.pipeline().addLast(new FromAppHandler());
	}

}
