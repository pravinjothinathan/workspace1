import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.List;


public class Client extends Thread{
	
	DataInputStream disDataIn;
	PrintStream psDataOut;
	List<Message> MessageInQueue;
	
	public Client(Socket tempsock, List<Message> messageInQueue)
	{
		try {
			disDataIn = new DataInputStream(tempsock.getInputStream());
			psDataOut = new PrintStream(tempsock.getOutputStream());
			MessageInQueue = messageInQueue;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error in client Data input and output stream creations");
			e.printStackTrace();
		}
	}
	
	public void run()
	{
		Message m = null; 
		while(true)
		{
			m = ReadStream();
			if(m!=null)
				MessageInQueue.add(m);
			
		}
	}
	
	@SuppressWarnings("deprecation")
	public Message ReadStream()
	{
		String line;
		try {
			//System.out.println("Read before!!");
			line = disDataIn.readLine();
			//System.out.println("Read after!!");
			if(line != null)
				return new Message(line);
			else
				return null;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error in Message read!!");
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("deprecation")
	public Message ReadStream(int ServerId)
	{
		String line;
		try {
			//System.out.println("Read before!!"+ServerId);
			line = disDataIn.readLine();
			//System.out.println("Read after!!"+ServerId);
			if(line != null)
				return new Message(line);
			else
				return null;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error in Message read!!");
			e.printStackTrace();
		}
		return null;
	}
}
