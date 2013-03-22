import java.util.ArrayList;
import java.util.List;

/* the Search and the heuristic implementations are done here */

enum Direction {left,right,top,down};
enum Search {Greedy,Astar};

public class Puzzle 
{
	Heuristics Heur;
	Helper Help;
	//Contains the list of Explored state but not expanded
	List<WeightedState> ExploredStates;
	//contains the list of traversed states
	List<int[][]> VisitedStates;
	//Stack<int[][]> NotExpandedStates;
	int steps = 0;
	
	public Puzzle()
	{
		ExploredStates = new ArrayList<WeightedState>();
		VisitedStates =new ArrayList<int[][]>();
		//NotExpandedStates = new Stack<int[][]>();
		Heur = new Heuristics();
		Help = new Helper();
	}
	
	//Selection of the heuristic is done here 
	private int CalcDistance(int[][] ip, int[][] op,int heuristic) 
	{
		// TODO Auto-generated method stub
		int count=0;
		switch(heuristic)
		{
			case 0:
				//Misplaced tiles count
				count = Heur.MisplacedTiles(ip,op);
				break;
			case 1:
				//Manhattan Distance
				count = Heur.ManhattanDistance(ip,op);
				break;
			case 2:
				//Manhattan Distance -> Adding Linear 
				count = Heur.ManhattanDistance(ip,op);
				count += Heur.LinerConflict(ip,op);
				break;
			case 3:
				//Nilsson's Sequence
				count = Heur.ManhattanDistance(ip,op);
				count += 3 * Heur.Sequence(ip,op);
		}
		return count;
	}

	//Checking whether the particular state is already visited 
	// Just pruning the search space if the state is already visited
	private boolean CheckVisited(List<int[][]> visitedStates2, int[][] mat) 
	{
		// TODO Auto-generated method stub
		for(int i=0;i<visitedStates2.size();i++)
		{
			if(Help.Compare(visitedStates2.get(i),mat)==true)
				return true;
		}
		return false;
	}
	
	//The 4 possible transitions
	//Down
	private int[][] MoveDown(int[][] ip1, Point zeroPos,int n) 
	{
		int[][] ip = new int[ip1.length][ip1.length];
		Help.Copy(ip,ip1);
		ip[zeroPos.x][zeroPos.y] = ip[zeroPos.x+1][zeroPos.y];
		ip[zeroPos.x+1][zeroPos.y] = n;
		return ip;
	}

	//up
	private int[][] MoveUp(int[][] ip1, Point zeroPos,int n) 
	{
		int[][] ip = new int[ip1.length][ip1.length];
		Help.Copy(ip,ip1);
		ip[zeroPos.x][zeroPos.y] = ip[zeroPos.x-1][zeroPos.y];
		ip[zeroPos.x-1][zeroPos.y] = n;
		return ip;
	}

	//right
	private int[][] Moveright(int[][] ip1, Point zeroPos,int n) 
	{
		int[][] ip = new int[ip1.length][ip1.length];
		Help.Copy(ip,ip1);
		ip[zeroPos.x][zeroPos.y] = ip[zeroPos.x][zeroPos.y+1];
		ip[zeroPos.x][zeroPos.y+1] = n;
		return ip;
	}

	//left
	private int[][] Moveleft(int[][] ip1, Point zeroPos, int n) 
	{
		int[][] ip = new int[ip1.length][ip1.length];
		Help.Copy(ip,ip1);
		ip[zeroPos.x][zeroPos.y] = ip[zeroPos.x][zeroPos.y-1];
		ip[zeroPos.x][zeroPos.y-1] = n;
		return ip;	
	}
	
	//The Greedy Search -> customized for the NTiles problem
	public void CalcGreedy(int[][] ip, int[][] op, int heu) 
	{
		VisitedStates.clear();
		ExploredStates.clear();
		int size = ip.length;
		WeightedState Ws = new WeightedState(ip);
		int steps =0 ;
		for(;;)
		{
			if(steps>5000)
			{
				System.out.println("Crossed 5000 Iterations!");
				break;
			}
			steps++;
			VisitedStates.add(Ws.mat);
			if(!Help.Compare(Ws.mat, op))
			{
				Point ZeroPos = Help.Find(Ws.mat, 0);
				//move left
				if(ZeroPos.y>0&&!CheckVisited(VisitedStates,Moveleft(Ws.mat,ZeroPos,0)))
				{
					int temp = CalcDistance(Moveleft(Ws.mat,ZeroPos,0),op,heu);
					WeightedState left = new WeightedState(Moveleft(Ws.mat,ZeroPos,0), Ws,Direction.left,temp,Search.Greedy);
					left.print();
					ExploredStates.add(left);
				}
				//move right
				if(ZeroPos.y<size-1&&!CheckVisited(VisitedStates,Moveright(Ws.mat,ZeroPos,0)))
				{
					int temp = CalcDistance(Moveright(Ws.mat,ZeroPos,0),op,heu);
					WeightedState right = new WeightedState(Moveright(Ws.mat,ZeroPos,0), Ws,Direction.right,temp,Search.Greedy);
					right.print();
					ExploredStates.add(right);
				}
				//move up
				if(ZeroPos.x>0&&!CheckVisited(VisitedStates,MoveUp(Ws.mat,ZeroPos,0)))
				{
					int temp = CalcDistance(MoveUp(Ws.mat,ZeroPos,0),op,heu);
					WeightedState up = new WeightedState(MoveUp(Ws.mat,ZeroPos,0), Ws,Direction.top,temp,Search.Greedy);
					up.print();
					ExploredStates.add(up);
				}
				//move down
				if(ZeroPos.x<size-1&&!CheckVisited(VisitedStates,MoveDown(Ws.mat,ZeroPos,0)))
				{
					int temp = CalcDistance(MoveDown(Ws.mat,ZeroPos,0),op,heu);
					WeightedState down = new WeightedState(MoveDown(Ws.mat,ZeroPos,0), Ws,Direction.down,temp,Search.Greedy);
					down.print();
					ExploredStates.add(down);
				}
				
				//Find the Best Expored State
				if(ExploredStates.size()==0)
				{
					System.out.println("No Solution Found");
					break;
				}
				else
				{
					int index = Ws.getBestState(ExploredStates);
					Ws = ExploredStates.get(index);
					ExploredStates.remove(index);
				}
			}
			else
			{
				System.out.println("Solution Achieved!!");
				//Help.printList(Ws.steps);
				System.out.println("Steps to Goal -"+Ws.steps.size());
				System.out.println("No os Steps -"+steps);
				break;
				//Print Steps
			}
		}
	}


	//The AStar Search -> Customized for the NTiles problem
	public void CalcAStar(int[][] ip, int[][] op, int heu) 
	{
		VisitedStates.clear();
		ExploredStates.clear();
		int size = ip.length;
		WeightedState Ws = new WeightedState(ip);
		int steps = 0;
		for(;;)
		{
			if(steps>5000)
			{
				System.out.println("Crossed 5000 Iterations!");
				break;
			}
			steps++;
			VisitedStates.add(Ws.mat);
			//System.out.println("Input");
			//Help.Print(Ws.mat);
			if(!Help.Compare(Ws.mat, op))
			{
				Point ZeroPos = Help.Find(Ws.mat, 0);
				//move left
				if(ZeroPos.y>0&&!CheckVisited(VisitedStates,Moveleft(Ws.mat,ZeroPos,0)))
				{
					int temp = CalcDistance(Moveleft(Ws.mat,ZeroPos,0),op,heu);
					WeightedState left = new WeightedState(Moveleft(Ws.mat,ZeroPos,0), Ws,Direction.left,temp,Search.Astar);
					left.print();
					ExploredStates.add(left);
				}
				//move right
				if(ZeroPos.y<size-1&&!CheckVisited(VisitedStates,Moveright(Ws.mat,ZeroPos,0)))
				{
					int temp = CalcDistance(Moveright(Ws.mat,ZeroPos,0),op,heu);
					WeightedState right = new WeightedState(Moveright(Ws.mat,ZeroPos,0), Ws,Direction.right,temp,Search.Astar);
					right.print();
					ExploredStates.add(right);
				}
				//move up
				if(ZeroPos.x>0&&!CheckVisited(VisitedStates,MoveUp(Ws.mat,ZeroPos,0)))
				{
					int temp = CalcDistance(MoveUp(Ws.mat,ZeroPos,0),op,heu);
					WeightedState up = new WeightedState(MoveUp(Ws.mat,ZeroPos,0), Ws,Direction.top,temp,Search.Astar);
					up.print();
					ExploredStates.add(up);
				}
				//move down
				if(ZeroPos.x<size-1&&!CheckVisited(VisitedStates,MoveDown(Ws.mat,ZeroPos,0)))
				{
					int temp = CalcDistance(MoveDown(Ws.mat,ZeroPos,0),op,heu);
					WeightedState down = new WeightedState(MoveDown(Ws.mat,ZeroPos,0), Ws,Direction.down,temp,Search.Astar);
					down.print();
					ExploredStates.add(down);
				}
				
				//Find the Best Expored State
				if(ExploredStates.size()==0)
				{
					System.out.println("No Solution Found");
					break;
				}
				else
				{
					int index = Ws.getBestState(ExploredStates);
					Ws = ExploredStates.get(index);
					ExploredStates.remove(index);
				}
			}
			else
			{
				System.out.println("Solution Achieved!!");
				///Help.printList(Ws.steps);
				System.out.println("Steps to Goal -"+Ws.steps.size());
				System.out.println("No os Steps -"+steps);
				break;
				//Print Steps
			}
		}
	}
}
