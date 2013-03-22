import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class StartMaster {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		List<Clients> servers = Collections.synchronizedList(new ArrayList<Clients>());
		try {
			int portNo = Integer.parseInt(args[0]);
			ServerSocket ssMaster = new ServerSocket(portNo);
			Master meMaster = new Master(servers);
			//meMaster.start();
			System.out.println("Master Started ...");
			Clients.meMaster = meMaster;
			while(true)
			{
				Socket tempsock = ssMaster.accept();
				System.out.println("Connection Accepted !!!");
				Clients c = new Clients(tempsock);
				c.start();
				servers.add(c);
			}
		} catch (Exception e) {
			System.out.println("Exception in main!!");
			e.printStackTrace();
		}
		
		
		
	}

}
