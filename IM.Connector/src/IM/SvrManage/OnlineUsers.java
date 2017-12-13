package IM.SvrManage;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import IM.Util.LogManager;
import io.netty.channel.Channel;

public class OnlineUsers {
   public static ConcurrentHashMap<String, Channel> UserList=new ConcurrentHashMap<String, Channel>();
   
   private static Timer _timer;
   
   static {
	   _timer=new Timer(true);
	   _timer.schedule(new TimerTask() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			LogManager.Debug("在线用户数:"+UserList.size());
		}
		   
	   }, 1000,1000);
   }
}
