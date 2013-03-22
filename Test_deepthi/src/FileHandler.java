import java.io.BufferedReader;
import java.io.FileReader;


public class FileHandler {
	
	public String ReadFileintoaString(String filetoRead)
	{
		
		String temp = "";
		try
		{
			BufferedReader input =  new BufferedReader(new FileReader(filetoRead));
			String line;
	        while (( line = input.readLine()) != null)
	        {
	        	temp += line + "@";
	        }
		}catch(Exception e)
		{
			System.out.println("Error in reading file");
		}
		return temp;
	}

}
