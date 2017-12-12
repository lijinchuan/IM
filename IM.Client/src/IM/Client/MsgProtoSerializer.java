package IM.Client;

import java.nio.ByteBuffer;

import IM.Contract.MsgType;
import IM.Contract.StringProto.SString;

public class MsgProtoSerializer implements IMsgSerializer {

	public Object readObject(MsgType msgtype, ByteBuffer buffer) throws Exception {
		// TODO Auto-generated method stub
		switch (msgtype) {
		case ECHO: {
			return SString.parseFrom(buffer).getStr();
		}
		default: {
			break;
		}
		}

		return null;
	}

	public byte[] writeObject(MsgType msgtype, Object obj) throws Exception {
		// TODO Auto-generated method stub
		switch (msgtype) {
		case HeartBeat: {
			ByteBuffer buffer = ByteBuffer.allocate(6);
			buffer.putInt(2);
			buffer.putShort((short) msgtype.getVal());
			buffer.flip();
			return buffer.array();
		}
		default:
			break;
		}
		return null;
	}

}
