import java.util.ArrayList;
import java.util.List;

/* A new Data structure defined to perform ASTAR and Greedy on a loop */
public class WeightedState 
{
	int[][] mat;//the state of the matrix
	int weight;// the weight corresponding to the goal and heuristic
	List<String> steps;//the steps that are performed to reach that particular state from the input state
	Helper Help = new Helper();
	

	public WeightedState(int[][] move, WeightedState ws, Direction Dir, int temp, Search s) 
	{
		// TODO Auto-generated constructor stub
		steps = new ArrayList<String>();
		mat = new int[move.length][move.length];
		Help.Copy(mat, move);
		Help.AddListContennts(steps,ws.steps);
		steps.add(Dir.toString());
		if (s == Search.Astar)
			weight = steps.size()+temp;
		else
			weight = temp;
		
	}

	public WeightedState(int[][] ip) {
		// TODO Auto-generated constructor stub
		steps = new ArrayList<String>();
		mat = new int[ip.length][ip.length];
		Help.Copy(mat, ip);
		weight = 100000;
	}

	//Finding the  next best state given a list of Explores states
	public int getBestState(List<WeightedState> exploredStates) {
		// TODO Auto-generated method stub
		
		int temp = 1000000;
		int index =0;
		
		for(int i=0;i<exploredStates.size();i++)
		{
			WeightedState ws = exploredStates.get(i);
			if(ws.weight<temp)
			{
				temp = ws.weight;
				index = i;
			}
		}
		
		return index;
	}

	public void print() {
		// TODO Auto-generated method stub
		//Help.Print(mat);
		//System.out.println(weight);
	}
}
