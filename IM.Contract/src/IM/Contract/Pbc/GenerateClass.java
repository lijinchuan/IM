package IM.Contract.Pbc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class GenerateClass {
	
	private static final String DEFAULT_ENCODING = "utf-8";// 编码

	static String ReadInput(InputStream input) {
		// 字节数组
		byte[] bcache = new byte[2048];
		int readSize = 0;// 每次读取的字节长�?
		ByteArrayOutputStream infoStream = new ByteArrayOutputStream();
		try {
			// �?次�?�读�?2048字节
			while ((readSize = input.read(bcache)) > 0) {
				// 将bcache中读取的input数据写入infoStream
				infoStream.write(bcache, 0, readSize);
			}
		} catch (IOException e1) {
			return "输入流读取异�?";
		}

		try {
			return infoStream.toString(DEFAULT_ENCODING);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			return e.getMessage();
		}

	}
	
   public static void main(String[] args) throws InterruptedException {
	   String protoFile = "msg.proto";//  
       String cmd = "C:\\Program Files\\protoc-3.4.0-win32\\bin\\protoc.exe -I=E:\\Work\\learn\\Git\\IM\\IM.Connector\\IM.Contract\\src\\IM\\Contract\\Pbc\\ --java_out=E:\\Work\\learn\\Git\\IM\\IM.Connector\\IM.Contract\\src\\ E:\\Work\\learn\\Git\\IM\\IM.Connector\\IM.Contract\\src\\IM\\Contract\\Pbc\\"+ protoFile;  
       try {
    	   Process process = Runtime.getRuntime().exec(cmd);
   		InputStream stdInput = process.getInputStream();
   		InputStream errInput = process.getErrorStream();

   		process.waitFor();

   		String stdinfo = ReadInput(stdInput);
   		String errinfo = ReadInput(errInput);
   		
   		System.out.println(stdinfo);
   		System.out.println(errinfo);
   		
       } catch (IOException e) {
           e.printStackTrace();
       }//通过执行cmd命令调用protoc.exe程序 
   }
}
