import java.io.DataInputStream;
import java.io.PrintStream;
import java.net.Socket;


public class Server extends Thread{

	Socket currSock;
	Server[] currClients;
	DataInputStream disInputStream;
	PrintStream psOutputStream;
	String strFolder; 
	int PID;
	Helper h;
	
	public Server(Socket clientSocket, Server[] clients, String folderPath) {
		// TODO Auto-generated constructor stub
		h = new Helper();
		h.Print("Entering client constructor");
		currSock = clientSocket;
		currClients = clients;
		strFolder = folderPath;
		PID = h.GetPID();
		h.Print("Client Creation : socket -"+clientSocket);
	}
	
	@SuppressWarnings("deprecation")
	public void run()
	{
		try
		{
			h.Print("Thread Started");
			disInputStream = new DataInputStream(currSock.getInputStream());
			h.Print("Input Stream Created");
			psOutputStream = new PrintStream(currSock.getOutputStream());
			h.Print("Output Stream Created");
			while(true)
			{
				h.Print("waiting for the input command ");
				String str = disInputStream.readLine();
				h.Print("received :"+str);
				if(str.startsWith("enquire"))
					psOutputStream.println(GetFilesList());
				else if(str.startsWith("read"))
					psOutputStream.println(FileRead(str));
				else if(str.startsWith("write"))
					psOutputStream.println(FileWrite(str));
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private String FileWrite(String str) {
		// TODO Auto-generated method stub
		h.Print("File Write initiated");
		String[] params = str.split(" ");
		String msg = "PID :"+params[2] + " TimeStamp :"+params[3];
		h.FileWrite(strFolder,params[1],msg);
		return "write succesful : PID -" + PID;
	}

	private String FileRead(String str) {
		// TODO Auto-generated method stub
		h.Print("File Read initiated");	
		String[] params = str.split(" ");
		return h.FileReadLastLine(strFolder, params[1]);
	}

	private String GetFilesList() {
		// TODO Auto-generated method stub
		h.Print("Files list requested");
		return h.ListFilesinFolder(strFolder);
	}

}
