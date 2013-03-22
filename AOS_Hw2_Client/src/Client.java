import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;


public class Client {

	static Semaphore ssn = new Semaphore(1);
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		InputParameters IPs = new InputParameters(args[0]);
		List<Server> lstServers= new ArrayList<Server>();
		List<String> FileList = Collections.synchronizedList(new ArrayList<String>());
		
		for (int i = 0; i < IPs.FileCount; i++) {
			FileList.add(String.format("%d", i));
		}
		
		for (Info server : IPs.Servers) {
			try {
				Socket connection = new Socket(server.addr,server.portNo);
				Server tempserver = new Server(connection,FileList,ssn);
				tempserver.start();
				lstServers.add(tempserver);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Error in opening a socket to server");
				e.printStackTrace();
			}
		}
		
		//int countWrite =0;
		//for(int i=0;i<1000;i++)
		while(true)
		{
			//System.out.println("i ->"+i++);
			int rndServer = (int)(Math.random()*IPs.Servers.size()-1);
			int rndOperation = (Math.random()>0.5)?0:1;
			int rndFile = (int)(Math.random()*IPs.FileCount);
			
			//if(rndOperation==1)
				//countWrite++;
			//fWriteLock.lock();
			if(FileList.contains(String.format("%d", rndFile)))
			{
				System.out.println("rserv "+rndServer+" rOper "+rndOperation+" rFle "+rndFile);
				try {
					ssn.acquire();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				FileList.remove(String.format("%d", rndFile));
				ssn.release();
				Message m = new Message(rndOperation,rndFile,IPs.ClientId);
				lstServers.get(rndServer).SendRequest(m);
				System.out.println("Request Sent");
				
				try {
					Thread.sleep(IPs.TimeInt);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			///fWriteLock.unlock();	
			
		}
		//System.out.println("count Write ->"+ countWrite);
		
	}

}
