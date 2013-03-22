import aima.core.environment.eightpuzzle.EightPuzzleBoard;
import aima.core.environment.eightpuzzle.EightPuzzleFunctionFactory;
import aima.core.environment.eightpuzzle.EightPuzzleGoalTest;
import aima.core.environment.eightpuzzle.ManhattanHeuristicFunction;
import aima.core.search.framework.ActionsFunction;
import aima.core.search.framework.GraphSearch;
import aima.core.search.framework.Problem;
import aima.core.search.framework.Search;
import aima.core.search.framework.SearchAgent;
import aima.core.search.informed.GreedyBestFirstSearch;


public class Sample {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		int ip [][] = {{1,0,2,4},{5,6,3,7},{9,10,11,8},{13,14,15,12}};
		int op [][] = {{1,2,3,4},{5,6,7,8},{9,10,11,12},{13,14,15,0}};

		//ActionsFunction
		//EightPuzzleFunctionFactory
		EightPuzzleBoard epb = null;
		Problem p = new Problem(epb,EightPuzzleFunctionFactory.getActionsFunction(),EightPuzzleFunctionFactory.getResultFunction(),new EightPuzzleGoalTest());
		
		Search gbs = new GreedyBestFirstSearch(new GraphSearch(), new ManhattanHeuristicFunction());
		
		try {
			SearchAgent s = new SearchAgent(p,gbs);
			
				
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
