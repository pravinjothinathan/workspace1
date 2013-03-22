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
	//private final ReentrantReadWriteLock fLock = new ReentrantReadWriteLock();
	//private final Lock fWriteLock = fLock.writeLock();
	
	public Server(Socket sock, List<String> fileList2, Semaphore ssn)
	{
		try {
			disDataIn = new DataInputStream(sock.getInputStream());
			ps = new PrintStream(sock.getOutputStream());
			FileList = fileList2;
			ss = ssn;
			System.out.println("Stream creation with server succesful :)");
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
				//System.out.println("returns -> " + line);
				if(line.contains("ACK")||line.contains("read"))
				{
				//	fWriteLock.lock();
					try {
						ss.acquire();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("returns -> " + line);
					System.out.println("adding -> "+line.split(":")[1]);
					FileList.add(line.split(":")[1]);
					ss.release();
					//fWriteLock.unlock();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void SendRequest(Message m) {
		// TODO Auto-generated method stub
		ps.println(m.StrMessage());
	}

}
