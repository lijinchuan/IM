package IM.Client.NettyClient;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import IM.Client.MsgProtoSerializer;
import IM.Contract.StringProto.SString;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClients {
	EventLoopGroup group;
	List<Channel> channelFutures;
	Bootstrap bs;
	private final int _serverport;
	private final String _serverhost;
	private boolean _init = false;

	MsgProtoSerializer _msgSerializer;

	public NettyClients(String serverhost, int serverport) {
		this._serverport = serverport;
		this._serverhost = serverhost;

		_msgSerializer = new MsgProtoSerializer();
	}

	public void startNew() throws Exception {
		if (!_init) {
			synchronized (this) {
				if (!_init) {
					group = new NioEventLoopGroup();
					bs = new Bootstrap();

					bs.group(group).channel(NioSocketChannel.class).option(ChannelOption.SO_KEEPALIVE, true)
							.option(ChannelOption.TCP_NODELAY, true)
							.remoteAddress(new InetSocketAddress(this._serverhost, this._serverport))// 绑定监听端口
							.handler(new ClientInitializer(_msgSerializer, this));

					channelFutures = new ArrayList<Channel>();
					_init = true;
				}
			}
		}

		bs.connect().addListener(new ChannelFutureListener() {

			public void operationComplete(ChannelFuture future) throws Exception {
				// TODO Auto-generated method stub
				if (future.isSuccess()) {
					channelFutures.add(future.channel());
				}
			}

		}).sync();

	}

	public void channelReconnect(Channel channel) throws Exception {
		if (_init) {
			if (channelFutures.remove(channel)) {
				this.startNew();
				
				System.out.println("断线重连");
			}
		}
	}

	public void SendEcho(String str) throws IOException {
		SString sstring = SString.newBuilder().setStr(str).build();

		byte[] bytes = sstring.toByteArray();
		int bytelen = bytes.length + 2;
		ByteBuffer ba = ByteBuffer.allocate(6 + bytes.length);
		ba.putInt(bytelen);
		ba.putShort((short) 0);
		ba.put(bytes);
		ba.flip();
		channelFutures.get(0).writeAndFlush(Unpooled.copiedBuffer(ba));
	}

	public void close() throws InterruptedException {
		try {
			for (Channel channel : channelFutures) {
				try {
					channel.close();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		} finally {
			if (group != null) {
				group.shutdownGracefully().sync(); // 释放线程池资源
			}
		}
	}
}
