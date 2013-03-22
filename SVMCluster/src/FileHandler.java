/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//package svmcluster;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

import edu.mit.jwi.item.ISynsetID;


public class FileHandler
{
	int ParentNo;
	List<String> cEvents;
	TreeMap<String,ISynsetID> SynSetMap;
	TreeMap<String,Integer> DepthMap;
	TreeMap<String,TreeMap<String,Feature>> MasterMap;
	int FileNo;
	Synsets s ;

        public int GetFileNo()
        {
            return FileNo + 1;
        }

	public FileHandler()
	{
		
		try {
			s = new Synsets();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SynSetMap = new TreeMap<String,ISynsetID>();
		DepthMap = new TreeMap<String,Integer>();
		MasterMap = new TreeMap<String,TreeMap<String,Feature>>();
		ParentNo = 2000;
		cEvents = null;
		FileNo =0;
	}

	public TreeMap<String,TreeMap<String,Feature>> getMap()
	{
		return MasterMap;
	}

	public TreeMap<String,String> MapData(List<Event> events)
	{
		TreeMap<String,String> map = new TreeMap<String,String>();
		for(int i=0;i<events.size();i++)
		{
			map.put(events.get(i).text, String.format("%d", i));
		}
		return map;
	}

	public String[] FilesInaFolder(String strPath, String strFilter) {
		File fileFolder = new File(strPath);
		FilenameFilter list = new Extensions(strFilter);
		String[] Files = fileFolder.list(list);
		return Files;
	}

	public List<Event> EventData(String[] FileNames,String strPath)
	{
		List<String>  Sample = new ArrayList<String>();
		List<Event> Events = new ArrayList<Event>();
		String line;
		int totalWords = 0;
		for(int i=0;i<FileNames.length;i++)
		{
			 try
			 {
				BufferedReader input =  new BufferedReader(new FileReader(strPath+"/"+FileNames[i]));

				line ="";
		        while (( line = input.readLine()) != null)
		        {
		        	String[] temp = line.split(" ");
		        	if(Sample.contains(temp[0])==false)
		        	{
		        		Sample.add(new String(temp[0]));
		        		Events.add(new Event(new String (temp[0])));
		        	}
		        	else
		        	{
		        		int temp1 = Sample.lastIndexOf(temp[0]);
		        		Events.get(temp1).incrementCount();
		        	}
		        	totalWords++;
		        }
			 }
			 catch (Exception e)
			 {
				e.printStackTrace();
			 }
		}
		//System.out.println("Total Words" + totalWords);
		return Events;
	}

	/*public List<DataPoint> ParseTree(String strTreePath, String[] strTreeFiles,TreeMap<String, String> tmEvents)
	{
		String line;
		List<DataPoint> dps= new ArrayList<DataPoint>();
		for(int i=0;i<strTreeFiles.length;i++)
		{
			TreeMap<String,String> FileEventMap = new TreeMap<String,String>();
			List<String> Events = new ArrayList<String>();
			List<String> Words = new ArrayList<String>();
			 try
			 {
				BufferedReader input =  new BufferedReader(new FileReader(strTreePath+"/"+strTreeFiles[i].replace(".tree", ".list")));
				line ="";
		        while (( line = input.readLine()) != null)
		        {
		        	String[] tokens = line.split(" ");
		        	FileEventMap.put(tokens[1].replaceAll("[(,)]", ""), tmEvents.get(tokens[0]));
		        	Events.add(tokens[1].replaceAll("[(,)]", ""));
		        	//Words.add(tokens)
		        	//System.out.println(tokens[1]+"-"+tokens[0]+"-"+tmEvents.get(tokens[0]));
		        }
		        input.close();
			 }
			 catch (Exception e)
			 {
				e.printStackTrace();
			 }
			 TreeMap<String,String> FilelistMap = new TreeMap<String,String>();
			 String Parent="";
			 try
			 {
				BufferedReader tinput =  new BufferedReader(new FileReader(strTreePath+"/"+strTreeFiles[i]));
				line ="";
		        while (( line = tinput.readLine()) != null)
		        {
		        	String[] tokens = line.split(" ");
		        	if(tokens.length==1)
		        	{
		        		Parent = String.format("%s",tokens[0]);
		        		//System.out.println("Parent :" + tokens[0]);
		        	}
		        	else
		        	{
			        	FilelistMap.put(tokens[0], tokens[1]);
			        	//System.out.println(tokens[0]+"-"+tokens[1]);
		        	}
		        }
		        tinput.close();
			 }
			 catch (Exception e)
			 {
				e.printStackTrace();
			 }
			 TreeMap<String,List<String>> EventWithParent = IdentifyParents(FilelistMap,Events,Parent);//new TreeMap<String,List<String>>();
			 //System.out.println("Check ");
			 //System.out.println("FileEventMap.size()"+FileEventMap.size());
			 for(int j=0;j<Events.size();j++)
			 {
				 for(int k=j+1;k<Events.size();k++)
				 {
					 String e1 = Events.get(j);
					 String e2 = Events.get(k);
					 int Dist = CalcDistance(e1, e2, EventWithParent);
					 //System.out.println(e1+"-"+e2+"-"+Dist+"-"+FileEventMap.get(e1)+"-"+FileEventMap.get(e2));
					 dps.add(new DataPoint(FileEventMap.get(e1),FileEventMap.get(e2),Dist,i));
				 }
			 }
		}
		return dps;
	}

	private TreeMap<String, List<String>> IdentifyParents(
			TreeMap<String, String> filelistMap, List<String> events, String parent)
	{
		TreeMap<String, List<String>> map = new TreeMap<String, List<String>>();

		for(int i=0;i<events.size();i++)
		{
			List<String> Parents = new ArrayList<String>();
			String current = events.get(i);
			String temp = String.format("%s", current);
			//System.out.println(current +":");
			for(;!current.equals(parent);)
			{
				//System.out.println(current + "-" + parent);
				Parents.add(filelistMap.get(current));
				current = filelistMap.get(current);
			}
			//System.out.print(temp+":");
			//PrintListStr(Parents);
			map.put(temp, Parents);
		}

		return map;
	}

	private void PrintListStr(List<String> s)
	{
		for(int i=0;i<s.size();i++)
		{
			System.out.print(s.get(i)+" ");
		}
		System.out.println();
	}
	private int CalcDistance(String e1, String e2,
			TreeMap<String, List<String>> eventWithParent)
	{
		List<String> l1 = eventWithParent.get(e1);
		List<String> l2 = eventWithParent.get(e2);
		//System.out.print("l1:");PrintListStr(l1);
		//System.out.print("l2:");PrintListStr(l2);

		for(int i=0;i<l1.size();i++)
		{
			for(int j=0;j<l2.size();j++)
			{
				if(l1.get(i).equals(l2.get(j)))
				{
					return i+j+1;//max(i,j)+1;
				}
			}
		}
		return 0;
	}

	private int max(int i, int j)
	{
		// TODO Auto-generated method stub
		if(i>j)
			return i;
		else
			return j;

	}
*/
	public List<DataPoint> ParseTree1(String strTreePath,String[] strTreeFiles, TreeMap<String, String> tmEvents, String strListPath) {
		// TODO Auto-generated method stub
		ParentNo = tmEvents.size()+1;
		String line="";
		List<DataPoint> dps= new ArrayList<DataPoint>();
		for(int i=0;i<strTreeFiles.length;i++)
		{
			TreeMap<String,String> FileEventMap = new TreeMap<String,String>();
			List<String> Events = new ArrayList<String>();
			 try
			 {
				BufferedReader input =  new BufferedReader(new FileReader(strListPath+"/"+strTreeFiles[i].replace(".tree", ".list")));
				line ="";
		        while (( line = input.readLine()) != null)
		        {
		        	String[] tokens = line.split(" ");
		        	//System.out.println(tokens[1].replaceAll("[(,)]", ""));
		        	FileEventMap.put(tokens[1].replaceAll("[(,)]", ""), tokens[0]);
		        	Events.add(tokens[0]);
		        }
		        input.close();
			 }
			 catch (Exception e)
			 {
				e.printStackTrace();
			 }

			// System.out.println("Event Map is Done ");

			TreeMap<String,List<String>> ParentMap = new TreeMap<String,List<String>>();
			List<String> Parents = new ArrayList<String>();
			try
			 {
				BufferedReader tinput =  new BufferedReader(new FileReader(strTreePath+"/"+strTreeFiles[i]));
				line ="";
				String ParentName ="";
		        while (( line = tinput.readLine()) != null)
		        {
		        	String[] tokens = line.split(" ");
		        	if(tokens.length>1)
		        	{
		        		if(ParentMap.containsKey(tokens[1]))
		        		{
		        			List<String> temp = ParentMap.get(tokens[1]);
		        			if(FileEventMap.get(tokens[0])!=null)
		        				temp.add(FileEventMap.get(tokens[0]));
		        			else
		        				temp.add(tokens[0]);
		        		}
		        		else
		        		{
		        			List<String> temp = new ArrayList<String>();
		        			if(FileEventMap.get(tokens[0])!=null)
		        				temp.add(FileEventMap.get(tokens[0]));
		        			else
		        				temp.add(tokens[0]);
		        			ParentMap.put(tokens[1], temp);
		        			Parents.add(tokens[1]);
		        		}
		        	}
		        	else
		        	{
		        		ParentName = tokens[0];
		        	}
		        }
		        FileNo = i;
		        try
		        {
		        	System.out.println("Valid File : "+strTreeFiles[i]);
		        	AddtoMasterMap(ParentMap,MasterMap,Parents,Events,ParentName);
		        	dps  = CreateDataPoints(Events,dps,FileNo,strListPath+"/"+strTreeFiles[i].replace(".tree", ".html"));
		        }
		        catch(Exception e)
		        {
		        	System.out.println("Crash : "+strTreeFiles[i]);
		        	e.printStackTrace();
		        }
		        //else
		       // {
		        //	System.out.println("Parent Name Null : "+strTreeFiles[i]);
		        //}
		        tinput.close();
			 }
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		return dps;
	}

	private List<DataPoint> CreateDataPoints(List<String> events,
		List<DataPoint> dps, int fileNo2,String fileName)
		{
		List<String> File = new ArrayList<String>();
		String compFile ="";
		File = ReadFromFile(fileName);

	// TODO Auto-generated method stub
		for(int i=0;i<events.size();i++)
		{
			for(int j=i+1;j<events.size();j++)
			{
				if(MasterMap.containsKey(events.get(i)))
				{
					TreeMap<String,Feature> temp = MasterMap.get(events.get(i));

					if(temp.containsKey(events.get(j)))
					{
						Feature f = temp.get(events.get(j));
						int e1 = CheckNull(SynSetMap.get(events.get(i)));
						int e2 = CheckNull(SynSetMap.get(events.get(j)));
						Word w =CheckLine(events.get(i),events.get(j),File,compFile);
						dps.add(new DataPoint(new String(events.get(i)),new String(events.get(j)),Distance(events.get(i),events.get(j)),fileNo2,w.SameLine,w.WordDist,f.SynOffset,e1,e2,true,SynSetMap.get(events.get(i)),SynSetMap.get(events.get(j))));
					}
					else
					{
						Word w =CheckLine(events.get(i),events.get(j),File,compFile);
						dps.add(new DataPoint(new String(events.get(i)),new String(events.get(j)),Distance(events.get(i),events.get(j)),fileNo2,w.SameLine,w.WordDist,-99,-99,-99,false,null,null));
					}
				}
				else
				{
					//System.out.println("null insertion");
					Word w =CheckLine(events.get(i),events.get(j),File,compFile);
					dps.add(new DataPoint(new String(events.get(i)),new String(events.get(j)),Distance(events.get(i),events.get(j)),fileNo2,w.SameLine,w.WordDist,-99,-99,-99,false,null,null));
				}
			}
		}
	return dps;
}

	public List<String> ReadFromFile(String fileName) {

		String line;
		List<String> File = new ArrayList<String>();
		// TODO Auto-generated method stub
		try
		{
			BufferedReader input =  new BufferedReader(new FileReader(fileName));
			line ="";
	        while (( line = input.readLine()) != null)
	        {
	        	//compFile = compFile + line;
	        	//line = line.replace(".", " .");
	        	String[] temp = line.split("[.]");
	        	for(int i=0;i<temp.length;i++)
	        		File.add(temp[i]);
	        }
	        input.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return File;
	}

	public Word CheckLine(String string1, String string2, List<String> file, String compFile) {
		// TODO Auto-generated method stub
		boolean word = false;
		int dist =1000;
		for(int i=0;i<file.size();i++)
		{
			String temp = file.get(i);
			if(temp.contains(string1))
			{
				if(temp.contains(string2))
				{
					word = true;
					int d = WordDistance(temp,string1,string2);
					if(d<dist)
						dist = d;
				}

			}
		}

		return new Word(word,dist);
	}

	private int WordDistance(String temp, String string1, String string2)
	{
		// TODO Auto-generated method stub//<b>reported</b>[<font, color="green">e1</font>]
		temp = temp.replace("<b>","");
		temp = temp.replace("</b>"," ");
		List<String> words = Arrays.asList(temp.split(" "));
		int l1 = words.indexOf(string1);
		int l2 = words.indexOf(string2);
		//if(Math.abs(l1-l2)==0)
			//return 1000;
		//else
			return Math.abs(l1-l2);
	}

	private int Distance(String string1, String string2)
	{
		// TODO Auto-generated method stub
		int n1 , n2 ;
		//n1 = DepthMap.get(string1);
		if(DepthMap.get(string1)==null)
			n1 = 0;
		else
			n1 = DepthMap.get(string1);

		if(DepthMap.get(string2)==null)
			n2 = 0;
		else
			n2 = DepthMap.get(string2);
		return n1+n2;
	}

	private void AddtoMasterMap(TreeMap<String, List<String>> parentMap,
			TreeMap<String, TreeMap<String, Feature>> masterMap,
			List<String> parents, List<String> events, String parentName)
	{
		// TODO Auto-generated method stub
		cEvents = null;
		cEvents = events;
		DepthMap = null;
		DepthMap = new TreeMap<String,Integer>();
		SynSetMap = null;
		SynSetMap = new TreeMap<String,ISynsetID>();
		TreeTraversal(parentMap,parents,parentName,0);

		return;
	}

	private String TreeTraversal(
			TreeMap<String, List<String>> parentMap,
			List<String> parents, String parentName,int depth) {
		// TODO Auto-generated method stub
		String value = "";
		List<String> temp = parentMap.get(parentName);
		if(temp==null)
		{
			System.out.println("temp is null");
			System.out.println(parentName);
			return "0";
		}
		
		if(temp.size()<2)
		{
			System.out.println(temp.size());
			return "1";
		}
		else if(temp.size()==2)
		{
			String Event1 = temp.get(0);
			String Event2 = temp.get(1);
			value  = CreateEvents(Event1,Event2,parentMap,parents,depth);
			return value;
		}
		else
		{
			for(int i=0;i<temp.size();i++)
			{
				for(int j=i+1;j<temp.size();j++)
				{
					value = CreateEvents1(temp.get(i),temp.get(j),parentMap,parents,ParentNo,depth);
					//System.out.println(temp.get(i)+temp.get(j)+value);
				}
			}
			cEvents.add(new String(String.format("%d",ParentNo)));
			return new String(String.format("%s",ParentNo++));
		}
		//return value;
	}

	private String CreateEvents(String Event1, String Event2, TreeMap<String, List<String>> parentMap, List<String> parents,int depth) {
		// TODO Auto-generated method stub
		if(MasterMap.containsKey(Event1))
		{
			TreeMap<String,Feature> sample = MasterMap.get(Event1);

			if(sample.containsKey(Event2))
			{
				Feature f1  = sample.get(Event2);
				if(f1!=null)
				{
					f1.OccurancePP();
					f1.AddFileNo(FileNo);
					DepthMap.put(Event1, depth);
					DepthMap.put(Event2, depth);
					return f1.Parent;

				}
				else
				{
					DepthMap.put(Event1, depth);
					DepthMap.put(Event2, depth);
					return "";
				}
			}
		}

		if(cEvents.contains(Event1)&&cEvents.contains(Event2))
		{
			if(MasterMap.containsKey(Event1))
			{
				TreeMap<String,Feature> sample = MasterMap.get(Event1);

				if(sample.containsKey(Event2))
				{
					DepthMap.put(Event1, depth);
					DepthMap.put(Event2, depth);
					//dead code !
					// have to check
					Feature f1  = sample.get(Event2);
					f1.OccurancePP();
					f1.AddFileNo(FileNo);
					return f1.Parent;
					//Do nothing -> Future increase the count
				}
				else
				{
					DepthMap.put(Event1, depth);
					DepthMap.put(Event2, depth);
					//System.out.println(Event1+":"+Event2);
					ISynsetID n = GetSynSetID(Event1,Event2);
					SynSetMap.put(new String(String.format("%d",ParentNo)), n);
					int off = CheckNull(n);
					Feature f = new Feature(new String(String.format("%d",ParentNo)),0,ParentNo,off);
					sample.put(Event2, f);
					//sample.put(Event2,String.format("%d",ParentNo));
					cEvents.add(new String(String.format("%d",ParentNo++)));
				}
			}
			else
			{
				Create2Events(Event1,Event2,depth);
			}
			//check master create a name not present create a name
		}
		else if(!cEvents.contains(Event1)&&!cEvents.contains(Event2))
		{
			String e1 = TreeTraversal(parentMap,parents,Event1,depth+1);
			String e2 = TreeTraversal(parentMap,parents,Event2,depth+1);
			Create2Events(e1,e2,depth);

		}
		else if(!cEvents.contains(Event1))
		{
			String e1 = TreeTraversal(parentMap,parents,Event1,depth+1);
			Create2Events(e1,Event2,depth);
		}
		else if(!cEvents.contains(Event2))
		{
			String e2 = TreeTraversal(parentMap,parents,Event2,depth+1);
			Create2Events(Event1,e2,depth);
		}
		return new String(String.format("%d",ParentNo-1));

	}

	private String CreateEvents1(String Event1, String Event2, TreeMap<String, List<String>> parentMap, List<String> parents,int pno,int depth) {
		// TODO Auto-generated method stub

		if(cEvents.contains(Event1)&&cEvents.contains(Event2))
		{
			if(MasterMap.containsKey(Event1))
			{
				TreeMap<String,Feature> sample = MasterMap.get(Event1);

				if(MasterMap.containsKey(Event2))
				{
					DepthMap.put(Event1, depth);
					DepthMap.put(Event2, depth);
				}
				else
				{
					DepthMap.put(Event1, depth);
					DepthMap.put(Event2, depth);
					//System.out.println(Event1+":"+Event2)
					ISynsetID n = GetSynSetID(Event1,Event2);
					SynSetMap.put(new String(String.format("%d",pno)), n);
					int off = CheckNull(n);
					Feature f = new Feature(new String(String.format("%d",pno)),0,FileNo,off);
					sample.put(Event2, f);
				}
			}
			else
			{
				Create2Events1(Event1,Event2,pno,depth);
			}
			//check master create a name not present create a name
		}
		else if(!cEvents.contains(Event1)&&!cEvents.contains(Event2))
		{
			ParentNo++;
			String e1 = TreeTraversal(parentMap,parents,Event1,depth+1);
			ParentNo++;
			String e2 = TreeTraversal(parentMap,parents,Event2,depth+1);
			Create2Events1(e1,e2,pno,depth);

		}
		else if(!cEvents.contains(Event1))
		{
			ParentNo++;
			String e1 = TreeTraversal(parentMap,parents,Event1,depth+1);
			Create2Events1(e1,Event2,pno,depth);
		}
		else if(!cEvents.contains(Event2))
		{
			ParentNo++;
			String e2 = TreeTraversal(parentMap,parents,Event2,depth+1);
			Create2Events1(Event1,e2,pno,depth);
		}
		//cEvents.add(new String(String.format("%d",pno)));
		//ParentNo++;
		return new String(String.format("%d",pno));

	}


	private void Create2Events(String event1, String event2,int depth) {
		// TODO Auto-generated method stub
		//creating 2 events
		//if(!CheckMasterMap(event1,event2))
		{
			DepthMap.put(event1, depth);
			DepthMap.put(event2, depth);
			TreeMap<String,Feature> sample = new TreeMap<String,Feature>();
			ISynsetID n = GetSynSetID(event1,event2);
			SynSetMap.put(new String(String.format("%d",ParentNo)), n);
			int off = CheckNull(n);
			Feature f = new Feature(new String(String.format("%d",ParentNo)),0,FileNo,off);
			sample.put(event2,f);
			cEvents.add(new String(String.format("%d",ParentNo++)));
			MasterMap.put(event1, sample);
		}
	}

	private int CheckNull(ISynsetID n) {
		// TODO Auto-generated method stub
		if(n==null)
			return -99;
		return n.getOffset();
	}

	private ISynsetID GetSynSetID(String event1, String event2) 
	{
		// TODO Auto-generated method stub
		
		ISynsetID s1,s2,Fin;
		if(SynSetMap.containsKey(event1))
		{
			s1 = SynSetMap.get(event1);
		}
		else
		{
			s1 = s.getSynID(event1);
			SynSetMap.put(event1, s1);
		}
		if(SynSetMap.containsKey(event2))
		{
			s2 = SynSetMap.get(event2);
		}
		else
		{
			s2 = s.getSynID(event2);
			SynSetMap.put(event2, s2);
		}
		Fin = s.getRelatedSysnsets(s1, s2);
		return Fin;
	}

	private void Create2Events1(String event1, String event2, int PNO,int depth) {
		// TODO Auto-generated method stub
		//creating 2 events
		//if(!CheckMasterMap(event1,event2))
		{
			DepthMap.put(event1, depth);
			DepthMap.put(event2, depth);
			ISynsetID n = GetSynSetID(event1,event2);
			SynSetMap.put(new String(String.format("%d",PNO)), n);
			TreeMap<String,Feature> sample = new TreeMap<String,Feature>();
			int off = CheckNull(n);
			Feature f = new Feature(new String(String.format("%d",PNO)),0,FileNo,off);
			sample.put(event2,f);
			//cEvents.add(new String(String.format("%d",ParentNo)));
			MasterMap.put(event1, sample);
		}
	}

	public int getParentNo() {
		// TODO Auto-generated method stub
		return ParentNo;
	}
}