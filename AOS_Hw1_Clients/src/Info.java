import java.net.InetAddress;


public class Info {

	InetAddress addr;
	int portNo;
	
	public Info(InetAddress byName, int port) {
		// TODO Auto-generated constructor stub
		addr = byName;
		portNo = port;
	}
	
	public void Print()
	{
		System.out.println("Address :"+addr);
		System.out.println("Port No :"+portNo);
	}
	
}
