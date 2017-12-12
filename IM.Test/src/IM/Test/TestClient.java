package IM.Test;

import java.io.IOException;
import java.util.Date;

import IM.Client.Client;
import IM.Client.NettyClient.NettyClients;
import IM.Util.LogManager;


public class TestClient {
	public static void main(String[] args) throws Exception {
		NettyClients clients = new NettyClients("127.0.0.1", 20001);
		LogManager.Debug("����50000��������");
		Date now = new Date();
		int count = 0;
		while (count < 50000) {
			for (int i = 0; i < 1000; i++) {
				clients.startNew();
			}
			count+=1000;
			LogManager.Debug("����"+String.valueOf(count)+"�����������,��ʱ:" + String.valueOf(new Date().getTime() - now.getTime()) + "ms");
		}
		
		now = new Date();
		LogManager.Debug("��һ��������Ϣ");
		for (int i = 0; i < 1000000; i++) {
			clients.SendEcho("hello word:" + i);
			// client.SendError("hellow word:"+i);
			// Thread.sleep(1);
		}
		LogManager
				.Debug("����һ������Ϣ�ɹ�,��ʱ:" + String.valueOf(new Date().getTime() - now.getTime()) + "ms");
		Thread.sleep(1000000);
		clients.close();
	}
}
