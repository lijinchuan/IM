package IM.Client;

import java.nio.ByteBuffer;

import IM.Contract.MsgType;

public interface IMsgSerializer {
    public Object readObject(MsgType msgtype,ByteBuffer buffer) throws Exception;
    public byte[] writeObject(MsgType msgtype,Object obj) throws Exception;
}
