package IM.Test;

import java.io.IOException;

import IM.Client.Client;
import IM.Client.NettyClient.NettyClients;


public class TestClient {
	public static void main(String[] args) {
		try {
			//Client client = new Client("127.0.0.1", 20001);
			//client.connect();
			
			IM.Client.NettyClient.NettyClients clients=new NettyClients("127.0.0.1", 20001);
			clients.startNew();
			
			for (int i = 0; i < 100; i++) {
				//client.SendMsg("hello word:" + i);
				//client.SendEcho("hellow word:"+i);
				// Thread.sleep(1);
				
				//sb.append("hello word:"+i+",");
				
				clients.SendEcho("hello word:"+i);
			}
			Thread.sleep(100000);
			clients.close();
		} catch (Exception ex) {
           ex.printStackTrace();
		}
	}
}
