import java.net.InetAddress;
import java.net.Socket;
import java.util.List;


public class Killer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Helper h = new Helper();
		InputParameters ip = new InputParameters(args[0]);
		int n =51555;
		
		List<Info> Ports = ip.Servers;
		for(int i=0;i<Ports.size();i++)
		{
			Info temp = Ports.get(i);
			try {
				
				Socket s = new Socket(temp.addr,temp.portNo,InetAddress.getByName("localhost"),n++);
				s.close();
				System.out.println(temp.portNo);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				h.Print("Crash! Creating Socket Connection");
				e.printStackTrace();
			}
		}
	}	

}
