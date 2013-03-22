import java.util.List;

/*the same helper files as in NPUZZLE*/

public class Helper {
	
	//just validates the matrices are of equal size and of size more than 1
	public boolean ValidateInput(int[][]ip, int[][]op)
	{
		if(ip==null||op==null)
			return false;
			
		if(ip.length==op.length)
		{
			if(ip.length>1)
			{
				if(ip[0].length!=op[0].length)
					return false;
			}
			else
				return false;
		}
		else
			return false;
						
			return true;
		}
	
	public Point Find(int[][] ip,int n)
	{
		int Rows = ip.length;
		int Cols = ip[0].length;
		for(int i=0;i<Rows;i++)
		{
			for(int j=0;j<Cols;j++)
			{
				if(ip[i][j]==n)
					return new Point(i,j);
			}
		}
		return null;
	}
	
	public int[][] Conv2Matrix(int[] ip) 
	{
		// TODO Auto-generated method stub
		int count = (int)Math.pow(ip.length,0.5);
		int[][] matrix = new int[count][count];
		int l=0;
		for(int i=0;i<count;i++)
		{
			for(int j=0;j<count;j++)
			{
				matrix[i][j] = ip[l++];
			}
		}
		return matrix;
	}

	public int[] Linearize(int[][] ip) {
		// TODO Auto-generated method stub
		int[] temp = new int[ip.length*ip.length];
		int l = 0;
		int Rows = ip.length;
		for(int i=0;i<Rows;i++)
		{
			for(int j=0;j<Rows;j++)
			{
				//System.out.println(l);
				temp[l]= ip[i][j];
				l++;
			}
		}
		return temp;
	}
	
	public void Print(int[][] ip)
	{
		int Rows = ip.length;
		for(int i=0;i<Rows;i++)
		{
			for(int j=0;j<Rows;j++)
			{
				System.out.print(ip[i][j]+" ");
			}
		System.out.println();
		}
		return;
	}
	
	public boolean Compare(int[][]ip, int[][]op)
	{
		int Rows = ip.length;
		for(int i=0;i<Rows;i++)
			for(int j=0;j<Rows;j++)
				if(ip[i][j]!=op[i][j])
					return false;
		return true;
	}
	
	public void Copy(int[][] ip, int[][] ip1) 
	{
		int Rows = ip.length;
		int Cols = ip[0].length;
		for(int i=0;i<Rows;i++)
			for(int j=0;j<Cols;j++)
				ip[i][j]=ip1[i][j];
		
	}

	public void AddListContennts(List<String> steps, List<String> steps2) {
		for(int i=0;i<steps2.size();i++)
			steps.add(new String(steps2.get(i)));
	}

	public void printList(List<String> steps) {
		// TODO Auto-generated method stub
		
		for(int i=0;i<steps.size();i++)
			System.out.println(steps.get(i));
		
	}

}
