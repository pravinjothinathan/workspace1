import java.util.List;


public class Server extends Thread{
	
	List<Clients> clients;

	public Server(List<Clients> inclients)
	{
		clients = inclients;
	}
	
	public void run()
	{
		
	}
}
