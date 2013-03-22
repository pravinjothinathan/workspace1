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
		InputParameters IPs = new InputParameters(args[0]);
		List<Server> lstServers= new ArrayList<Server>();
		List<String> FileList = Collections.synchronizedList(new ArrayList<String>());
		//List<Integer> FailServers = Collections.synchronizedList(new ArrayList<Integer>());
		int FailServer = -1;
		try {
			//Establishing connection with master server!!
			Info infoMaster = IPs.infoMasterServer;
			Socket sockMast = new Socket(infoMaster.addr,infoMaster.portNo);
			MasterListener Listener = new MasterListener(sockMast,IPs.ClientId,FailServer);
			Listener.start();
		} catch (Exception e) {
			System.out.println("Exception in Master Server !!!");
		}
		
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
				System.out.println("Error in opening a socket to server");
				e.printStackTrace();
			}
		}
		
		while(true)
		{
			int rndServer = (int)(Math.random()*IPs.Servers.size());
			int rndOperation = (Math.random()>0.7)?1:0;
			int rndFile = (int)(Math.random()*IPs.FileCount);
			
			if(FailServer==rndServer)
				continue;
			//if(Contains(rndServer,FailServers))
				//continue;
			
			if(FileList.contains(String.format("%d", rndFile)))
			{
				if(rndOperation==1)
				{
					System.out.println("write on file -> "+ (rndFile) + "to Server ->"+rndServer);
					//FileList.remove(String.format("%d", rndFile));
				}
				else
					System.out.println("read on file -> "+ (rndFile) + "to Server ->"+rndServer);
				try {
					ssn.acquire();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				
				ssn.release();
				Message m = new Message(rndOperation,rndFile,IPs.ClientId);
				lstServers.get(rndServer).SendRequest(m);
				System.out.println("Request Sent");
				
				try {
					Thread.sleep(IPs.TimeInt);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
/*	private static boolean Contains(int rndServer, List<Integer> failServers) {
		for (Integer integer : failServers) {
			if(integer == rndServer)
				return true;
		}
		return false;
	}*/
}
