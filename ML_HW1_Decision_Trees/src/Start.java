import java.util.ArrayList;
import java.util.List;


public class Start {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		

		FileHandler FH = new FileHandler();
		ParseData Parser = new ParseData();
		
		List<Node> Treees = new ArrayList<Node>(); 
		List<Double> TestPercentages = new ArrayList<Double>();
		//commad line arguments
		int L= Integer.parseInt(args[0]);
		int K= Integer.parseInt(args[1]);;
		String str_train_set = FH.FetchData(args[2]);
		String val_set = FH.FetchData(args[3]);
		String test_set = FH.FetchData(args[4]);
		String Print = args[5];
		//System.out.print(s);
		for(int i=0;i<3;i++)
		{
			Parser.parse(str_train_set);
			int[][] training_set = Parser.GetMatrix();
			String[] Headings = Parser.GetColHeaders();
			Node t = Classify(training_set,Headings.length-1,Headings,i);
			
			Traverse(t, -1);
			//pruning on validation set
			ParseData Parser1 = new ParseData();
			Parser1.parse(val_set);
			int[][] val_set_mat = Parser1.GetMatrix();
			Node NewTree = PostPrune(t,L,K,val_set_mat);
			
			//Accuracy on test_set
			ParseData ParserTestSet = new ParseData();
			ParserTestSet.parse(test_set);
			int[][] test_set_mat = ParserTestSet.GetMatrix();
			double valPercentage = ValidateDecisionTree(NewTree,test_set_mat);
			
			//System.out.println("test Percentage :"+ valPercentage);
			TestPercentages.add(valPercentage);
			Treees.add(NewTree);
			//Traverse(NewTree, -1);
		}
		if(Print.equals("yes"))
		{
			for(int i=0;i<3;i++)
			{
				System.out.println("Tree with Heuristic i : "+(i+1));
				Traverse(Treees.get(i), -1);
			}
		}
		
		for(int i=0;i<3;i++)
		{
			System.out.println("Test Percentage with Heuristic i = "+TestPercentages.get(i)*100);
		}

		//Traverse(t, -1);

	}
	
	private static Node PostPrune(Node t, int l, int k, int[][] val_set_mat) {
		// TODO Auto-generated method stub
		
		//copy tree
		Helper H = new Helper();
		Node bestTree = t;
		
		for(int i=0;i<l;i++)
		{
			Node tempTree = (Node)H.DeepCopy(t);
			int m = (int)(Math.random()*10000)%k;
			System.out.println("m :"+m);
			for(int j=0;j<m;j++)
			{
				int nodes = CountNodes(tempTree,0);
				System.out.println("j-"+j+"nodes-"+nodes);
				if(nodes<1)
					break;
				int randNo = (int)(Math.random()*10000)%nodes;
				tempTree = DeleteNodeat(tempTree,randNo);
			}
			double valPercentagenewTree = ValidateDecisionTree(tempTree,val_set_mat);
			double valPercentagebestTree = ValidateDecisionTree(bestTree,val_set_mat);
			System.out.println("valPercentagenewTree-"+valPercentagenewTree+"valPercentagebestTree-"+valPercentagebestTree);
			if(valPercentagebestTree<valPercentagenewTree)
			{
				System.out.println("Tree Changed");
				bestTree = tempTree;
			}
		}
		return bestTree;
	}

	private static Node DeleteNodeat(Node tempTree, int randNo) {
		// TODO Auto-generated method stub
		if(tempTree instanceof leaf)
			return tempTree;
		
		if(tempTree.NodeNo == randNo)
		{
			int zeroCount = Count(tempTree, 0, 0);
			int oneCount = Count(tempTree, 0, 1);
			
			System.out.println("zero Count -"+zeroCount+"one Count -"+oneCount);
			
			if(zeroCount > oneCount)
			{
				tempTree = new leaf(0,zeroCount);
			}
			else
			{ 
				tempTree = new leaf(1,oneCount);
			}
		}
		else
		{
			tempTree.ZeroNode = DeleteNodeat(tempTree.ZeroNode,randNo);
			tempTree.OneNode = DeleteNodeat(tempTree.OneNode,randNo);
		}
		return tempTree;
	}

	private static int CountNodes(Node tempTree,int i) {
		// TODO Auto-generated method stub
		int temp = i;
		
		if(tempTree instanceof leaf)
			return 0;
		
		//System.out.println(tempTree.Heading);
		
		
		temp = CountNodes(tempTree.ZeroNode,temp) + CountNodes(tempTree.OneNode,temp);
		
		tempTree.NodeNo = temp;
		
		temp++;
		
		return temp;
		
	}
	
	

	private static double ValidateDecisionTree(Node t, int[][] x) {
		// TODO Auto-generated method stub
		int errCount = 0;
		for(int i=0;i<x.length;i++)
		{
			int n = ClassfromDT(x[i],t);
			
			if(n==x[i][x[i].length-1])
				errCount++;
		}
		
		return (double)errCount/x.length;
	}

	private static int ClassfromDT(int[] row, Node t) {
		// TODO Auto-generated method stub
		for(;;)
		{
			if(t instanceof leaf)
				return ((leaf)t).ClassInfo;
			if(row[t.Column]==0)
				t = t.ZeroNode;
			else
				t = t.OneNode;
		}
	}

	private static void Traverse(Node t,int lvl) {
		lvl++;
		if(t.ZeroNode!=null)
		{
			if(t.ZeroNode instanceof leaf)
			{
				for(int i=0;i<lvl;i++)
					System.out.print("|");
				System.out.println(t.Heading+"= 0:"+((leaf)t.ZeroNode).strinfo+"count:"+((leaf)t.ZeroNode).items);
			}
			else
			{
				for(int i=0;i<lvl;i++)
					System.out.print("|");
				System.out.println(t.Heading+"= 0:");
				Traverse(t.ZeroNode,lvl);
			}
				
		}
		if(t.OneNode!=null)
		{
			if(t.OneNode instanceof leaf)
			{
				for(int i=0;i<lvl;i++)
					System.out.print("|");
				//System.out.println("one");
				System.out.println(t.Heading+"= 1:"+((leaf)t.OneNode).strinfo+"count:"+((leaf)t.OneNode).items);
			}
			else
			{
				for(int i=0;i<lvl;i++)
					System.out.print("|");
				System.out.println(t.Heading+"= 1:");
				Traverse(t.OneNode,lvl);
			}
				
		}
		return;
	}

	private static int Count(Node node,int count,int classInfo) {
		// TODO Auto-generated method stub
		int c1 = count;
		if(node instanceof leaf)
		{
			if(((leaf)node).ClassInfo==classInfo)
			{
				return ((leaf)node).items;
			}
			else
			{
				return 0;
			}
		}
		c1 += Count(node.ZeroNode,count,classInfo);
		c1 += Count(node.OneNode,count,classInfo);
		return c1;
	}

	private static Node Classify(int[][] x,int Noofcols, String[] headings, int iheur)
	{
		Helper H = new Helper();
		
		//if(x.length==0)
			//return null;
		
		if(H.ClassifiedAll(x, Noofcols))
			return new leaf(x[0][headings.length-1],x.length);
		
		Node node = new Node();
		Heuristics heur = new Heuristics();
		int BestCol = 0;
		
		if(iheur == 0)
			BestCol = heur.SelectBestColumnonInformationGain(x, Noofcols);
		else if(iheur==1)
			BestCol = heur.SelectBestColumnon1StepLookAhead(x, Noofcols);
		else
			BestCol = heur.SelectBestColumnonVarianceImpurity(x,Noofcols);
		
		System.out.println("Best Col"+BestCol);
		node.Column = BestCol;
		node.Heading = String.format("%s", headings[BestCol]);
		//node.count = x.length;
		
		//left zero subtree
		node.ZeroNode = Classify(H.ZeroSplitonData(x, BestCol),Noofcols,headings,iheur);
		node.OneNode = Classify(H.OneSplitonData(x, BestCol), Noofcols,headings,iheur);
		
		return node;
	}
	

}
