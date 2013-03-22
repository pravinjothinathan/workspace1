/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//package svmcluster;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.TreeMap;


public class Base {

	/**
	 * @param args
	 *
	 *
	 */

     public Base()
    {

    }
	TreeMap<String,String> tmEvents;
	List<DataPoint> Dps;
	TreeMap<String,TreeMap<String,Feature>> Map;
	int ParentNo;
	
	
	public String GetEventValue(String Event)
	{
		return tmEvents.get(Event);
	}

	public double GetDoubleEventValue(String Event)
	{
		return Integer.parseInt(tmEvents.get(Event));
	}


	public TreeMap<String,String> GetEvents()
	{
		return tmEvents;
	}

	public List<DataPoint> GetDataPoints()
	{
		return Dps;
	}

	public TreeMap<String,TreeMap<String,Feature>> GetMap()
	{
		return Map;
	}

	public String GetParentValue(String Event1,String Event2)
	{
		if(Map.containsKey(Event1))
		{
			TreeMap<String,Feature> sample = Map.get(Event1);

			if(Map.containsKey(Event2))
			{
				Feature f = sample.get(Event2);
				return f.Parent;
			}
			else
			{
				return new String(String.format("%s", ++ParentNo));
			}
		}
		else
		{
			return new String(String.format("%s", ++ParentNo));
		}
	}

	public double GetDoubleParentValue(String Event1,String Event2)
	{
		if(Map.containsKey(Event1))
		{
			TreeMap<String,Feature> sample = Map.get(Event1);

			if(Map.containsKey(Event2))
			{
				Feature f = sample.get(Event2);
				return Integer.parseInt(f.Parent);//(ParentNo + 100);//(double)Integer.parseInt(tmEvents.get(Event)) / (ParentNo + 100);
			}
			else
			{
				return ++ParentNo;//(ParentNo + 100);
			}
		}
		else
		{
			return ++ParentNo;//(ParentNo + 100);
		}
	}


	public Base(String strListPath,String strTreePath,String Filename) throws Exception
	{
		String strListFilter =".list";
		String strTreeFilter = ".tree";

		FileHandler FH = new FileHandler();

		String[] strListFiles = FH.FilesInaFolder(strListPath,strListFilter);
		String[] strTreeFiles = FH.FilesInaFolder(strTreePath, strTreeFilter);

		List<Event> Events = FH.EventData(strListFiles,strListPath);

		//contains the event info
		tmEvents = FH.MapData(Events);

		//List<DataPoint> Dps1= FH.ParseTree(strTreePath,strTreeFiles,tmEvents);
		Dps= FH.ParseTree1(strTreePath,strTreeFiles,tmEvents,strListPath);

		Map = FH.getMap();

		ParentNo = FH.getParentNo();

		File file = new File(Filename);
		BufferedWriter output;
		try {
			output = new BufferedWriter(new FileWriter(file));
                        output.write("@relation trainingData");
                        output.newLine();
                        output.newLine();
                        output.write("@attribute event1 numeric");
                        output.newLine();
                        output.write("@attribute event2 numeric");
                        output.newLine();
                        output.write("@attribute distance numeric");
                        output.newLine();
                        output.write("@attribute docnumber numeric");
                        output.newLine();
                        output.write("@attribute sameSentence {true, false}");
                        output.newLine();
                        output.write("@attribute WordDistance numeric");
                        output.newLine();
                        output.write("@attribute SynsetNoEv1 numeric");
                        output.newLine();
                        output.write("@attribute SynsetNoEv2 numeric");
                        output.newLine();
                        output.write("@attribute SemanticSimilarity numeric");
                        output.newLine();
                        output.write("@attribute connect {true, false}");
                        output.newLine();
                        output.newLine();
                        output.write("@data");
                        output.newLine();
                        Synsets sn = new Synsets();
			for(int i=0;i<Dps.size();i++)
			{
				Dps.get(i).Print();
				//Dps.get(i).PrintFile(output);
				Dps.get(i).PrintFile(output,tmEvents,ParentNo,strTreeFiles.length,sn);

			}
			output.close();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

	}


	public static void main(String[] args)
	{
		String strListFilter =".list";
		String strTreeFilter = ".tree";
		String strListPath = "/Users/pravinjothinathan/Downloads/subevents";
		String strTreePath  = "/Users/pravinjothinathan/Documents/NLP/Annotations/";

		FileHandler FH = new FileHandler();
		String[] strListFiles = FH.FilesInaFolder(strListPath,strListFilter);
		String[] strTreeFiles = FH.FilesInaFolder(strTreePath, strTreeFilter);

		List<Event> Events = FH.EventData(strListFiles,strListPath);

		//contains the event info
		TreeMap<String,String> tmEvents = FH.MapData(Events);

		//List<DataPoint> Dps1= FH.ParseTree(strTreePath,strTreeFiles,tmEvents);
		List<DataPoint> Dps= FH.ParseTree1(strTreePath,strTreeFiles,tmEvents,strListPath);

		//Contains the map info
		//TreeMap<String,TreeMap<String,Feature>> Map = FH.getMap();

		File file = new File("write.txt");
		BufferedWriter output;
		try {
			output = new BufferedWriter(new FileWriter(file));
			for(int i=0;i<Dps.size();i++)
			{
				Dps.get(i).Print();
				//Dps.get(i).PrintFile(output);
				//Dps.get(i).PrintFile(output,tmEvents);
				//Dps.get(i).PrintFile(output,tmEvents,ParentNo,strTreeFiles.length);
			}
			output.close();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	//	String[] strHtmlFiles = FilesInaFolder(strPath, strHtmlFilter);
	}
}
