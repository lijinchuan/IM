package IM.Connector;

import java.nio.ByteBuffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public class MsgBuffer {
	private int _datalen;
	private int _datarev;
	private ByteBuf _lenbuf;
	private ByteBuffer _databuf;
	
	public MsgBuffer()
	{
		_lenbuf=ByteBufAllocator.DEFAULT.buffer(4);
	}

	public int getDatalen() {
		return this._datalen;
	}

	public void setDatalen(int value) {
		this._datalen = value;
	}

	public int getDatarev() {
		return this._datarev;
	}

	public void setDatarev(int value) {
		this._datarev = value;
	}

	public ByteBuffer getDatabuf() {
		return this._databuf;
	}

	public void setDatabuf(ByteBuffer value) {
		this._databuf = value;
	}
	
	public ByteBuf getLenBuf()
	{
		return this._lenbuf;
	}
	
	public void reSet()
	{
		this._datalen=0;
		this._datarev=0;
		this._databuf=null;
		this._lenbuf.clear();
	}
}
