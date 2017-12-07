package IM.Test;

import java.io.IOException;

import IM.Client.Client;


public class TestClient {
	public static void main(String[] args) {
		try {
			Client client = new Client("127.0.0.1", 20001);
			client.connect();
			StringBuilder sb=new StringBuilder();
			for (int i = 0; i < 20000; i++) {
				//client.SendMsg("hello word:" + i);
				//client.SendEcho("hellow word:"+i);
				// Thread.sleep(1);
				
				sb.append("hello word:"+i+",");
			}
			
			client.SendEcho(sb.toString());
			client.Close();
		} catch (Exception ex) {

		}
	}
}
