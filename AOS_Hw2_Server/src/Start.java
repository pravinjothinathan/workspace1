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
		ServerSocket SSServer = null;
		ServerSocket SSClient = null;
		
		h.Print(IPs);
		
		//Establishing Server Socket
		try {
			SSServer = new ServerSocket(IPs.ServerPortNo);
			Server s;
			if(IPs.Servers==null)
				s = new Server(lstServers,lstClients,IPs.tail,IPs.ServerId,0,IPs.pathFolder);
			else
				s = new Server(lstServers,lstClients,IPs.tail,IPs.ServerId,IPs.Servers.size(),IPs.pathFolder);
			s.start();
			SSClient = new ServerSocket(IPs.ClientPortNo);
		} catch (IOException e) {
			System.out.println("Error in Server Socket Creation");
			e.printStackTrace();
		}
		
		if(IPs.Servers!=null)
		{
			for (Info serverAddress : IPs.Servers) {
				//Servers s = new Server(serverAddress.addr,serverAddress.portNo);
				try {
					Socket tempsock = new Socket(serverAddress.addr,serverAddress.portNo);
					System.out.println("Connection Estblshed with Serverport - "+tempsock.getPort());
					Server tempserv = new Server(tempsock);
					tempserv.start();
					lstServers.add(tempserv);
				} catch (IOException e) {
					System.out.println("Error in existing server socket creation");
					e.printStackTrace();
				}
			}
		}
		
		ClientHandler clientListener = new ClientHandler(lstClients, SSClient);
		clientListener.start();
		
		//Adding New Connections as and when a connection is requested from other Servers
		while(SSServer!=null)
		{
			try{
			Socket Connection = SSServer.accept();
			System.out.println("Connection accepted from Server Port - "+Connection.getPort() );
			Server s = new Server(Connection);
			s.start();
			lstServers.add(s);
			//Connection.close();
			}
			catch(Exception e)
			{
				System.out.println("Error in Connection Establishment");
				e.printStackTrace();
			}
		}
		
	}

}
