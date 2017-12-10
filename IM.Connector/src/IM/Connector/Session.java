package IM.Connector;

import java.nio.ByteBuffer;
import java.util.Date;

public class Session {
	private String _id; //标识
	private boolean _islogin=false; //是否登录
	private Date _conntime; //连接时间
	private Date _logintime; //登录时间
	private Date _lastHeartBeat; //上次心跳包时间
	private Date _lastSessionTime; //上次会话时间
	
	public String getId() {
		return this._id;
	}

	public void setId(String value) {
		this._id = value;
	}
	
	public boolean getIsLogin()
	{
		return this._islogin;
	}
	
	public void setIsLogin(boolean value) {
		this._islogin=value;
	}
	
	public Date getConntime()
	{
		return this._conntime;
	}
	
	public void setConntime(Date value) {
		this._conntime=value;
	}
	
	public Date getLogintime()
	{
		return this._logintime;
	}
	
	public void setLogintime(Date value) {
		this._logintime=value;
	}
	
	public Date getLastHeartBeat()
	{
		return this._lastHeartBeat;
	}
	
	public void setLastHeartBeat(Date value) {
		this._lastHeartBeat=value;
	}
	
	public Date getLastSessionTime()
	{
		return this._lastSessionTime;
	}
	
	public void setLastSessionTime(Date value) {
		this._lastSessionTime=value;
	}
	
}
