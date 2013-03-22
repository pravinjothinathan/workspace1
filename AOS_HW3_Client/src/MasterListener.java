import java.io.DataInputStream;
import java.io.PrintStream;
import java.net.Socket;


public class MasterListener extends Thread{
	
	DataInputStream disDataIn ;
	PrintStream psDataOut;
	int ServerId;
	int FailServer;
	
	public MasterListener(Socket s,int serverId, int failServer) throws Exception
	{
		disDataIn = new DataInputStream(s.getInputStream());
		psDataOut = new PrintStream(s.getOutputStream());
		ServerId = serverId;
		FailServer = failServer;
	}
	
	@SuppressWarnings("deprecation")
	public void run()
	{
		psDataOut.println("client:"+ServerId);
		try {
			System.out.println("Master Listener Started !!!");
			String line ="";
			while(true)
			{
				line = disDataIn.readLine();
				if(line!=null)
				{
					if(line.contains("crash"))
					{
						System.out.println("From master - > " + line);
						int serverid = Integer.parseInt(line.split(":")[1]);
						FailServer = serverid;
						System.out.println("###############");
						//FailServers.add(serverid);
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Exception caught !!! Run in master Listener!!!");
		}
	}
}
