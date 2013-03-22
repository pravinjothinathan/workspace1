import java.io.BufferedReader;
import java.io.FileReader;
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

}
