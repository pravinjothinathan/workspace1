import java.io.DataInputStream;
import java.io.PrintStream;
import java.net.Socket;


public class Clients extends Thread{

	Socket sock;
	PrintStream psDataOut;
	DataInputStream disDataIn;
	
	public Clients(Socket sockin)
	{
		sock = sockin;
		try {
			psDataOut = new PrintStream(sock.getOutputStream());
			disDataIn = new DataInputStream(sock.getInputStream());
		} catch (Exception e) {
			System.out.println("Exception in creating input and output streams for the client server communication!!!");
			// TODO: handle exception
		}
		
	}
	
	@SuppressWarnings("deprecation")
	public void run()
	{
		String line;
		
		while(sock!=null)
		{
			try {
				line = disDataIn.readLine();
				if(line!=null)
					ProcessMessage(line);
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		}
	}
	
    public static byte[] asBytes (String s) {
        String s2;
        byte[] b = new byte[s.length() / 2];
        int i;
        for (i = 0; i < s.length() / 2; i++) {
            s2 = s.substring(i * 2, i * 2 + 2);
            b[i] = (byte)(Integer.parseInt(s2, 16) & 0xff);
        }
        return b;
    }

	
	public static String asHex (byte buf[]) {
        StringBuffer strbuf = new StringBuffer(buf.length * 2);
        int i;

        for (i = 0; i < buf.length; i++) {
         if (((int) buf[i] & 0xff) < 0x10)
  	    strbuf.append("0");

         strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
        }

        return strbuf.toString();
       }


	private void ProcessMessage(String line) {
		// TODO Auto-generated method stub
		System.out.println("Process in coming message from client");
		RSA rsa = new RSA();
		System.out.println("incoming message -> " + line);
		System.out.println("Decypted Message -> "+rsa.DecryptMessage(asBytes(line)));
	}
}
