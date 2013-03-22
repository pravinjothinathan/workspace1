/* Addressing the Problem of Solvability*/
public class Solvability 
{
	
	Helper Help ;
	
	public Solvability()
	{
		Help = new Helper();
	}
	
	//Checking whether the given input matrix is solvable or not
	public boolean IsSolvable(int[] ip) 
	{
		// TODO Auto-generated method stub
		//count is nothing but the no of inversions required for deciding whether the given input is solvable or not !
		int count = 0;
		for(int i=0;i<ip.length;i++)
		{
			int temp = ip[i];
			if(temp!=0)
			{
				for(int j=i+1;j<ip.length;j++)
				{
					if(ip[j]<temp&&ip[j]!=0)
						count++;
				}
			}
			//System.out.println("c:"+count);
		}
		//System.out.println("Inversions"+count);
		
		//Decision Part
		//if the grid is even
		if(ip.length%2==0)
		{
			Point zeroPos = Help.Find(Help.Conv2Matrix(ip),0);
			if((zeroPos.x)%2==0)//even row
			{
				if(count%2!=0)
					return true;
			}
			else//odd row
			{
				if(count%2==0)
					return true;
			}
		}
		else//if the grid is odd
		{
			//no of inversions is even
			if(count%2==0)
				return true;
		}
		return false;
	}

}
