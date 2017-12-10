package IM.Client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.Buffer;
import java.nio.ByteBuffer;

import IM.Contract.StringProto.SString;

public class Client {
	private String _serverIp;
	private int _serverPort;
	protected Socket socketClient;
	
	private boolean stop=false;
	
	private boolean IsConnected() {
		return this.socketClient != null && this.socketClient.isConnected() && !this.socketClient.isClosed();
	}
	
	public Client(String serverip,int serverport) {
		this._serverIp=serverip;
		this._serverPort=serverport;
	}
	
	public Boolean connect() {
		try {
			if (this.IsConnected())
				return true;


			try {
				socketClient = new Socket(this._serverIp, this._serverPort);
				socketClient.setReceiveBufferSize(32000);
				socketClient.setSendBufferSize(32000);
				socketClient.setTcpNoDelay(true);

			} catch (UnknownHostException e) {
				Exception ne = new Exception(
						String.format("连接到远程服务器%s失败，端口:%d，原因:%s", _serverIp, _serverPort, e.getMessage()));
				throw ne;

			} catch (IOException e) {
				e.printStackTrace();
				throw e;
			}

			return true;
		} catch (Exception e) {
			// OnError(e);
			return false;
		}
	}
	
	public void SendEcho(String str) throws IOException {
		SString sstring=SString.newBuilder().setStr(str).build();
		
		byte[] bytes= sstring.toByteArray();
		int bytelen= bytes.length+4;
		ByteBuffer ba=ByteBuffer.allocate(8);
		ba.putInt(bytelen);
		ba.putInt(0);
		
		synchronized (this.socketClient) {
			DataOutputStream dos = null;
			try {
				OutputStream os = socketClient.getOutputStream();
				dos = new DataOutputStream(os);
				dos.write(ba.array());
                dos.write(bytes);
				dos.flush();

			} catch (IOException ex) {
				if (dos != null) {
					dos.close();
				}
				socketClient.close();
				throw ex;
			}
		}
	}
	
	public void SendMsg(String str) throws IOException {
		byte[] bytes= str.getBytes("utf-8");
		int bytelen= bytes.length;
		ByteBuffer ba=ByteBuffer.allocate(4);
		ba.putInt(bytelen);
		
		synchronized (this.socketClient) {
			DataOutputStream dos = null;
			try {
				OutputStream os = socketClient.getOutputStream();
				dos = new DataOutputStream(os);
				dos.write(ba.array());
                dos.write(bytes);
				dos.flush();

			} catch (IOException ex) {
				if (dos != null) {
					dos.close();
				}
				socketClient.close();
				throw ex;
			}
		}
	}
	
	public void Close() throws IOException
	{
		this.socketClient.close();
	}
}
