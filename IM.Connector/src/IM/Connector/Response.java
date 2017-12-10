package IM.Connector;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import IM.Contract.MsgType;
import IM.Contract.ResponseCode;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.GenericFutureListener;

public class Response {
	private static boolean sendMsg(ChannelHandlerContext ctx,ByteBuffer bag) {
		bag.flip();
		ctx.writeAndFlush(Unpooled.copiedBuffer(bag));
		
		return true;
	}
	
	private static boolean sendMsg(ChannelHandlerContext ctx,IM.Contract.MsgType msgtype,byte[] data) {
		ByteBuffer bag=ByteBuffer.allocateDirect(6+data.length).putInt(data.length+2)
				.putShort((short)msgtype.getVal()).put(data);
		
		return sendMsg(ctx,bag);
	}
	
	public static boolean sendResponseCode(ChannelHandlerContext ctx, IMsgSerializer seri, ResponseCode code) throws Exception {
		try {
			byte[] data = seri.writeObject(MsgType.ResponeCode, code);
			if (data != null) {
				ctx.channel().writeAndFlush(data);
			}
			return true;
		} catch (Exception ex) {
			throw ex;
		}
	}
	
	public static boolean sendEcho(ChannelHandlerContext ctx, IMsgSerializer seri, String echo) throws Exception {
		try {
			byte[] data=seri.writeObject(MsgType.ECHO, echo);
			if(data!=null) {
				
				sendMsg(ctx,MsgType.ECHO,data);
			}
			return true;
		} catch (Exception ex) {
			throw ex;
		}
	}
}
