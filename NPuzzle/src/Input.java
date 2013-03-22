
/*Input file for the Program*/

public class Input {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		Puzzle P = new Puzzle();
		Helper help = new Helper();
		Solvability solvable = new Solvability();
		String[] heuristics = {"Misplaced Tiles","Manhattan Distance","Manhattan Distance with Linear Conflict","Nilssons Sequence Score"};	
		//int[][] ip = {{5,1,2},{7,4,6},{0,8,3}};
		//int[][] ip = {{1,3,6},{5,0,2},{4,7,8}};
		//int[][] op = {{1,2,3},{4,5,6},{7,8,0}};

		int ip [][] = {{12,1,10,2},{7,11,4,14},{5,0,9,15},{8,13,6,3}};
		//int ip [][] = {{1,6,2,4},{5,10,3,8},{9,0,7,12},{13,14,11,15}};
		//int ip [][] = {{1,2,3,4},{5,6,7,8},{9,10,15,11},{13,14,0,12}};
		//int ip [][] = {{1,0,2,4},{5,6,3,7},{9,10,11,8},{13,14,15,12}};
		int op [][] = {{1,2,3,4},{5,6,7,8},{9,10,11,12},{13,14,15,0}};
		
		if(solvable.IsSolvable(help.Linearize(ip))&&help.ValidateInput(ip, op))
		{
			
			for(int i=0;i<4;i++)
			{
				int[][] ip1 = new int[ip.length][ip.length];
				int[][] op1 = new int[ip.length][ip.length];
				help.Copy(ip1, ip);
				help.Copy(op1, op);
				System.out.println(heuristics[i]);
				System.out.println("AStar");
				P.CalcAStar(ip1,op1,i);
				
				int[][] ip2 = new int[ip.length][ip.length];
				int[][] op2 = new int[ip.length][ip.length];
				help.Copy(ip2, ip);
				help.Copy(op2, op);
				System.out.println("Greedy");
				P.CalcGreedy(ip2, op2,i);
			}
			//P.CalcAStar(ip,op,3);
			System.out.println("Completed");
		}
		else
		{
			System.out.println("The Problem is not Solvable or the Input is not Valid !");
		}
	}

}
