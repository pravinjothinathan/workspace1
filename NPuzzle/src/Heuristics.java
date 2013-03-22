import java.util.ArrayList;
import java.util.List;

/*Contains all the heuristics and its ssupport Functions */
public class Heuristics {
	
	Helper help;
	
	public Heuristics()
	{
		help = new Helper();
	}

	//Calculating the Manhattan Distance
	public int ManhattanDistance(int[][] ip, int[][] op) {
		int count = 0;
		int size = (ip.length) * (ip.length);
		for(int i=0;i<size;i++)
		{
			Point ipZeroPos = help.Find(ip,i);
			Point opZeroPos = help.Find(op,i);
			count += EQDist(ipZeroPos,opZeroPos);
		}
		return count;
	}
	
	//Calculating the absolute difference between two points
	private int EQDist(Point ipZeroPos, Point opZeroPos) {
		// TODO Auto-generated method stub
		return Math.abs(ipZeroPos.x-opZeroPos.x)+Math.abs(ipZeroPos.y-opZeroPos.y);
		//return 0;
	}

	//Resolving liner conflicts there by creating new count 
	public int LinerConflict(int[][] ip, int[][] op) {
		// TODO Auto-generated method stub
		int count=0;
		for(int i=0;i<ip.length;i++)
		{
			int[] x = copyiRow(ip,i);
			List<Integer> PresentinGoalState = GetGoalStateEntries(x,i,op);
			if(PresentinGoalState!=null)
			{
				for(int j=0;j<PresentinGoalState.size()-1;j++)
				{
					if(PresentinGoalState.get(j)<PresentinGoalState.get(j+1))
						count = count + 2;
				}
			}
		}
		return count;
	}

	//helper function for liner conflicts
	private List<Integer> GetGoalStateEntries(int[] x, int i1, int[][] op) 
	{
		// TODO Auto-generated method stub
		List<Integer> vals = new ArrayList<Integer>();
		for(int i=0;i<x.length;i++)
		{
			for(int j=0;j<x.length;j++)
			{
				if(x[i]==op[i1][j])
					vals.add(x[i]);
			}
		}
		return vals;
	}

	//helper function for linear conflict
	private int[] copyiRow(int[][] ip, int i1) 
	{
		int [] x = new int[ip.length];
		for(int i=0;i<ip.length;i++)
			x[i]=ip[i][i];
		// TODO Auto-generated method stub
		return x;
	}

	// basic heuristic of misplaced tiles -> un matched tiles
	public int MisplacedTiles(int[][] ip, int[][] op) {
		// TODO Auto-generated method stub
		int count = 0;
		int Rows = ip.length;

		for(int i=0;i<Rows;i++)
			for(int j=0;j<Rows;j++)
				if(ip[i][j]==op[i][j])
					count++;

		return count;
	}

	// Nilsson's sequence score 
	public int Sequence(int[][] ip, int[][] op) {
		// TODO Auto-generated method stub
		int count = 0;
		
		for(int i=1;i<ip.length-1;i++)
		{
			for(int j=1;j<ip.length-1;j++)
			{
				count += MisplacedNeighbours(ip,op,i,j);
			}
		}
		return count;
	}

	//calculating for each value
	private int MisplacedNeighbours(int[][] ip, int[][] op, int i, int j) {
		// TODO Auto-generated method stub
		int count =0;
		int[] pos = {-1,0,1};
		for(int a=0;a<3;a++)
		{
			for(int b=0;b<3;b++)
			{
				
				if(ip[i+pos[a]][j+pos[b]]!=op[i+pos[a]][j+pos[b]])
				{
					if(pos[a]==0&&pos[b]==0)
						count = count + 1;
					else
						count = count +2;
				}
			}
		}
		return count;
	}
	
	
	
}
