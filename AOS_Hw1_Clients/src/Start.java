import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;


public class Start {

	/**
	 * @param args
	 */
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		InputParameters ip = new InputParameters(args[0]);
		int portNo = ip.PortNo;
		Helper h = new Helper();		
		List<Info> ServerPorts = ip.Servers; 
		Client[] clients = new Client[10];
		
		
		try
		{
			ServerSocket Server = new ServerSocket(portNo);
			h.Print("Server socket created on port -"+portNo);
			clients[0] = new Client(ServerPorts,clients,ip.Interval);
			clients[0].start();
			
			int ctr = 1;
			if(ip.Clients!=null)
			{
				ctr = ctr + ip.Clients.size();
				for(int j = 0;j<ip.Clients.size();j++)
				{
					Info temp = ip.Clients.get(j);
					h.Print("Establishing Sockets for command line arguments");
					InetAddress addr = temp.addr;
					int port = temp.portNo;
					Socket Sock = new Socket(addr,port);
					h.Print("create a socket with existing port on -"+port);
					for(int i=1;i<5;i++)
					{
						if(clients[i]==null)
						{
							clients[i] = new Client(Sock,clients);
							h.Print("Server creates a new client from cmd line argument");
							clients[i].start();
							break;
						}
					}
					
				}
			}
			
			while(true)
			{
				
				h.Print(".");
				Socket ClientSocket = Server.accept();
				
				if(ClientSocket.getPort()>51554 && ClientSocket.getPort()<51655)
				{
					System.out.println("ClientSocket.getLocalPort() -"+ClientSocket.getPort());
					
					for(int j=0;j<9;j++)
					{
						if(clients[j]!=null)
						{
							//clients[j].currSock.close();
							clients[j].stop();
						}
					}
					Server.close();
					break;

				}
				else
				{
					h.Print("Client Socket created" + ctr);
					clients[ctr] = new Client(ClientSocket,clients);
					h.Print("Client Thread Started");
					clients[ctr].start();
					ctr++;
				}
				//if(Breaker == true)
					//Thread.currentThread().stop();
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}

}
