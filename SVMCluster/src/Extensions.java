/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//package svmcluster;
import java.io.File;
import java.io.FilenameFilter;

public class Extensions implements FilenameFilter
{
	String value;

	public Extensions(String strValue)
	{
		value = strValue;
	}

	@Override
	public boolean accept(File dir, String name)
	{
		return name.contains(value);
	}
}