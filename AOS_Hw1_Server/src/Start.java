import java.net.ServerSocket;
import java.net.Socket;


public class Start {

	/**
	 * @param args
	 */
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int portNo = Integer.parseInt(args[0]);
		String FolderPath =args[1];
		
		Helper h = new Helper();
		Server[] clients = new Server[5];
		try
		{
			ServerSocket Server = new ServerSocket(portNo);
			
			for(int i=0;i<5;i++)
			{
				h.Print(".");
				Socket sock = Server.accept();
				
				System.out.println("ClientSocket.getLocalPort() -"+sock.getLocalPort());
				if(sock.getPort()>51554 && sock.getPort()<51655)
				{
					System.out.println("ClientSocket.getLocalPort() -"+sock.getLocalPort());
					for(int j=0;j<5;j++)
					{
						if(clients[j]!=null)
						{
							//clients[j].currSock.close();
							clients[j].stop();
						}
					}
					sock.close();
					break;
				}
				else
				{
					if(clients[i]==null)
					{
						clients[i] = new Server(sock,clients,FolderPath);
						h.Print("Connetion Accepted :"+sock.getInetAddress());
						clients[i].start();
					}
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
