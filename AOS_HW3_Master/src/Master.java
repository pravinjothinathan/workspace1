import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;


public class Master extends Thread{
	
	List<Clients> servers;
	BufferedReader brReader;
	
	public Master(List<Clients> inservers) {
		servers = inservers;
		InputStreamReader isrReader = new InputStreamReader(System.in);
		brReader = new BufferedReader(isrReader);
	}
	
	public void run()
	{
		while(true)//servers.size()>3)
		{
			/*for (Clients c : servers) {
				if(c.isServer==true)
				{
					if(c.Crash==true)
					{
						SendMessages(c.id);
					}
				}
			}*/
		}
	}
	
	public void SendMessages(int serverid) {
		for(Clients client : servers) {
			String Msg = "crash:" + serverid;
			client.psDataOut.println(Msg);
		}
		System.out.println("Message send Succesful!!!");
	}

}
