import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.management.ManagementFactory;


public class Helper {
	
	public void Print(String strtoPrint)
	{
		System.out.println(strtoPrint);
	}
	
	public String ListFilesinFolder(String Path)
	{
		
		File Folder = new File(Path);
		String[] files = Folder.list();
		
		return StringArraytoString(files); 
		
		
	}

	private String StringArraytoString(String[] files) {
		// TODO Auto-generated method stub
		String temp ="";
		for(int i=0;i<files.length;i++)
		{
			if(i!=files.length-1)
				temp = temp + files[i] + " ";
			else
				temp = temp+ files[i];
		}
		
		return temp;
	}

	public void FileWrite(String strFolder, String strFileName, String msg) {
		// TODO Auto-generated method stub
		String Path = strFolder +"/"+ strFileName;
		try
		{
			BufferedWriter out = new BufferedWriter(new FileWriter(Path,true));
			out.write(msg);
			out.newLine();
			out.close();
		}
		catch(Exception e)
		{
			Print("Crash! File write Failed");
			e.printStackTrace();
		}
	}
	
	public String FileReadLastLine(String strFolder, String strFileName) {
		// TODO Auto-generated method stub
		String Path = strFolder +"/"+ strFileName;
		String temp ="";
		
		 try 
		 {
			BufferedReader input =  new BufferedReader(new FileReader(Path));
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
	
	public int GetPID() {
		// TODO Auto-generated method stub
		String[] arr = ManagementFactory.getRuntimeMXBean().getName().split("@"); 
		Print("Process Id -" + arr[0]);
		return Integer.parseInt(arr[0]);
	}

}
