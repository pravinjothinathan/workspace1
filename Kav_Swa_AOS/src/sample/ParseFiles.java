package sample;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

public class ParseFiles {

	public static ArrayList<Account> parseFileForServer(String fileName)
			throws IOException {

		// parse the file
		// create a arraylist of accountz and return it
		int accNumbr = 0;
		int amt = 0;

		ArrayList<Account> myAccounts = new ArrayList<Account>();
		List<String> lines = FileUtils.readLines(new File("config.txt"));

		for (Iterator<String> iterator = lines.iterator(); iterator.hasNext();) {
			String tok = (String) iterator.next();
			String[] tokens = tok.split(":");

			for (int i = 2; i < tokens.length - 1; i = i + 2) {
				accNumbr = Integer.parseInt(tokens[i]);
				amt = Integer.parseInt(tokens[i + 1]);
				Account myAccount = new Account(accNumbr, amt);
				myAccounts.add(myAccount);
			}
		}
		return myAccounts;
	}
	
	
	
	public static Map<Integer, Integer> sendMyMap() throws IOException {
		String fileName = "config.txt";
		ArrayList<MyServer> serverList = parseFileForClient(fileName);
		Map<Integer, Integer> accountPortMap = new HashMap<Integer, Integer>();
		for (MyServer server : serverList) {
		for (String accountNo : server.getAccountList()) {
			int accNo = Integer.parseInt(accountNo);
		accountPortMap.put(accNo, server.getPortNumber());
		}
		}
		return accountPortMap;
		}


	public static ArrayList<MyServer> parseFileForClient(String fileName)
			throws IOException {

		int accNumbr = 0;
		String serverName;
		int portNumbr;

		ArrayList<MyServer> myServ = new ArrayList<MyServer>();
		;
		List<String> lines = FileUtils.readLines(new File("config.txt"));
		int ii=1;
		for (Iterator<String> iterator = lines.iterator(); iterator.hasNext();) {
			String tok = (String) iterator.next();
			String[] tokens = tok.split(":");
			serverName = tokens[0];
			String SerPort = tokens[1];
			String[] tempServPort = SerPort.split("-");
			Info ServInfo = new Info(InetAddress.getByName(tempServPort[0]), Integer.parseInt(tempServPort[1]));
			portNumbr = Integer.parseInt(tempServPort[1]);
			System.out.println(ii++);
		
			List<String> accountList = new ArrayList<String>();
			for (int i = 2; i < tokens.length - 1; i = i + 2) {
				accountList.add(tokens[i]);
			}
			MyServer servObj = new MyServer(serverName, portNumbr, accountList,ServInfo);
			myServ.add(servObj);
		}
		return myServ;
	}
}

// Account myAccount = new Account();
// int accNumbr = 0;
// int amt = 0;

/*
 * for (Iterator<Account> iterator2 = myAccounts.iterator(); iterator2
 * .hasNext();) { Account account = (Account) iterator2.next();
 * System.out.println(account.getAccntno() + "    :" + account.getBalance());
 * 
 * 
 * }
 */
// Client myClient = new Client();
// parseFiles pf = new parseFiles();
// myClient.getMyAccounts(myAccounts);
// myServer.setMyMap(myAccounts);
// myServer.printMap();
// }

/*
 * public static ArrayList<Integer> getPortNo() { return portNo; }
 * 
 * public static void setPortNo(ArrayList<Integer> portNo) { ParseFiles.portNo =
 * portNo; }
 * 
 * }
 */