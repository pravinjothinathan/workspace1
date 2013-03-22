import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;


public class Client extends Thread{
	Socket s;
	DataInputStream disStreamReceiver;
	PrintStream psStreamSender;

	public Client(Socket tempSocket) {
		// TODO Auto-generated constructor stub
		s = tempSocket;
		try {
			disStreamReceiver = new DataInputStream(s.getInputStream());
			psStreamSender = new PrintStream(s.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//I dont think i need a client thread 
	//neeed to revisit and change the code 
	//look into !!!
	@SuppressWarnings("deprecation")
	public void run()
	{
		String line = "";
		while(true)
		{
			try {
				line = disStreamReceiver.readLine();
				if(line!=null)
				{
					System.out.println(line);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("deprecation")
	public String ReadLine() {
		// TODO Auto-generated method stub
		String line ="";
		try {
			line = disStreamReceiver.readLine();
			if(line!=null)
			{
				System.out.println(line);
				return line;
			}
			else
			{
				return null;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
