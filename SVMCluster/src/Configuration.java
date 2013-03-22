import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

//SubEvents=1
//Annotations=2
//ArffFile=3
//WordNetDictionary=4
//tmEventsFile=file1.txt
//MasterMapFile=file2.txt


public class Configuration 
{
	Properties prop;
	
	public Configuration()
	{
		prop = new Properties();
		try {
			prop.load(new FileInputStream("config.properties.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getSubevent()
	{
		return prop.getProperty("SubEvents");
	}
	
	public String getAnnotations()
	{
		return prop.getProperty("Annotations");
	}
	
	public String getArffFile()
	{
		return prop.getProperty("ArffFile");
	}
	
	public String getWordNetDictionary()
	{
		return prop.getProperty("WordNetDictionary");
	}
	
	public String gettmEventsFile()
	{
		return prop.getProperty("tmEventsFile");
	}
	public String getMasterMapFile()
	{
		return prop.getProperty("MasterMapFile");
	}
	
	
}
