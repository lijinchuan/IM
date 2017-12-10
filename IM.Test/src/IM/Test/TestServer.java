package IM.Test;

import IM.Connector.Server;

public class TestServer {
   public static void main(String[] args) throws Exception {
	   final IM.Connector.Server server=new Server(20001);
	   
	  
	   server.startServerDeam();
	   
	   
   }
}
