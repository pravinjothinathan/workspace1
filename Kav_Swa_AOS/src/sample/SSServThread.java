package sample;

import java.io.DataInputStream;
import java.io.PrintStream;
import java.net.Socket;

public class SSServThread extends Thread{

	Socket sock;
	PrintStream ps;
	DataInputStream dis;
	
	public SSServThread(MyServer test) throws Exception
	{
		sock = new Socket(test.ServInfo.addr,test.ServInfo.portNo);
		ps = new PrintStream(sock.getOutputStream());
		dis = new DataInputStream(sock.getInputStream());

	}
	
	public void run()
	{
		try{
		ps.println("snapshot");
		String line="";
		while(null != (line = dis.readLine())){
			System.out.println("snapshot acc values" + line);
			break;
		}
		ps.println("defrost");
		while(null != (line = dis.readLine())){
			System.out.println("Messages in transit" + line);
			break;
		}
		System.out.println("Snapshot Complete!");
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
