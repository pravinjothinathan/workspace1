package sample;

//import java.io.BufferedReader;
import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
	public ConcurrentHashMap<Integer, Integer> myMap;
	private Map<Integer, Integer> accountPortMap;
	private Map<String, Integer> serverPortMap;
	public Map<Integer,Info> accountDetails;
	private clientThread t[];

	public String serverName;
	private static int port;

	public Server(String serverName) {
		super();
		this.serverName = serverName;
		myMap = new ConcurrentHashMap<Integer, Integer>();
		accountPortMap = new HashMap<Integer, Integer>();
		serverPortMap = new HashMap<String, Integer>();
		accountDetails = new HashMap<Integer, Info>();
		t = new clientThread[10];
	}

	public ConcurrentHashMap<Integer, Integer> getMyMap() {
		return myMap;
	}

	public static void main(String[] args) throws IOException {

		String serverName = args[0];
		String fileName = "config.txt";
		Server myServer = new Server(serverName);
		int ssPort =0;
		ArrayList<MyServer> serverList = ParseFiles.parseFileForClient(fileName);
		ArrayList<Account> acc = ParseFiles.parseFileForServer(fileName);
		
		for (Iterator iterator = serverList.iterator(); iterator.hasNext();) {
			MyServer myServer2 = (MyServer) iterator.next();
			System.out.println("myServer2.serverName-"+myServer2.serverName);
			System.out.println("serverName-"+serverName);
			if(myServer2.serverName.equals(serverName))
			{
				for (Iterator itr = acc.iterator(); itr.hasNext();) {
					Account account = (Account) itr.next();
					String chkAct = String.format("%s", account.accntno);
					System.out.println("acct-"+chkAct);
					if(myServer2.accountList.contains(chkAct))
					{
						myServer.myMap.put(account.getAccntno(), account.getBalance());
						System.out.println("add : act no -"+account.accntno +"bal -"+account.balance);
					}
				}
				//for (String account : myServer2.accountList) {
					
				//}
				//myServer.myMap.put(
			}
			for (int i = 0; i < myServer2.accountList.size(); i++) {
				int accno = Integer.parseInt(myServer2.accountList.get(i));
				myServer.accountDetails.put(accno, myServer2.ServInfo);
			}
			myServer.serverPortMap.put(myServer2.getServerName(),
					myServer2.getPortNumber());

		}
		// parse file passing the filename to the parseFiles
		
		
		/*for (Iterator iterator = acc.iterator(); iterator.hasNext();) {
			Account account = (Account) iterator.next();
			myServer.myMap.put(account.getAccntno(), account.getBalance());

		}*/
		
		myServer.accountPortMap = ParseFiles.sendMyMap();
		
		//Starting SnapShotServerListener
//		ServrSnapshotListener ssListener = new ServrSnapshotListener(ssPort);
//		ssListener.start();

		ServerSocket echoServer = null;
		Socket clientSocket = null;
		// start the server socket and accept connection
		try {
			// according to servername find port
			 port = myServer.serverPortMap.get(serverName);
			// bind to port and listen
			echoServer = new ServerSocket(myServer.port);
		} catch (IOException e) {
			System.out.println(e);
		}
		// in the connection
		while(true) {
		try {
			clientSocket = echoServer.accept();
			for(int i = 0; i <= 9; i++) {
				if(myServer.t[i] == null) {
					
					(myServer.t[i] = new clientThread(clientSocket,myServer.t, port,myServer)).start();
					break;
				}
				}
		} catch (IOException e) {
			System.out.println(e);
		}
		}
	}
}
