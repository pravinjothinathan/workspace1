/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//package svmcluster;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Feature implements Serializable
{
	int Occurance;
	int Distance;
	int SynOffset;
	List<Integer> FileNo;
	String Parent;


	public Feature(String Name,int Dist,int File,int off)
	{
		Parent = new String(Name);
		FileNo = new ArrayList<Integer>();
		Distance = Dist;
		Occurance = File;
		SynOffset = off;
	}

	//Adding the no of occurances
	public void OccurancePP()
	{
		Occurance++;
	}

	public void AddFileNo(int x)
	{
		FileNo.add(x);
	}
}
