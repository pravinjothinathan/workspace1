import java.io.BufferedReader;
import java.io.FileReader;


public class FileHandler {
	
	public String FetchData(String FileName)
	{
		StringBuilder temp = new StringBuilder();
		
		 try 
		 {
			BufferedReader input =  new BufferedReader(new FileReader(FileName));
			
			String line = null; //not declared within while loop
	        
	        while (( line = input.readLine()) != null)
	        {
	        	//System.out.println(line);
	        	temp.append(line+";");
	        }
		 } 
		 catch (Exception e) 
		 {
			e.printStackTrace();
		 }
		
		return temp.toString();
	}

}
