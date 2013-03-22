package sample;

import java.io.DataInputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;

public class SnapShotServer {

	/**
	 * @param args
	 */
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String fileName = "config.txt";
		try
		{
		ArrayList<MyServer> serverList = ParseFiles.parseFileForClient(fileName);
		//for (int i = 0; i < 1; i++) {
		for (int i = 0; i < serverList.size(); i++) {
			MyServer test = serverList.get(i);
			System.out.println("ip:"+test.ServInfo.addr+"port"+test.ServInfo.portNo);
			SSServThread t = new SSServThread(test);
			t.start();
		}
		}catch(Exception e)
		{
			e.printStackTrace();
			//System.out.println("Exception in File Parse");
		}
	}
}
