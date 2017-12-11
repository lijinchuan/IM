package IM.Client;

import java.nio.ByteBuffer;

import io.netty.channel.ChannelHandlerContext;

public class MsgProcessor implements Runnable{
    private int _msgtype;
    private ByteBuffer _msgdata;
    private ChannelHandlerContext _context;
	
    public MsgProcessor(int msgtype,ByteBuffer msgdata,ChannelHandlerContext context)
    {
	   this._msgtype=msgtype;
	   this._msgdata=msgdata;
	   this._context=context;
    }

	public void run() {
		// TODO Auto-generated method stub
		
	}
}
