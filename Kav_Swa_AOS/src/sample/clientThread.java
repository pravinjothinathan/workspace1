package sample;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class clientThread extends Thread {
	Socket clientSocket = null;
	PrintStream os = null;
	DataInputStream is = null;
	clientThread t[] = null;
	static boolean freeze = false;
	static List<String> MessagesinTransit = new ArrayList<String>();
	String line;
	int serverPortNo;
	Server objServer;
	static private Map<Integer, Integer> accountPortMap = new HashMap<Integer, Integer>();

	public clientThread(Socket clientSocket, clientThread[] t,int servPortNo,Server sServer) {
		this.clientSocket = clientSocket;
		this.t = t;
		serverPortNo = servPortNo;
		objServer = sServer;
	}
	@SuppressWarnings("deprecation")
	public void run() {
		try {
			is = new DataInputStream(clientSocket.getInputStream());
			os = new PrintStream(clientSocket.getOutputStream());
			accountPortMap = ParseFiles.sendMyMap();
			while(null!=(line = is.readLine()))
			{
				String[] toks = this.line.split("[\\s]");
				String operation = toks[0];
				if (operation.equals("checkbalance")) {
					int accountNo = Integer.parseInt(toks[1]);
					int currBal = 0;
					currBal = objServer.myMap.get(accountNo);
					os.println("Current Balance" + currBal);
				}
				if(freeze!=true)
				{
					funcTransactions(line);
					// deposit step
				}
				else
				{
					System.out.println("Frozen!!");
					if (operation.equals("deposit")||operation.equals("withdraw")||operation.equals("transfer"))
					{
						MessagesinTransit.add(line);
					}
				}
				if(operation.equals("snapshot"))
				{
					System.out.println("SS Received");
					freeze = true;
					os.println(convertStateToString());
					String line="";
					String Msg2Send ="";
					while(null!=(line=is.readLine()))
					{
						if(line.equals("defrost"))
						{
							while(MessagesinTransit.size()>0)
							{
								Msg2Send += MessagesinTransit.get(0);
								funcTransactions(Msg2Send);
								MessagesinTransit.remove(0);
							}
							os.println(Msg2Send);
							freeze = false;
							System.out.println("Received Deforst from Snapshot Server");
							break;
						}
					}
					//os.println("SS ACK"+objServer.serverName);
				}
			}
			
			
				    // close the output stream
				    // close the input stream
				    // close the socket
				    
				    is.close();
				    os.close();
				    clientSocket.close();


		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	 
	private void funcTransactions(String line) {
		// TODO Auto-generated method stub
		String[] toks = this.line.split("[\\s]");
		String operation = toks[0];
		if (operation.equals("deposit")) {
			//String resString = null;
			int accountNo = Integer.parseInt(toks[1]);
			int amount = Integer.parseInt(toks[2]);
			int currBal = 0;
			int totBal = 0;
			currBal = objServer.myMap.get(accountNo);
			totBal = currBal + amount;
			os.println("Total Balance: " + totBal);
			objServer.myMap.put(accountNo, totBal);
		}
		// withdraw step
		if (operation.equals("withdraw")) {
			int accountNo = Integer.parseInt(toks[1]);
			int amount = Integer.parseInt(toks[2]);
			int currBal = 0;
			int totBal = 0;
			currBal = objServer.myMap.get(accountNo);
			totBal = currBal - amount;
			os.println("Total Balance: " + totBal);
			objServer.myMap.put(accountNo, totBal);
		}
		
		//transfer step 
		//new socket will separately send new communication to the server
		if (operation.equals("transfer")) {
			
			 int srcAccountNo = Integer.parseInt(toks[1]);
			 int destAccountNo = Integer.parseInt(toks[2]);
			 int amount = Integer.parseInt(toks[3]);

			os.println("The balance is" +transferClient(srcAccountNo,destAccountNo,amount));
		}

	}
	public String transferClient(int srcAccountNo, int destAccountNo,
			int amount) {
		Socket s = null;
		//PrintWriter os = null;
		String resString = null;
		int port1, port2, srcBal, transferBal, destBal, finalBal;
		
		Info i1 = objServer.accountDetails.get(srcAccountNo);
		Info i2 = objServer.accountDetails.get(destAccountNo);
		if (i1.addr == i2.addr && i1.portNo == i2.portNo) {
			System.out.println("Accounts in same server");
			srcBal = objServer.myMap.get(srcAccountNo);
			destBal = objServer.myMap.get(destAccountNo);
			transferBal = srcBal - amount;
			finalBal = destBal + amount;
			objServer.myMap.put(srcAccountNo, transferBal);
			objServer.myMap.put(destAccountNo, finalBal);
			resString = Integer.toString(transferBal);
			//os.println("Source Bal:" + Server.myMap.get(srcAccountNo));
			//os.println("Destination Bal:" + Server.myMap.get(destAccountNo));

		}
		
		else {
			System.out.println("Accounts in different server");
			
			srcBal = objServer.myMap.get(srcAccountNo);
			destBal = objServer.myMap.get(destAccountNo);
			transferBal = srcBal - amount;
			resString = Integer.toString(transferBal);
			try {
			s = new Socket(i2.addr, i2.portNo);
			PrintWriter dos = new PrintWriter(s.getOutputStream(), true);
			BufferedReader inp = new BufferedReader(new InputStreamReader(
			s.getInputStream()));
			String resLine = null;
			dos.println("deposit" +" " +destAccountNo+" " +amount);
			while (((resLine = inp.readLine()) != null)) {
			System.out.println(resLine);
			}
			dos.close();
			inp.close();
			s.close();
			} catch(IOException e) {
				System.out.println("No IO for the connection");
			}
			catch(Exception e) {
				System.out.println("Exception");
			}

		}
		return resString;
	}
	
	public String convertStateToString(){
		String vals ="";

		for (Iterator<Integer> iterator = this.objServer.myMap.keySet().iterator(); iterator.hasNext();) {
			int acno =  (int)iterator.next();
			int bal = objServer.myMap.get(acno);
			vals += acno + ":" + bal + ";";
		}
		return vals;
	}
	
}

//		else {
//			try {
//
//				/*Socket s = new Socket("localhost", port2);
//				PrintWriter dos = new PrintWriter(s.getOutputStream(), true);
//				BufferedReader inp = new BufferedReader(new InputStreamReader(
//						s.getInputStream()));
//				String resLine = null;
//				dos.println("deposit");
//				dos.print(destAccountNo);
//				dos.print(amount);
//				if (((resLine = inp.readLine()) != null)) {
//					System.out.println(resLine);
//				}
//
//				// check if thisport is equal to reference port
//				// if same , witdraw from srcacno, and deposit in dest ac
//				// witdraw 1234 200 , //deposit 1234 200
//				// if diff server
//				// create socket
//				// send same message //witdraw 1234 200 , //
//
//				dos.println();
//
//				dos.flush();
//				dos.close();
//				dos.close();
//				s.close();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}*/


