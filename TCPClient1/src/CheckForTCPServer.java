import java.net.InetAddress;
import java.net.Socket;


public class CheckForTCPServer {

	public void check(String host) {
		// TODO Auto-generated method stub
		try
		{
			InetAddress addr = InetAddress.getByName(host);
			
			for(int i=1025;i<65536;i++)
			{
				System.out.println(i);
				try
				{
					new Socket(addr,i);
					System.out.println("port : "+i+" is listening for TCP connections");
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			
			return;
		}
		catch(Exception E)
		{
			E.printStackTrace();
			//System.out.println(E.printStackTrace());
		}
	}

}
