import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;


public class Helper {

	public void Print(InputParameters iPs) {
		// TODO Auto-generated method stub
		System.out.println("Port No :"+iPs.ServerPortNo);
		System.out.println("Port No :"+iPs.ClientPortNo);
		if(iPs.Servers!=null){
			for(Info Server : iPs.Servers) {
				System.out.println("Server Detail ->");
				Server.Print();
			}
		}
		System.out.println("Tail :"+iPs.tail);
		System.out.println("Server Id :"+iPs.ServerId);
	}
	
	
	public void FileWrite(String Filename, String msg) {
		// TODO Auto-generated method stub
		try
		{
			BufferedWriter out = new BufferedWriter(new FileWriter(Filename,true));
			out.write(msg);
			out.newLine();
			out.close();
		}
		catch(Exception e)
		{
			System.out.println("Crash! File write Failed");
			e.printStackTrace();
		}
	}
	
	public String FileReadLastLine(String strFileName) {
		// TODO Auto-generated method stub
		String temp ="";
		
		 try 
		 {
			BufferedReader input =  new BufferedReader(new FileReader(strFileName));
			String line = null; //not declared within while loop	        
	        while (( line = input.readLine()) != null)
	        {
	        	temp = String.format("%s", line);
	        }
	        input.close();
		 } 
		 catch (Exception e) 
		 {
			e.printStackTrace();
		 }
		
		return temp;
	}

}
