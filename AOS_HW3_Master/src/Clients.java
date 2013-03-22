import java.io.DataInputStream;
import java.io.PrintStream;
import java.net.Socket;


public class Clients extends Thread{
	PrintStream psDataOut;
	DataInputStream disDataIn;
	boolean isServer = false;
	int id;
	Socket sock;
	boolean Crash = false;
	public static Master meMaster;

	public Clients(Socket s) throws Exception {
		psDataOut = new PrintStream(s.getOutputStream());
		disDataIn = new DataInputStream(s.getInputStream());
		sock =s;
	}
	
	@SuppressWarnings("deprecation")
	public void run()
	{	try {
			String line ="" ;
			line = disDataIn.readLine();
			if(line!=null)
			{
				System.out.println(line);
				if(line.contains("server"))
					isServer = true;
				id = Integer.parseInt(line.split(":")[1]);
			}
			while(isServer)
			{
				psDataOut.println("alive ?");
				if(psDataOut.checkError())
				{
					Crash = true;
					meMaster.SendMessages(id);
					break;
				}
				//System.out.println("id - crash :"+id);
				///psDataOut.println("alive ??");	
			}
			//System.out.println("");
			
		} catch (Exception e) {
			System.out.println("Exception!!");
			System.out.println("id - crash :"+id);
		}
	}

}
