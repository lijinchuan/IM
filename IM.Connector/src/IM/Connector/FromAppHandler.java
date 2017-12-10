package IM.Connector;

import java.nio.ByteBuffer;
import java.util.Date;
import java.util.Random;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;

public class FromAppHandler extends ChannelInboundHandlerAdapter {
	// 这个键保存用户会话信息
	final static AttributeKey<Session> sessionattr = AttributeKey.newInstance("session");
	// final static AttributeKey<MsgBuffer>
	// msgbufferattr=AttributeKey.newInstance("msgbuf");

	final static int MAXBAGLEN = 1024 * 1024; // 最大允许包长
	final static Exception READLENERROREXCEPTION = new Exception("read data len error");
	final static Exception DATALENETOOLONGEXCEPTION = new Exception("data lenth too long error");

	private MsgBuffer msgbuf = new MsgBuffer();

	@Override
	public void exceptionCaught(ChannelHandlerContext context, Throwable cause) throws Exception {
		// TODO Auto-generated method stub
		// super.exceptionCaught(ctx, cause);
		//cause.printStackTrace();
		 System.out.println(cause.toString());
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
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
		ByteBuffer msgbufdata = msgbuf.getDatabuf();
		int ablelen = directByteBuf.readableBytes();
		
		System.out.println("prepareRead->ablelen:"+ablelen);

		if (msgbuf.getLenBuf().readerIndex() == 0) {
			
			System.out.println("包长缓冲区数据为空,需要获取长度");
			
			if (ablelen - offset < 4 && ablelen < directByteBuf.capacity()) {
				
				System.out.println(String.format("缓冲长度%d不足4且缓冲区未写满,报错退出",ablelen - offset));
				
				exceptionCaught(ctx, READLENERROREXCEPTION);
				this.closeClient(ctx, ServerError.readLenError.getErrorCode());
				return;
			} else if (ablelen - offset < 4 && ablelen == directByteBuf.capacity()) {
				System.out.println(String.format("缓冲长度%d不足4但缓冲区写满,等待后面数据",ablelen - offset));
				
				directByteBuf.markReaderIndex();
				directByteBuf.readerIndex(offset).getBytes(offset, msgbuf.getLenBuf(),ablelen-offset);
				msgbuf.getLenBuf().readerIndex(ablelen-offset);
				directByteBuf.resetReaderIndex();
				return;
			}

			int datalen = msgbuf.getDatalen();
			if (datalen == 0) {
				directByteBuf.markWriterIndex();
				datalen = directByteBuf.readerIndex(offset).readInt();
				directByteBuf.resetReaderIndex();
				offset += 4;
				
				System.out.println("datalen长度为0,从缓冲区直接读取长度:" +datalen );
			}

			if (datalen < 0 || datalen > MAXBAGLEN) {
				
				System.out.println("datalen长度超出最大包限制:" +datalen );
				
				exceptionCaught(ctx, DATALENETOOLONGEXCEPTION);
				this.closeClient(ctx, ServerError.readLenError.getErrorCode());
				return;
			}

			ByteBuffer newdatabuf = ByteBuffer.allocate(datalen);

			System.out.println(String.format("datalen:%d,ablelen:%d,offset:%d",datalen,ablelen,offset));
			if (datalen <= ablelen - offset) {

				directByteBuf.markReaderIndex();
				directByteBuf.readerIndex(offset);
				directByteBuf.getBytes(offset, newdatabuf);
				directByteBuf.resetReaderIndex();

				System.out.println("数据长度小于缓冲区可用数据长度,一次性获取完消息");
				msgbuf.reSet();
				readFinished(ctx, newdatabuf);

				if (datalen < ablelen - offset) {
					System.out.println("还有数据,继续读取");
					
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
				
				System.out.println("io缓冲区数据不足,一次性写入消息缓冲,写入长度:"+newdatabuf.position()+",预写长度:"+(ablelen-offset));
			}
		} else {
		
			int readlenbytelen = 4 - msgbuf.getLenBuf().readerIndex();
			
			System.out.println("消息长度缓冲区上次未写完,需要从io缓存区提取:"+readlenbytelen);

			if (readlenbytelen <= 0) {
				exceptionCaught(ctx, DATALENETOOLONGEXCEPTION);
				this.closeClient(ctx, ServerError.readLenError.getErrorCode());
				return;
			}
			directByteBuf.markReaderIndex();
			directByteBuf.readerIndex(offset).getBytes(offset, msgbuf.getLenBuf(), readlenbytelen);
			msgbuf.getLenBuf().readerIndex(msgbuf.getLenBuf().readerIndex()+readlenbytelen);
			directByteBuf.resetReaderIndex();

			int datalen = msgbuf.getLenBuf().getInt(0);
			System.out.println("读取到了包长:"+datalen);

			if (datalen < 0 || datalen > MAXBAGLEN) {
				this.closeClient(ctx, ServerError.dataTooLenError.getErrorCode());
				exceptionCaught(ctx, DATALENETOOLONGEXCEPTION);
				return;
			}

			offset += readlenbytelen;

			// msgbuf.reSet();
			msgbuf.setDatalen(datalen);
			msgbuf.setDatarev(0);
			msgbuf.getLenBuf().clear();
			ByteBuffer buffer = ByteBuffer.allocate(datalen);
			buffer.clear();
			msgbuf.setDatabuf(buffer);
			if (ablelen > offset) {
				System.out.println("读取到了包长,还有数据:"+(ablelen-offset));
				prepareRead(ctx, directByteBuf, offset);
			}
		}
	}

	private void readFinished(ChannelHandlerContext ctx, ByteBuffer data) throws Exception {
		data.flip();
		System.out.println(new String(data.array(), "utf-8"));

		channelReadComplete(ctx);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// TODO Auto-generated method stub
		ByteBuf buf = (ByteBuf) msg;
		System.out.println("有新的数据,长度:" + buf.readableBytes());

		if (msgbuf.getDatalen() > 0) {
			int ablelen = buf.readableBytes();

			int leftlen = msgbuf.getDatalen() - msgbuf.getDatarev();
			System.out.println("接着上次预读取:" + leftlen + "长度数据");

			if (leftlen > ablelen) {

				System.out.println("数据长度小于所要的长度,全部写入");

				int oldlimit=msgbuf.getDatabuf().limit();
				msgbuf.getDatabuf().limit(msgbuf.getDatarev()+ablelen);
				buf.getBytes(0, msgbuf.getDatabuf());
				msgbuf.setDatarev(msgbuf.getDatarev() + ablelen);
				msgbuf.getDatabuf().limit(oldlimit);
			} else {

				buf.getBytes(0, msgbuf.getDatabuf());

				System.out.println("预取长度小于缓冲长度,获取全部包,分发消息");

				// 读完了数据,处理了
				readFinished(ctx, msgbuf.getDatabuf());
				msgbuf.reSet();

				if (leftlen < ablelen) {

					System.out.println(String.format("leftlen长%d,缓冲长度%d,还有数据,接着读下一个包", leftlen, ablelen));
					prepareRead(ctx, buf, leftlen);
				} else {
					System.out.println(String.format("leftlen长%d,缓冲长度%d,没有数据了", leftlen, ablelen));
				}

			}
		} else {
			System.out.println("msgbuf没有指定长度,按新包处理");
			prepareRead(ctx, buf, 0);
		}
		
		buf.clear();
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelReadComplete(ctx);
	}
}
