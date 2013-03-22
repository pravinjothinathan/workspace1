import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class StartServer extends Thread{

	List<Client> alClients;
	int serverPort;
	
	/**
	 * @param args
	 */
	public StartServer()
	{
		
		
	}
	 
	public void run() {
		// TODO Auto-generated method stub
		
		int ssPort = Integer.parseInt(args[0]);
		
		try {
			ServerSocket ssServer = new ServerSocket(ssPort);
			Server Server = new Server(ssServer,alClients);
			ServerUI uiServer = new ServerUI();
			uiServer.SetClients(alClients);
			Server.start();
			while(true)
			{
				Socket tempSocket = ssServer.accept();
				Client temp = new Client(tempSocket);
				alClients.add(temp);
				uiServer.updateList();
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		

	}

}
