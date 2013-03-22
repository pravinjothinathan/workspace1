import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FileManager {
	
	FileHandler fh;
	
	public FileManager()
	{
		fh = new FileHandler();
	}
	
	public List<String> getUniqueWords()
	{
		return fh.UniqueWords;
	}

	public Map<Integer, FilewithClassInfo> FetchData(String strTrainingFiles) {
		// TODO Auto-generated method stub
		String HamFiles = strTrainingFiles +"/ham";
		String SpamFiles = strTrainingFiles +"/spam";
		
		Map <Integer,FilewithClassInfo> CompleteSet = new HashMap <Integer,FilewithClassInfo>();
		int FileCounter = 0;
		
		File spamFolder = new File(SpamFiles);
		File[] spamFiles = spamFolder.listFiles();
		for(int i=0;i<spamFiles.length;i++)
		{
			//System.out.println(spamFiles[i].toString());
			CompleteSet.put(FileCounter,new FilewithClassInfo(0,fh.FetchData(spamFiles[i].toString(),FileCounter)));
			FileCounter++;
		}
		
		File hamFolder = new File(HamFiles);
		File[] hamFiles = hamFolder.listFiles();
		for(int i=0;i<hamFiles.length;i++)
		{
			//System.out.println(hamFiles[i].toString());
			CompleteSet.put(FileCounter,new FilewithClassInfo(1,fh.FetchData(hamFiles[i].toString(),FileCounter)));
			FileCounter++;
		}
		
		return CompleteSet;
	}

	public Map<Integer, FilewithClassInfo> FetchData(String strTrainingFiles,
			List<String> lststopWords) {
		// TODO Auto-generated method stub
		String HamFiles = strTrainingFiles +"/ham";
		String SpamFiles = strTrainingFiles +"/spam";
		
		Map <Integer,FilewithClassInfo> CompleteSet = new HashMap <Integer,FilewithClassInfo>();
		int FileCounter = 0;
		
		File spamFolder = new File(SpamFiles);
		File[] spamFiles = spamFolder.listFiles();
		for(int i=0;i<spamFiles.length;i++)
		{
			//System.out.println(spamFiles[i].toString());
			CompleteSet.put(FileCounter,new FilewithClassInfo(0,fh.FetchData(spamFiles[i].toString(),FileCounter,lststopWords)));
			FileCounter++;
		}
		
		File hamFolder = new File(HamFiles);
		File[] hamFiles = hamFolder.listFiles();
		for(int i=0;i<hamFiles.length;i++)
		{
			//System.out.println(hamFiles[i].toString());
			CompleteSet.put(FileCounter,new FilewithClassInfo(1,fh.FetchData(hamFiles[i].toString(),FileCounter,lststopWords)));
			FileCounter++;
		}
		
		return CompleteSet;
	}
	
	

}
