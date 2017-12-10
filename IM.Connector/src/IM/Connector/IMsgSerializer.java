package IM.Connector;

import java.nio.ByteBuffer;

import io.netty.buffer.ByteBuf;

public interface IMsgSerializer {
    public Object ReadObject(int msgtype,ByteBuffer buffer) throws Exception;
    public ByteBuf WriteObject(int msgtype,Object obj,ByteBuf buffer) throws Exception;
}
