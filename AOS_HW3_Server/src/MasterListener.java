import java.io.DataInputStream;
import java.io.PrintStream;
import java.net.Socket;


public class MasterListener extends Thread{
	
	DataInputStream disDataIn ;
	PrintStream psDataOut;
	int ServerId;
	
	public MasterListener(Socket s,int serverId) throws Exception
	{
		disDataIn = new DataInputStream(s.getInputStream());
		psDataOut = new PrintStream(s.getOutputStream());
		ServerId = serverId;
	}
	
	
	
	@SuppressWarnings("deprecation")
	public void run()
	{
		psDataOut.println("server:"+ServerId);
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
						Crash.intServer = Integer.parseInt(line.split(":")[1]);
						Crash.Crash = true;
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Exception caught !!! Run in master Listener!!!");
		}
	}
}
