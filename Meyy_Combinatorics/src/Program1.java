import java.util.ArrayList;
import java.util.List;


public class Program1 {

	/**
	 * @param args
	 */
	
	static String[][] tMatrix = {	{"x","A","B","x"},
			{"x","x","C","D"},
			{"x","C","x","E"},
			{"x","x","x","x"}
	};
	
	static List<String> Vertices = new ArrayList<String>();
	
	static int level;
	public static void main(String[] args) {
		Vertices.add("1");
		Vertices.add("2");
		Vertices.add("3");
		Vertices.add("4");

		List<String> input= new ArrayList<String>();
		input.clear();
		input.add("A");
		input.add("B");
		input.add("C");
		input.add("D");
		input.add("E");
		
		List<Double> prob= new ArrayList<Double>();
		prob.clear();
		prob.add(0.5);
		prob.add(0.5);
		prob.add(0.5);
		prob.add(0.5);
		prob.add(0.5);
		double reliablity = NewCombi(input,prob,"",1.0,"",0);
		
		System.out.println("reliablity of the Circuit ->"+reliablity);
	}
	
	private static String[][] Copy2dArray(String[][] tMatrix) {
		// TODO Auto-generated method stub
		String[][] newArray = new String[tMatrix.length][tMatrix.length];
		for (int i = 0; i < newArray.length; i++) {
			for (int j = 0; j < newArray.length; j++) {
				newArray[i][j]= tMatrix[i][j];
			}
		}
		return newArray;
	}
	
	private static String[][] RemoveinTMatrix(String[][] clone,
			String pathsToRemove) {
		String[] edges = pathsToRemove.split("-");
		
		for (int i = 0; i < clone.length; i++) {
			for (int j = 0; j < clone.length; j++) {
				for (int j2 = 0; j2 < edges.length; j2++) {
					if(clone[i][j].equals(edges[j2]))
						clone[i][j]="x";
				}
			}
		}
		return clone;
	}
	
	private static boolean PathExists(String[][] tMatrix, String start,
			String Dest, String path, List<String> vertices,boolean returnVal) {
		
		int Pos = vertices.indexOf(start);
		
		if(start.equals(Dest))
		{
			//System.out.println(path);
			return true;
		}
		
		for (int i = 0; i < tMatrix.length; i++) {
			if(!tMatrix[Pos][i].equals("x"))
			{
				//If the current element is already present then it forms a loop!
				if(path.contains(tMatrix[Pos][i]))
				{
					//System.out.println("break on "+tMatrix[Pos][i]);
					continue;
				}
				
				start = vertices.get(i);
				returnVal = PathExists(tMatrix,start,Dest,path+tMatrix[Pos][i],vertices,returnVal);
				
			}
		}
		
		return returnVal;
	}

	
	private static double NewCombi(List<String> input,List<Double> prob, String stackTrace, double reliability,String negPaths,double totReliability)
	{
	
		if(input.size()==0){
			//checkpath();
			System.out.println(stackTrace +"=>"+reliability + " - "+negPaths);
			
			String[][] newMatrix = RemoveinTMatrix(Copy2dArray(tMatrix),negPaths);
			
			boolean Exists = PathExists(newMatrix,"1","4","",Vertices,false);
			//System.out.println(Exists);
			
			if(Exists)
			{
				totReliability +=reliability;
				System.out.println("cumilative Reilability =>" +totReliability);
			}
			
			
			
			return totReliability;
		}
		 
		String leftString = input.remove(0);
		String rightString = "-" + leftString;
		
		double leftProb = prob.remove(0);
		double rightProb = 1 - leftProb ;
		
		
		//Left Tree
		totReliability = NewCombi(input, prob, stackTrace + leftString, reliability * leftProb, negPaths,totReliability);
		
		//right Tree
		totReliability = NewCombi(input, prob, stackTrace + rightString, reliability * rightProb,negPaths + rightString,totReliability );
		
		input.add(0, leftString);
		prob.add(0,leftProb);
		
		return totReliability;
		
	}
	
}
