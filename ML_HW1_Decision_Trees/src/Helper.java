import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;


public class Helper {
	
	public boolean ClassifiedAll(int[][] x,int col)
	{
		if(x.length==1)
			return true;
		
		int temp =0;
		
		for(int i=0;i<x.length;i++)
		{
			if(i==0)
				temp = x[i][col];
			if(temp!=x[i][col])
				return false;
		}
		return true;
	}
	


	public void Print(int[][] x)
	{
		for(int i=0;i<x.length;i++)
		{
			for(int j=0;j<x[i].length;j++)
			{
				System.out.print(x[i][j]+" ");
			}
			System.out.println();
		}
	}
	
	
	public int[][] ZeroSplitonData(int[][] input,int col)
	{
		int counter = 0;
		for(int i=0;i<input.length;i++)
		{
			if(input[i][col]==0)
				counter++;
		}
		int[][] n = new int[counter][input[0].length];
		int l=0;
		for(int i=0;i<input.length;i++)
		{
			if(input[i][col]==0)
			{
				for(int j=0;j<input[i].length;j++)
				{
					n[l][j] = input[i][j];
				}
				l++;
			}
		}
		return n;
	}
	
	public int[][] OneSplitonData(int[][] input,int col)
	{
		int counter = 0;
		for(int i=0;i<input.length;i++)
		{
			if(input[i][col]==1)
				counter++;
		}
		int[][] n = new int[counter][input[0].length];
		int l=0;
		for(int i=0;i<input.length;i++)
		{
			if(input[i][col]==1)
			{
				for(int j=0;j<input[i].length;j++)
				{
					n[l][j] = input[i][j];
				}
				l++;
			}
		}
		return n;
	}



	public int Max(List<Double> vals) {
		// TODO Auto-generated method stub
		double max = 0;
		int maxindex = 0;
		for(int i=0;i<vals.size();i++)
		{
			if(vals.get(i)>max)
			{
				max = vals.get(i);
				maxindex=i;
			}
		}
		return maxindex;
	}
	
	public int Min(List<Integer> vals) {
		// TODO Auto-generated method stub
		int min = 1000000;
		int minindex = 0;
		for(int i=0;i<vals.size();i++)
		{
			if(vals.get(i)<min)
			{
				min = vals.get(i);
				minindex=i;
			}
		}
		return minindex;
	}
	
	public Object DeepCopy(Node original)
	{
		  ObjectInputStream ois;
		  ObjectOutputStream oos;
		  ByteArrayInputStream bais;
		  ByteArrayOutputStream baos;
		  byte [] data;
		  Object copy;
		
		  try
		  {
		  // write object to bytes
		  baos = new ByteArrayOutputStream();
		  oos = new ObjectOutputStream(baos);
		  oos.writeObject(original);
		  oos.close();
		
		  // get the bytes
		  data = baos.toByteArray();
		
		  // construct an object from the bytes
		  bais = new ByteArrayInputStream(data);
		  ois = new ObjectInputStream(bais);
		  copy = ois.readObject();
		  ois.close();
		  return copy;
		  }
		  catch(Exception e)
		  {
			  System.out.println("Exception :"+e.getMessage());
		  }
		 	
		  return null;
	}
}