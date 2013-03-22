/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

///package svmcluster;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.TreeMap;

import edu.mit.jwi.item.ISynsetID;


public class DataPoint
{
	String Event1;
	String Event2;
	int Distance;
	int DocNo;
	boolean Classification;
	int WordDistance;
	boolean SameLine;
	int Syn1;
	int Syn2;
	int Offset;
	ISynsetID SS1;
	ISynsetID SS2;
	

	public DataPoint(String E1,String E2,int Dist,int Doc,boolean line,int WordDist,int Off,int s1,int s2,boolean classify,ISynsetID ss1,ISynsetID ss2)
	{
		Event1 = E1;
		Event2 = E2;
		Distance = Dist;
		DocNo = Doc;
		SameLine = line;
		WordDistance = WordDist;
		Syn1 = s1;
		Syn2 = s2;
		Offset = Off;
		Classification = classify;
		SS1 = ss1;
		SS2 = ss2;
	}

	public void Print()
	{
		if(Classification==true)
		System.out.println(Event1+","+Event2+","+Distance+","+DocNo+","+SameLine+","+WordDistance+","+Offset+","+Syn1+","+Syn2+","+Classification);
	}

	public void PrintFile(BufferedWriter output)
	{
		// TODO Auto-generated method stub
		try {
			output.write(Event1+","+Event2+","+Distance+","+DocNo+","+Classification+"\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void PrintFile(BufferedWriter output,
			TreeMap<String, String> tmEvents) {
		// TODO Auto-generated method stub
		try {
			String e1 = tmEvents.get(Event1);
			if(e1 == null)
				e1 = Event1;
			String e2 = tmEvents.get(Event2);
			if(e2 == null)
				e2 = Event2;
			output.write(Integer.parseInt(e1)+","+Integer.parseInt(e2)+","+Distance+","+DocNo+","+Classification+"\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void PrintFile(BufferedWriter output,
			TreeMap<String, String> tmEvents,int ParentNo,int totDocs, Synsets sn) throws Exception {
		// TODO Auto-generated method stub
		try {
			String e1 = tmEvents.get(Event1);
			if(e1 == null)
				e1 = Event1;
			String e2 = tmEvents.get(Event2);
			if(e2 == null)
				e2 = Event2;
                        
                        Double sim;
                        String simi;
                        sim = sn.getLCSimilarity(SS1, SS2);
                        simi = Double.toString(sim);
                        
			output.write(Integer.parseInt(e1)+","+Integer.parseInt(e2)+","+Distance+","+DocNo+","+SameLine+","+WordDistance+","+Replace(Offset)+","+Replace(sn.getSynoffset(Event1))+","+Replace(sn.getSynoffset(Event2))+","+simi+","+Classification+"\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String Replace(int synoffset) {
		// TODO Auto-generated method stub
		if(synoffset==-99)
			return "?";
		else
			return new String(String.format("%d",synoffset));
	}
}
