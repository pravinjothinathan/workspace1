import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 
 */

/**
 * @author pravinjothinathan
 * Starting point of my Server
 */
public class Start {

	/**
	 * @param args
	 */
	static Helper h = new Helper();
	public static void main(String[] args) {
		InputParameters IPs = new InputParameters(args[0]);
		List<Server> lstServers = Collections.synchronizedList(new ArrayList<Server>());
		List<Client> lstClients = Collections.synchronizedList(new ArrayList<Client>());
		List<Message> MessageInQueue = Collections.synchronizedList(new ArrayList<Message>());
		ServerSocket SSServer = null;
		ServerSocket SSClient = null;
		Socket sockMast;
		try {
			//Establishing connection with master server!!
			Info infoMaster = IPs.infoMasterServer;
			sockMast = new Socket(infoMaster.addr,infoMaster.portNo);
			MasterListener Listener = new MasterListener(sockMast,IPs.ServerId);
			Listener.start();
		} catch (Exception e) {
			System.out.println("Exception in Master Server !!!");
		}
		
		//Establishing Server Socket
		try {
			SSServer = new ServerSocket(IPs.ServerPortNo);
			Server s;
			if(IPs.Servers==null)
				s = new Server(lstServers,lstClients,IPs.tail,IPs.ServerId,0,IPs.pathFolder,MessageInQueue);
			else
				s = new Server(lstServers,lstClients,IPs.tail,IPs.ServerId,IPs.Servers.size(),IPs.pathFolder,MessageInQueue);
			//lstServers.add(s);
			s.start();
			SSClient = new ServerSocket(IPs.ClientPortNo);
		} catch (IOException e) {
			System.out.println("Error in Server Socket Creation");
			e.printStackTrace();
		}
		
		if(IPs.Servers!=null)
		{
			for (Info serverAddress : IPs.Servers) {
				try {
					Socket tempsock = new Socket(serverAddress.addr,serverAddress.portNo);
					Server tempserv = new Server(tempsock);
					tempserv.start();
					lstServers.add(tempserv);
				} catch (IOException e) {
					System.out.println("Error in existing server socket creation");
					e.printStackTrace();
				}
			}
		}
		
		ClientHandler clientListener = new ClientHandler(lstClients, SSClient,MessageInQueue);
		clientListener.start();
		
		//Adding New Connections as and when a connection is requested from other Servers
		while(SSServer!=null)
		{
			try{
			Socket Connection = SSServer.accept();
			Server s = new Server(Connection);
			s.start();
			lstServers.add(s);
			}
			catch(Exception e)
			{
				System.out.println("Error in Connection Establishment");
				e.printStackTrace();
			}
		}
		
	}

}
