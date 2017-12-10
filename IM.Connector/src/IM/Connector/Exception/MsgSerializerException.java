package IM.Connector.Exception;

public class MsgSerializerException extends Exception {
	public MsgSerializerException(String message) {
		super(message);
	}
	
	public MsgSerializerException(String message,Throwable throwable) {
		super(message,throwable);
	}
}
