import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.List;
//import java.util.concurrent.locks.Lock;
//import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.Semaphore;


public class Server extends Thread{
	
	DataInputStream disDataIn;
	PrintStream ps;
	List<String> FileList;
	static Semaphore ss;
	
	public Server(Socket sock, List<String> fileList2, Semaphore ssn)
	{
		try {
			disDataIn = new DataInputStream(sock.getInputStream());
			ps = new PrintStream(sock.getOutputStream());
			FileList = fileList2;
			ss = ssn;
			System.out.println("Stream creation with server succesful :)");
		} catch (IOException e) {
			System.out.println("Error in creating input and output streams for server communication");
			e.printStackTrace();
		}
	}
	
	public void run()
	{
		while(true)
		{
			try {
				@SuppressWarnings("deprecation")
				String line = disDataIn.readLine();
				if(line.contains("ACK"))
				{
					try {
						ss.acquire();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("returns -> " + line);
					//System.out.println("adding -> "+line.split(":")[1]);
					///FileList.add(line.split(":")[1]);
					ss.release();
				}
				else if(line.contains("read"))
					System.out.println("returns -> " + line);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void SendRequest(Message m) {
		ps.println(m.StrMessage());
	}

}
