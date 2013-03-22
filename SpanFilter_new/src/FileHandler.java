import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FileHandler {
	List<String> UniqueWords;
	Helper h;
	public FileHandler()
	{
		h = new Helper();
		UniqueWords = new ArrayList<String>();
	}
	
	public Map<String,Integer> FetchData(String FileName,int fileno)
	{
		Map<String,Integer> WordMap = new HashMap<String,Integer>();		
		String temp ="";//= new StringBuilder();
		
		 try 
		 {
			BufferedReader input =  new BufferedReader(new FileReader(FileName));
			
			String line = null; 
	        
	        while (( line = input.readLine()) != null)
	        {
	        
	        	temp = temp + " "+ line;
	        }
	        
	        input.close();
		 } 
		 catch (Exception e) 
		 {
			e.printStackTrace();
		 }
		 
		 for (String token : temp.split("[ ]")) {
			
			 if(!UniqueWords.contains(token))
			 {
				 UniqueWords.add(token);
			}
			 
			if(WordMap.containsKey(token))
			{
				int n = WordMap.get(token)+1;
				WordMap.remove(token);
				WordMap.put(token, n);
			}
			else
			{
				WordMap.put(token, 1);
			}
		}
		 	
		return WordMap;
	}

	public Map<String, Integer> FetchData(String string, int fileCounter,
			List<String> lststopWords) {
		// TODO Auto-generated method stub
		Map<String,Integer> WordMap = new HashMap<String,Integer>();		
		String temp ="";//= new StringBuilder();
		
		 try 
		 {
			BufferedReader input =  new BufferedReader(new FileReader(string));
			
			String line = null; 
	        
	        while (( line = input.readLine()) != null)
	        {
	        
	        	temp = temp + " "+ line;
	        }
	        
	        input.close();
		 } 
		 catch (Exception e) 
		 {
			e.printStackTrace();
		 }
		 
		 for (String token : temp.split("[ ]")) {
			 
			 if(lststopWords.contains(token))
				 continue;
			 //if(token.length()<=3)
				// continue;
			 //if(h.isIntNumber(token))
				 //continue;
			
			 if(!UniqueWords.contains(token))
			 {
				 UniqueWords.add(token);
			}
			 
			if(WordMap.containsKey(token))
			{
				int n = WordMap.get(token)+1;
				WordMap.remove(token);
				WordMap.put(token, n);
			}
			else
			{
				WordMap.put(token, 1);
			}
		}
		 	
		return WordMap;
	}
	
	void WriteArff(String location, int[][] filetoWordcountMatrix, int[] fileClassInfo, List<String> vocab, String relation)
	{
		  try{
			  FileWriter fstream = new FileWriter(location);
			  BufferedWriter out = new BufferedWriter(fstream);
			  out.write("@relation "+relation);
			  out.write("\n");
			  
			  int ii=0;
			  for (int i=0;i<filetoWordcountMatrix[0].length;i++) {
				  out.write("@attribute '"+ii+++"' real");
				  out.write("\n");
			}
			  out.write("@attribute 'class' {ham,spam}");
			  out.write("\n");
			  
			  out.write("@data");
			  out.write("\n");
			  
			  for (int i = 0; i < filetoWordcountMatrix.length; i++) {
				for (int j = 0; j < filetoWordcountMatrix[0].length; j++) {
					out.write(filetoWordcountMatrix[i][j]+",");
				}
				//out.write("\n");
				if(fileClassInfo[i]==1)
					out.write("ham");
				else
					out.write("spam");
				out.write("\n");
			}
			  //Close the output stream
			  out.close();
			  }catch (Exception e){//Catch exception if any
			  System.err.println("Error: " + e.getMessage());
			  }
			  

	}

}
