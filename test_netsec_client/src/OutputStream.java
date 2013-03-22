import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;


public class OutputStream extends Thread{

	PrintStream psDataOut;
	String username;
	
	public OutputStream(Socket sock) {
		// TODO Auto-generated constructor stub
		try {
			psDataOut = new PrintStream(sock.getOutputStream());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void run()
	{
		InputStreamReader isrReader = new InputStreamReader(System.in);
		BufferedReader brReader = new BufferedReader(isrReader);
	
		boolean invalidData = true;
		int option = 0;
		while(invalidData)
		{
			System.out.println("Press 1 for Login:");
			System.out.println("Press 2 for Registration");
			try {
				String line = brReader.readLine();
				if(line.equals("1")){
					option =1;
					invalidData = false;
				}
				else if (line.equals("2")){
					option =2;
					invalidData = false;
					}
				else{
					System.out.println("Invalid input!!! Try again!!");
				}
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("Exception in read from prompt!!!");
			}

			System.out.println("Option Entered Succesful opt -> "+option);
			
			if(option ==1)
				invalidData = Login(brReader);
			else
				invalidData = Register(brReader);

		}
	}

	private boolean Register(BufferedReader brReader) {
		// TODO Auto-generated method stub
		String password1 ="",password2 ="";
		System.out.println("Welcome to the Registration Process ");
		System.out.println("Please Enter your Username");
		username = ReadLine(brReader);
		System.out.println("Enter Password :");
		password1 = ReadLine(brReader);
		System.out.println("Retype Password :");
		password2 = ReadLine(brReader);
		if(password1.equals(password2)/*&&username.length()<12*/)
		{
			Sha512 sha = new Sha512();
			String usrpwd = String.format("771%srim%s907", username,password1);
			System.out.println("usrpwd"+usrpwd);
			String hashusrpwd = sha.GetDigest(usrpwd);
			System.out.println("hashusrpwd -> "+hashusrpwd);
			String Message = String.format("%s:%s:%s","register",username,hashusrpwd);
			RSA rsa = new RSA();
			byte[] encMsg = rsa.EncryptMessage(Message);
			String strEncMsg = asHex(encMsg);
			System.out.println("strEncMsg -> "+strEncMsg);
			SendMessage(strEncMsg);
		}
		else
		{
			return false;
		}
		return false;
	}
	
	public void SendMessage(String msg)
	{
		try {
			psDataOut.println(msg);
			System.out.println("Message Sent");
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception in Message send !!!");
		}
		
	}

	private boolean Login(BufferedReader brReader) {
		// TODO Auto-generated method stub
		String password ="";
		System.out.println("Enter User Name :");
		username = ReadLine(brReader);
		System.out.println("Enter Password :");
		password = ReadLine(brReader);
		
		
		Sha512 sha = new Sha512();
		String msg = String.format("111%s222%s333", username,password);
		String hash = sha.GetDigest(msg);
		System.out.println("Hash ->"+hash);
		
		RSA rsa = new RSA();
		byte[] encMsg = rsa.EncryptMessage(hash);
		
		String strEncMsg = asHex(encMsg);
		System.out.println("Encrypted Message length "+strEncMsg.length());
		System.out.println(asHex(encMsg));
		
		SendMessage(asHex(encMsg));
		
		return false;
		
		
	}
	
	public String asHex (byte buf[]) {
        StringBuffer strbuf = new StringBuffer(buf.length * 2);
        int i;

        for (i = 0; i < buf.length; i++) {
         if (((int) buf[i] & 0xff) < 0x10)
  	    strbuf.append("0");

         strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
        }

        return strbuf.toString();
       }


	private String ReadLine(BufferedReader brReader) {
		// TODO Auto-generated method stub
		String txt="";
		try {
			txt = brReader.readLine();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return txt;
	}

}
