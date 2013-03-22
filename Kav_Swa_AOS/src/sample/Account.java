package sample;

public class Account {
	
	public int accntno;
	public int balance;
	
	public Account(int accntno,int balance) {
		this.accntno = accntno;
		this.balance = balance;
	}
	public int getAccntno() {
		return accntno;
	}
	public void setAccntno(int accntno) {
		this.accntno = accntno;
	}
	public int getBalance() {
		return balance;
	}
	public void setBalance(int balance) {
		this.balance = balance;
	}
	

}
