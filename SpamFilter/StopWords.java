import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;


public class StopWords {
	
	List<String> StopWords;

	public StopWords(String PAth)
	{
		StopWords = new ArrayList<String>();
		try
		{
			FileReader frReader = new FileReader(PAth);
			BufferedReader bisReader = new BufferedReader(frReader); 
			String line="";
			while((line=bisReader.readLine())!=null)
			{
				StopWords.add(line);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public List<String> getStopWords()
	{
		return StopWords;
	}
}
