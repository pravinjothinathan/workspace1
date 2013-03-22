package sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Auto_Client {

	private Map<Integer, Integer> accountPortMap = new HashMap<Integer, Integer>();
	public Map<Integer,Info> accountDetails;
	public Auto_Client(Map<Integer, Integer> accountPortMap) {
		accountDetails = new HashMap<Integer, Info>();
		this.accountPortMap = accountPortMap;
	}

	public String getUserInput() {
		Scanner scan = new Scanner(System.in);
		String input = scan.nextLine();
		return input;
	}

	public int getAccountNoFromUserInput(String input) {
		String[] inToken = input.split(" ");
		int accNo = Integer.parseInt(inToken[1]);
		return accNo;
	}

	private void createSocketAndSendData(String userInput,InetAddress serveraddress, int port) {
		Socket clientSocket = null;
		PrintWriter os = null;
		BufferedReader in = null;
		try {
			System.out.println("inside createSocketAndSendData ");
			clientSocket = new Socket(serveraddress, port);
			os = new PrintWriter(clientSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream()));

			// send the data
			os.println(userInput);

			// wait for the server
			String serverInput = null;
			while ((serverInput = in.readLine()) != null) {
				System.out.println(serverInput);
				break;
			}
			os.flush();
			os.close();
			in.close();
			clientSocket.close();

		} catch (UnknownHostException e) {
			System.err.println("Dont know about Host");
		} catch (IOException e) {
			System.err.println("No IO for host");

		}

	}

	private void startProcessing() {

		while (true) {
			// get user input
			System.out.println("in Start Processing!!");
			String userInput = GenerateInputs();//getUserInput();
			
			System.out.println("User Input -> startProcessing :"+userInput);
			try {
				// get the accountNo
				int accountNo = getAccountNoFromUserInput(userInput);

				// now get the port . :)
				Info i1 = this.accountDetails.get(accountNo);
				InetAddress serverAddress = i1.addr; 
				int port = this.accountPortMap.get(accountNo);

				// do the socket call
				createSocketAndSendData(userInput,serverAddress, port);
				
				System.out.println("Func completed!!");
			} catch (Exception e) {
				System.err.println("Error occured while processing request: "
						+ userInput + e);
			}
		}

	}

	private String GenerateInputs() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[] strOperations = {"checkbalance","deposit","withdraw","transfer"};
		String Msg ="";
		List<Integer> list = new ArrayList<Integer>(accountDetails.keySet());
		int rndOperation = (int)Math.random()*strOperations.length;
		int rndAcct1 = (int)Math.random()*accountDetails.size();
		switch(rndOperation)
		{
		case 0:
			Msg = strOperations[rndOperation]+" "+list.get(rndAcct1);
			break;
		case 1:
			Msg = strOperations[rndOperation]+" "+list.get(rndAcct1) + " 10";
			break;
		case 2:
			Msg = strOperations[rndOperation]+" "+list.get(rndAcct1) + " 10";
			break;
		case 3:
			int rndAcct2 = (int)Math.random()*accountDetails.size();
			Msg = strOperations[rndOperation]+" "+list.get(rndAcct1)+" "+list.get(rndAcct2) + " 10";
			break;
		}
		return Msg;
	}

	public static void main(String args[]) throws IOException,
			UnknownHostException {

		
		Auto_Client myClient = new Auto_Client(ParseFiles.sendMyMap());
		String fileName = "config.txt";
		ArrayList<MyServer> serverList = ParseFiles
				.parseFileForClient(fileName);
	
		for (Iterator<MyServer> iterator = serverList.iterator(); iterator.hasNext();) {
			MyServer myServer2 = (MyServer) iterator.next();
	
			for (int i = 0; i < myServer2.accountList.size(); i++) {
				int accno = Integer.parseInt(myServer2.accountList.get(i));
				myClient.accountDetails.put(accno, myServer2.ServInfo);
			}
		}
		myClient.startProcessing();
	}
}
