/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//package svmcluster;

public class Event {
	String text;
	int count;

	public Event(String Val)
	{
		text = Val;
		count = 1;
	}

	public void incrementCount()
	{
		count++;
	}

	public int count()
	{
		return count;
	}

	public void print()
	{
		System.out.println(String.format("%15s %d", text,count));
	}
}
