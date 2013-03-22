/* A Randomizer with all the move statements*/


public class PGenerator 
{
	Helper Help;
	public PGenerator() {
		// TODO Auto-generated constructor stub
		Help = new Helper();
	}

	public int[][] Generate(int size, Difficulty Diff) {
		// TODO Auto-generated method stub
		int[][] Prob = new int[size][size];
		int i=1;

		for(int j=0;j<size;j++)
		{
			for(int k=0;k<size;k++)
			{
				Prob[j][k]=i++;
			}
		}
		Prob[size-1][size-1]=0;
		
		//loops till the difficulty level no of iterations are performed
		//could have checked for visited states but did not 
		// it would have made the Generator perfect
		for(i=0;i<Diff.index;i++)
		{
			int op = (int)Math.round(Math.random()*1000)%4;
			
			Point ZeroPos= Help.Find(Prob, 0);
			
			switch(op)
			{
			case 0:
				//move left
				if(ZeroPos.y>0)
				{
					Prob = Moveleft(Prob,ZeroPos,0);
				}
				else
				{
					i--;
				}
				break;
			case 1:
				//move right
				if(ZeroPos.y<size-1)
				{
					Prob = Moveright(Prob,ZeroPos,0);
				}
				else
				{
					i--;
				}
				break;
			case 2:
				//move up
				if(ZeroPos.x>0)
				{
					Prob = MoveUp(Prob,ZeroPos,0);
				}
				else
					i--;
				break;
			case 3:
				//move down
				if(ZeroPos.x<size-1)
				{
					Prob = MoveDown(Prob,ZeroPos,0);
				}
				else
					i--;
				break;
				default:
					break;
			}
		}
		
		return Prob;
	}
		
	// the transition functions
	private int[][] MoveDown(int[][] ip1, Point zeroPos,int n) 
	{
		int[][] ip = new int[ip1.length][ip1.length];
		Help.Copy(ip,ip1);
		ip[zeroPos.x][zeroPos.y] = ip[zeroPos.x+1][zeroPos.y];
		ip[zeroPos.x+1][zeroPos.y] = n;
		return ip;
	}

	private int[][] MoveUp(int[][] ip1, Point zeroPos,int n) 
	{
		int[][] ip = new int[ip1.length][ip1.length];
		Help.Copy(ip,ip1);
		ip[zeroPos.x][zeroPos.y] = ip[zeroPos.x-1][zeroPos.y];
		ip[zeroPos.x-1][zeroPos.y] = n;
		return ip;
	}

	private int[][] Moveright(int[][] ip1, Point zeroPos,int n) 
	{
		int[][] ip = new int[ip1.length][ip1.length];
		Help.Copy(ip,ip1);
		ip[zeroPos.x][zeroPos.y] = ip[zeroPos.x][zeroPos.y+1];
		ip[zeroPos.x][zeroPos.y+1] = n;
		return ip;
	}

	private int[][] Moveleft(int[][] ip1, Point zeroPos, int n) 
	{
		int[][] ip = new int[ip1.length][ip1.length];
		Help.Copy(ip,ip1);
		ip[zeroPos.x][zeroPos.y] = ip[zeroPos.x][zeroPos.y-1];
		ip[zeroPos.x][zeroPos.y-1] = n;
		return ip;	
	}

	
}
