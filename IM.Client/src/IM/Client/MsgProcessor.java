package IM.Client;

import java.nio.ByteBuffer;

import IM.Contract.MsgType;
import io.netty.channel.ChannelHandlerContext;

public class MsgProcessor implements Runnable{
    private MsgType _msgtype;
    private ByteBuffer _msgdata;
    private ChannelHandlerContext _context;
	
    public MsgProcessor(MsgType msgtype,ByteBuffer msgdata,ChannelHandlerContext context)
    {
	   this._msgtype=msgtype;
	   this._msgdata=msgdata;
	   this._context=context;
    }

	public void run() {
		// TODO Auto-generated method stub
		
	}
}
