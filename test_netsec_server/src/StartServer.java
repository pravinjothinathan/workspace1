import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class StartServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int portNo = Integer.parseInt(args[0]);
		ServerSocket Server=null;
		List<Clients> clients = new ArrayList<Clients>();
		
		try {
			Server = new ServerSocket(portNo);
			System.out.println("Server Port Established");
			Server s = new Server(clients);
			s.start();
			System.out.println("Server Started");
		} catch (Exception e) {
			System.out.println("Exception is Server Socket Creation!!!");
			// TODO: handle exception
		}
		
		
		while(Server!=null)
		{
			try {
				Socket temp = Server.accept();
				System.out.println("Client Request in and accepted");
				Clients tclient = new Clients(temp);
				tclient.start();
				clients.add(tclient);
				System.out.println("client Server Started");
			} catch (Exception e) {
				System.out.println("Exception in connection Accept");
			}
			
		}

	}

}
