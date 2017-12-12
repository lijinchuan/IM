package IM.Client.NettyClient;

import java.nio.ByteBuffer;

import IM.Client.IMsgSerializer;
import IM.Contract.MsgType;
import IM.Util.ThreadPoolUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;

public class NettyClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

	private IMsgSerializer _msgSerializer;
	
	private NettyClients _clients;

	public NettyClientHandler(IMsgSerializer msgSerializer,NettyClients clients) {
		this._msgSerializer = msgSerializer;
		this._clients=clients;
	}

	private void readFinished(ChannelHandlerContext ctx, ByteBuffer data) throws Exception {
		data.flip();
		// System.out.println(new String(data.array(), "utf-8"));
		MsgType msgtype =MsgType.valueOf(data.getShort() & 65535);
		switch (msgtype) {
		case ECHO: {
			System.out.println("客户端回显:" + (String) this._msgSerializer.readObject(msgtype, data));
			break;
		}
		case HeartBeat: {
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
	
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		// TODO Auto-generated method stub
		 if (evt instanceof IdleStateEvent) {
	            IdleStateEvent e = (IdleStateEvent) evt;
	            switch (e.state()) {
	                case READER_IDLE:
	                    handleReaderIdle(ctx);
	                    break;
	                case WRITER_IDLE:
	                    handleWriterIdle(ctx);
	                    break;
	                case ALL_IDLE:
	                    handleAllIdle(ctx);
	                    break;
	                default:
	                    break;
	            }
	        }
	}
	
	protected void handleReaderIdle(ChannelHandlerContext ctx) {
      
    }

    protected void handleWriterIdle(ChannelHandlerContext ctx) {
        
    }

    protected void handleAllIdle(ChannelHandlerContext ctx) throws Exception {
        Request.sendHeartbeat(ctx, this._msgSerializer);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    	// TODO Auto-generated method stub
    	if(ctx.channel().isActive()) {
    		return;
    	}
    
    	System.out.println("channelInactive");
    	_clients.channelReconnect(ctx.channel());
    }
}
