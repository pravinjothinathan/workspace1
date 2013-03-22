import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;


public class ClientHandler extends Thread{
	
	List<Client> Clients;
	ServerSocket sock;
	
	public ClientHandler(List<Client> lstClients,ServerSocket SSClient)
	{
		Clients = lstClients;
		sock = SSClient; 
	}
	
	public void run()
	{
		while(sock!=null)
		{
			try {
				Socket Connection = sock.accept();
				Client client = new Client(Connection);
				Clients.add(client);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Error in Client Socket Establishment");
				e.printStackTrace();
			}
		}
	}
	
	

}
