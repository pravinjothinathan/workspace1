import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;


public class ClientHandler extends Thread{
	
	List<Client> Clients;
	ServerSocket sock;
	List<Message> MessageInQueue;
	
	public ClientHandler(List<Client> lstClients,ServerSocket SSClient, List<Message> messageInQueue)
	{
		Clients = lstClients;
		sock = SSClient; 
		MessageInQueue = messageInQueue;
	}
	
	public void run()
	{
		while(sock!=null)
		{
			try {
				Socket Connection = sock.accept();
				Client client = new Client(Connection,MessageInQueue);
				client.start();
				Clients.add(client);
			} catch (IOException e) {
				System.out.println("Error in Client Socket Establishment");
				e.printStackTrace();
			}
		}
	}
	
	

}
