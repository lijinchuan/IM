package IM.Connector;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import IM.Contract.AppMsgProto;
import IM.Contract.StringProto.SString;
import io.netty.buffer.ByteBuf;

public class MsgProtoSerializer implements IMsgSerializer {

	public Object ReadObject(int msgtype, ByteBuffer buffer) throws Exception {
		// TODO Auto-generated method stub
		switch (msgtype) {
		case 0: {
			return SString.parseFrom(buffer).getStr();
		}
		}
		return null;
	}

	public ByteBuf WriteObject(int msgtype, Object obj, ByteBuf buffer) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
