package IM.Connector;

import java.nio.ByteBuffer;
import java.util.Date;

import io.netty.util.AttributeKey;

public class Session {
	public final static AttributeKey<String> UID = AttributeKey.newInstance("uid"); //uid
	public final static AttributeKey<Date> CONNTIME = AttributeKey.newInstance("Date"); //连接时间
	public final static AttributeKey<Date> LOGINTIME = AttributeKey.newInstance("logintime"); 
	public final static AttributeKey<Date> LASTHEARTBEATTIME=AttributeKey.newInstance("heartbeattime");
	
}
