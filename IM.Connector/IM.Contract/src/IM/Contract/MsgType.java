package IM.Contract;

public enum MsgType {
	ECHO(0), //回显
	HeartBeat(1); //心跳消息
	
	private int _value;
	MsgType(int value){
		this._value=value;
	}
	
	public int getVal()
	{
		return this._value;
	}
}
