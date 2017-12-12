package IM.Client.NettyClient;

import IM.Client.IMsgSerializer;
import IM.Contract.MsgType;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

public class Request {
	public static boolean sendHeartbeat(ChannelHandlerContext ctx, IMsgSerializer seri) throws Exception {
		byte[] data = seri.writeObject(MsgType.HeartBeat, null);
		ctx.writeAndFlush(Unpooled.copiedBuffer(data));
		return true;
	}
}
