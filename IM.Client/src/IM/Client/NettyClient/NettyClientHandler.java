package IM.Client.NettyClient;

import java.nio.ByteBuffer;

import IM.Client.IMsgSerializer;
import IM.Util.ThreadPoolUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class NettyClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

	private IMsgSerializer _msgSerializer;

	public NettyClientHandler(IMsgSerializer msgSerializer) {
		this._msgSerializer = msgSerializer;
	}

	private void readFinished(ChannelHandlerContext ctx, ByteBuffer data) throws Exception {
		data.flip();
		// System.out.println(new String(data.array(), "utf-8"));
		int msgtype = data.getShort() & 65535;
		switch (msgtype) {
		case 0: {
			System.out.println("客户端回显:" + (String) this._msgSerializer.ReadObject(msgtype, data));
			break;
		}
		case 1: {
			break;
		}
		default: {
			// 说明,除非任务比较轻,不要直接处理,要交给MsgProcessor
			IM.Client.MsgProcessor proc = new IM.Client.MsgProcessor(msgtype, data, ctx);
			ThreadPoolUtil.QueueUserWorkItem(proc);
			break;
		}
		}

	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {
		// TODO Auto-generated method stub
		int ablelen = buf.readableBytes();
		if(ablelen==0) {
			return;
		}
		ByteBuffer bb = ByteBuffer.allocate(ablelen);
        bb.clear();
		buf.getBytes(0, bb);
		try {
			readFinished(ctx, bb);
		} finally {
			//this.acceptInboundMessage(buf);
			buf.clear();
		}
	}

}
