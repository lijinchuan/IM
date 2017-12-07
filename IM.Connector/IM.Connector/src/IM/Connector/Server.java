package IM.Connector;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Server {
	private final static int MAXCLIENTNUM	=1000000;
	private int _port;
	private ServerBootstrap _boot;
	private EventLoopGroup _bossGroup;
	private EventLoopGroup _workerGroup;
	private ChannelFuture _channelFuture;
	private volatile boolean _server_started = false;

	public Server(int port) {
		this._port = port;
	}

	public boolean startServer() {

		synchronized (this) {
			if (this._server_started) {
				return true;
			}

			System.out.println("start server...");
			_bossGroup = new NioEventLoopGroup();
			_workerGroup = new NioEventLoopGroup();
			_boot = new ServerBootstrap();
			
			_boot.group(_bossGroup, _workerGroup);
			_boot.channel(NioServerSocketChannel.class);
			_boot.childOption(ChannelOption.TCP_NODELAY, true);
			_boot.childOption(ChannelOption.SO_KEEPALIVE, true);
			_boot.option(ChannelOption.SO_BACKLOG, MAXCLIENTNUM);

			try {
				_boot.childHandler(new ServerInitializer());
				//开始干活
				_channelFuture = _boot.bind(_port).sync();
				_server_started = true;

				return true;
			} catch (Exception ex) {
				return false;
			}
		}

	}
	
	public void startServerDeam() throws Exception {

		synchronized (this) {
			if (this._server_started) {
				throw new Exception("server is staring...");
			}

			System.out.println("start server...");
			_bossGroup = new NioEventLoopGroup();
			_workerGroup = new NioEventLoopGroup();
			_boot = new ServerBootstrap();
			
			_boot.group(_bossGroup, _workerGroup);
			_boot.channel(NioServerSocketChannel.class);
			_boot.childOption(ChannelOption.TCP_NODELAY, true);
			_boot.childOption(ChannelOption.SO_KEEPALIVE, true);
			_boot.option(ChannelOption.SO_BACKLOG, MAXCLIENTNUM);

			//开始干活
			_boot.childHandler(new ServerInitializer());
		     _channelFuture = _boot.bind(_port).sync();
		     
			_server_started = true;
		}
		
		System.out.println("server start now");
		_channelFuture.channel().closeFuture().sync();
		System.out.println("server exit");

	}

	public void closeServer() {
		synchronized (this) {
            if(!_server_started) {
            	return;
            }
			
            _channelFuture.channel().close();
			_bossGroup.shutdownGracefully();
			_workerGroup.shutdownGracefully();
			
			_server_started = false;
		}
	}
}
