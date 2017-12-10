package IM.Connector;

import java.nio.ByteBuffer;

import IM.Connector.Exception.MsgSerializerException;
import IM.Contract.MsgType;

public interface IMsgSerializer {
    public Object readObject(MsgType msgtype,ByteBuffer buffer) throws MsgSerializerException;
    public byte[] writeObject(MsgType msgtype,Object obj) throws MsgSerializerException;
}
