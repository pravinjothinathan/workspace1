import java.util.ArrayList;
import java.util.List;

import weka.core.Instances;


public class TestData {
	List<Instances> data;
	public TestData(String[] lstFiles, String path)
	{
		FileHandler FH = new FileHandler();
		data = new ArrayList<Instances>();
		
		for (String file : lstFiles) {
			String nwPath = path + "/" + file;
			System.out.println(nwPath);
			Instances tInstances = FH.getInstances(nwPath);
			data.add(tInstances);
		}
	}
	
	public List<Instances> getTestInstances()
	{
		return data;
	}
}
