package IM.Test;

import java.io.IOException;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import IM.Client.Client;
import IM.Client.NettyClient.NettyClients;
import IM.Util.LogManager;
import io.netty.channel.Channel;

public class TestClient {
	public static void main(String[] args) throws Exception {
		ConcurrentHashMap<String, String> UserList = new ConcurrentHashMap<String, String>();

		NettyClients clients = new NettyClients("192.168.0.103", 20001);
		LogManager.Debug("����50000��������");
		Date now = new Date();
		int count = 0;
		while (count < 10000) {
			for (int i = 0; i < 1000; i++) {
				clients.startNew();
			}
			count += 1000;
			LogManager.Debug("����" + String.valueOf(count) + "�����������,��ʱ:"
					+ String.valueOf(new Date().getTime() - now.getTime()) + "ms");
		}

		now = new Date();
		LogManager.Debug("��һ��������Ϣ");
		int sendCount = 0;

		while (sendCount < 1000000) {
			for (int i = 0; i < 1000; i++) {
				clients.SendEcho("hello word:" + i);
				// client.SendError("hellow word:"+i);
				// Thread.sleep(1);
			}
			sendCount += 1000;
			Thread.sleep(5);
		}
		LogManager.Debug("����һ������Ϣ�ɹ�,��ʱ:" + String.valueOf(new Date().getTime() - now.getTime()) + "ms");

		// ������ɷ���Ϣ

		while (true) {
			try {
				clients.SendEcho("message:" + (sendCount));
				now = new Date();
				int c = new Random().nextInt(20);
				for (int i = 0; i < c; i++) {
					clients.SendEcho("new msg:" + (sendCount++));
				}
				if (c > 0) {
					LogManager.Debug(
							"������Ϣ" + c + "���ɹ�,��ʱ:" + String.valueOf(new Date().getTime() - now.getTime()) + "ms");
				}
				Thread.sleep(new Random().nextInt(10));
			} catch (Exception ex) {
				LogManager.Error(ex);
			}
		}
	}
}
