import java.net.ServerSocket;
import java.util.List;


public class Server extends Thread{

	ServerSocket sock;
	List<Client> alClients;
	public Server(ServerSocket ssServer,List<Client> clients) {
		// TODO Auto-generated constructor stub
		sock = ssServer;
		alClients = clients;
	}
	
	public void run()
	{
		while(true)
		{
			String line="";
			for (Client tempClient : alClients) {
				line = tempClient.ReadLine();
				if(line!=null)
				{
					//do something
				}
			}
		}
	}

}
