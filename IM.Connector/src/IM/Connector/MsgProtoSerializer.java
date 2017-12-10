package IM.Connector;

import java.nio.ByteBuffer;

import IM.Connector.Exception.MsgSerializerException;
import IM.Contract.MsgType;
import IM.Contract.StringProto.SString;
import io.netty.buffer.ByteBuf;

public class MsgProtoSerializer implements IMsgSerializer {

	public Object readObject(MsgType msgtype, ByteBuffer buffer) throws MsgSerializerException {
		// TODO Auto-generated method stub
		try {
			switch (msgtype) {
			case ECHO: {
				return SString.parseFrom(buffer).getStr();
			}
			case ResponeCode: {

			}
			default:
				break;
			}
			return null;
		} catch (Exception ex) {
			throw new MsgSerializerException(String.format("[MsgProtoSerializer.MsgProtoSerializer] error",msgtype), ex);
		}
	}

	public byte[] writeObject(MsgType msgtype, Object obj) throws MsgSerializerException {
		// TODO Auto-generated method stub
		switch (msgtype) {
		case ECHO: {
			if (obj == null) {
				return null;
			}

			String s = (String) obj;
			if (s.length() == 0) {
				return null;
			}
			return SString.newBuilder().setStr(s).build().toByteArray();
		}
		default:
			break;
		}
		return null;
	}
}
