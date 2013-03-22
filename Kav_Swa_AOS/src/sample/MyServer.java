package sample;

import java.util.List;

public class MyServer {
	
	public String serverName;
	public Info ServInfo;
	private int portNumber;
	public List<String> accountList;
	

	public MyServer(String serverName,int portNumber,List<String> accountList,Info servInfo) {
		// TODO Auto-generated constructor stub
		this.serverName = serverName;
		this.portNumber = portNumber;
		this.accountList = accountList;
		ServInfo = servInfo;
		
	}
	
	public List<String> getAccountList() {
		return accountList;
	}
	public void setAccountList(List<String> accountList) {
		this.accountList = accountList;
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public int getPortNumber() {
		return portNumber;
	}
	public void setPortNumber(int portNumber) {
		this.portNumber = portNumber;
	}
	

}
