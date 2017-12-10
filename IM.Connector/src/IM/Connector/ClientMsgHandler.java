package IM.Connector;

import java.nio.ByteBuffer;
import java.util.Date;
import java.util.Random;

import IM.Util.ThreadPoolUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;

public class ClientMsgHandler extends ChannelInboundHandlerAdapter {
	// 这个键保存用户会话信息
	final static AttributeKey<Session> sessionattr = AttributeKey.newInstance("session");
	// final static AttributeKey<MsgBuffer>
	// msgbufferattr=AttributeKey.newInstance("msgbuf");

	final static int MAXBAGLEN = 1024 * 1024; // 最大允许单个包长
	final static Exception READLENERROREXCEPTION = new Exception("read data len error");
	final static Exception DATALENETOOLONGEXCEPTION = new Exception("data lenth too long error");

	private MsgBuffer msgbuf = new MsgBuffer();

	private IMsgSerializer _msgSerializer;

	public ClientMsgHandler(IMsgSerializer msgSerializer) {
		this._msgSerializer = msgSerializer;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext context, Throwable cause) throws Exception {
		// TODO Auto-generated method stub
		// super.exceptionCaught(ctx, cause);
		// cause.printStackTrace();
		System.out.println(cause.toString());
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub

		Session session = new Session();
		session.setId("ljc" + new Random().nextInt(1000));
		session.setConntime(new Date());
		ctx.attr(sessionattr).set(session);

		// ctx.attr()
		System.out.println("new user:" + ctx.name());
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("user leave:" + ctx.name());
	}

	public void closeClient(ChannelHandlerContext ctx, int errcode) {
		ctx.close();
		try {
			handlerRemoved(ctx);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void prepareRead(ChannelHandlerContext ctx, ByteBuf directByteBuf, int offset) throws Exception {
		int ablelen = directByteBuf.readableBytes();

		if (msgbuf.getLenBuf().readerIndex() == 0) {
			if (ablelen - offset < 4 && ablelen < directByteBuf.capacity()) {
				exceptionCaught(ctx, READLENERROREXCEPTION);
				this.closeClient(ctx, ServerError.readLenError.getErrorCode());
				return;
			} else if (ablelen - offset < 4 && ablelen == directByteBuf.capacity()) {
				directByteBuf.markReaderIndex();
				directByteBuf.readerIndex(offset).getBytes(offset, msgbuf.getLenBuf(), ablelen - offset);
				msgbuf.getLenBuf().readerIndex(ablelen - offset);
				directByteBuf.resetReaderIndex();
				return;
			}

			int datalen = msgbuf.getDatalen();
			if (datalen == 0) {
				directByteBuf.markWriterIndex();
				datalen = directByteBuf.readerIndex(offset).readInt();
				directByteBuf.resetReaderIndex();
				offset += 4;
			}

			if (datalen < 0 || datalen > MAXBAGLEN) {
				exceptionCaught(ctx, DATALENETOOLONGEXCEPTION);
				this.closeClient(ctx, ServerError.readLenError.getErrorCode());
				return;
			}

			ByteBuffer newdatabuf = ByteBuffer.allocate(datalen);
			if (datalen <= ablelen - offset) {

				directByteBuf.markReaderIndex();
				directByteBuf.readerIndex(offset);
				directByteBuf.getBytes(offset, newdatabuf);
				directByteBuf.resetReaderIndex();

				readFinished(ctx, newdatabuf);
				msgbuf.reSet();

				if (datalen < ablelen - offset) {
					offset += datalen;
					prepareRead(ctx, directByteBuf, offset);
				}
			} else {
				msgbuf.setDatabuf(newdatabuf);
				msgbuf.setDatalen(datalen);

				directByteBuf.markReaderIndex();
				newdatabuf.limit(ablelen - offset);
				directByteBuf.readerIndex(offset).getBytes(offset, newdatabuf);
				newdatabuf.limit(datalen);
				directByteBuf.resetReaderIndex();
				msgbuf.setDatarev(ablelen - offset);
			}
		} else {

			int readlenbytelen = 4 - msgbuf.getLenBuf().readerIndex();

			if (readlenbytelen <= 0) {
				exceptionCaught(ctx, DATALENETOOLONGEXCEPTION);
				this.closeClient(ctx, ServerError.readLenError.getErrorCode());
				return;
			}
			directByteBuf.markReaderIndex();
			directByteBuf.readerIndex(offset).getBytes(offset, msgbuf.getLenBuf(), readlenbytelen);
			msgbuf.getLenBuf().readerIndex(msgbuf.getLenBuf().readerIndex() + readlenbytelen);
			directByteBuf.resetReaderIndex();

			int datalen = msgbuf.getLenBuf().getInt(0);

			if (datalen < 0 || datalen > MAXBAGLEN) {
				this.closeClient(ctx, ServerError.dataTooLenError.getErrorCode());
				exceptionCaught(ctx, DATALENETOOLONGEXCEPTION);
				return;
			}

			offset += readlenbytelen;

			msgbuf.setDatalen(datalen);
			msgbuf.setDatarev(0);
			msgbuf.getLenBuf().clear();
			ByteBuffer buffer = ByteBuffer.allocate(datalen);
			buffer.clear();
			msgbuf.setDatabuf(buffer);
			if (ablelen > offset) {
				prepareRead(ctx, directByteBuf, offset);
			}
		}
	}
	
	private void senMsg(String msg) {
		
	}

	private void readFinished(ChannelHandlerContext ctx, ByteBuffer data) throws Exception {
		data.flip();
		// System.out.println(new String(data.array(), "utf-8"));
		int msgtype = data.getInt();
		switch (msgtype) {
		case 0: {
			System.out.println("回显:"+(String)this._msgSerializer.ReadObject(msgtype, data));
			break;
		}
		case 1:
		{
			break;
		}
		default:
		{
			//说明,除非任务比较轻,不要直接处理,要交给MsgProcessor
			MsgProcessor proc=new MsgProcessor(msgtype, data,ctx);
			ThreadPoolUtil.QueueUserWorkItem(proc);
			break;
		}
		}
		this._msgSerializer.ReadObject(msgtype, data);
		channelReadComplete(ctx);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// TODO Auto-generated method stub
		ByteBuf buf = (ByteBuf) msg;
		if (msgbuf.getDatalen() > 0) {
			int ablelen = buf.readableBytes();

			int leftlen = msgbuf.getDatalen() - msgbuf.getDatarev();

			if (leftlen > ablelen) {
				int oldlimit=msgbuf.getDatabuf().limit();
				msgbuf.getDatabuf().limit(msgbuf.getDatarev()+ablelen);
				buf.getBytes(0, msgbuf.getDatabuf());
				msgbuf.setDatarev(msgbuf.getDatarev() + ablelen);
				msgbuf.getDatabuf().limit(oldlimit);

			} else {

				buf.getBytes(0, msgbuf.getDatabuf());
				// 读完了数据,处理了
				readFinished(ctx, msgbuf.getDatabuf());
				msgbuf.reSet();

				if (leftlen < ablelen) {
					prepareRead(ctx, buf, leftlen);
				}
			}
		} else {
			prepareRead(ctx, buf, 0);
		}

		buf.clear();
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		@SuppressWarnings("deprecation")
		Session s = ctx.attr(sessionattr).get();
		System.out.println(s.getId());
		super.channelReadComplete(ctx);
	}
}
