import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Program3 {

	/**
	 * @param args
	 */
	
 public static void main(String[] args) {
		// TODO Auto-generated method stub

		String[][] tMatrix = {	{"x","A","B","x"},
				{"x","x","C","D"},
				{"x","C","x","E"},
				{"x","x","x","x"}
		};
		
		List<String> Edges = new ArrayList<String>();
		
		Edges.add("A");
		Edges.add("B");
		Edges.add("C");
		Edges.add("D");
		Edges.add("E");
		
		List<String> Vertices = new ArrayList<String>();
		
		Vertices.add("1");
		Vertices.add("2");
		Vertices.add("3");
		Vertices.add("4");
		
		Map<String,Double> prob= new HashMap<String,Double>();
		prob.clear();
		prob.put("A",0.5);
		prob.put("B",0.5);
		prob.put("C",0.5);
		prob.put("D",0.5);
		prob.put("E",0.5);
		

		int len =6;
		int start =1;
		List<String> Combinations = new ArrayList<String>();
		Combinations = GeneNoRepeat("",len-1,start,0,Combinations,Edges);
		
		Combinations = sort(Combinations);
		
		List<String> Paths = new ArrayList<String>();
		
		for (int i = 0; i < Combinations.size(); i++) {
			
			String pathsToRemove = Combinations.get(i) ;
			
			String[][] newMatrix = RemoveinTMatrix(Copy2dArray(tMatrix),pathsToRemove);
			
			boolean Exists = PathExists(newMatrix,"1","4","",Vertices,false);
			if(Exists == false)
			{
				System.out.println(pathsToRemove);
				
				Paths.add(pathsToRemove);
				
				Combinations = RemoveFromtheRest(Combinations,pathsToRemove);
			}
			
		}
		
		System.out.println(Paths.size());
		
		double RelFraction = GetFraction(Paths,prob);
		
		System.out.println(RelFraction);

		
	}
 
 	private static double GetFraction(List<String> paths,
			Map<String, Double> prob) {
		double finVal = 1 ;
		for (int i = 0; i < paths.size(); i++) {
			double temp= 1;
			System.out.println(paths.get(i));
			String[] indexes = paths.get(i).split(":");
			for (int j = 0; j < indexes.length; j++) {
				if(prob.get(indexes[j])!=null)
					temp *= prob.get(indexes[j]);
			}
			System.out.println("prob foe the current path ->"+temp);
			finVal *= (1- temp); 
		}
		return 1-finVal;
	}

	
	private static List<String> sort(List<String> combinations) {
		while(true)
		{
			int count =0;
			
			for(int i=0;i<combinations.size()-1;i++)
			{
				if(combinations.get(i).length()>combinations.get(i+1).length())
				{
					combinations.add(i, combinations.remove(i+1));
					count++;
				}
			}
			
			if(count==0)
				break;
		}
		return combinations;
	}

	private static List<String> RemoveFromtheRest(List<String> combinations,
			String pathsToRemove) {
		
		String[] edges = pathsToRemove.split(":");
		for (int i = 0; i < combinations.size(); i++) {
			int x = 0;
			for (int j = 0; j < edges.length; j++) {
				if(combinations.get(i).contains(edges[j]))
					x++;
					
			}
			if(x==edges.length)
			{
				combinations.remove(i);
				i--;
			}
		}
		return combinations;
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
		String[] edges = pathsToRemove.split(":");
		
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

	private static List<String> GeneNoRepeat(String s, int len, int start, int lvl, List<String> combinations,List<String> vals) {
		combinations.add(s);
		
		for (int i = 0; i < vals.size(); i++) {
			
			if(s.contains(vals.get(i)))
				continue;
			
			String temp = String.format("%s", s);
			temp = temp+ ":" +vals.get(i);
				
			if(len>=start)
			{
				combinations = GeneNoRepeat(temp, len, start+1,lvl+1,combinations,vals);
			}
			
			if(lvl==0)
			{
				//System.out.println("remove ->"+vals.remove(0));
				//i--;
				//len--;
			}
				
		}
		
		return combinations;
	}
	
	private static boolean PathExists(String[][] tMatrix, String start,
			String Dest, String path, List<String> vertices,boolean returnVal) {
		
		int Pos = vertices.indexOf(start);
		
		if(start.equals(Dest))
		{
			return true;
		}
		
		for (int i = 0; i < tMatrix.length; i++) {
			if(!tMatrix[Pos][i].equals("x"))
			{
				if(path.contains(tMatrix[Pos][i]))
				{
					continue;
				}
				
				start = vertices.get(i);
				returnVal = PathExists(tMatrix,start,Dest,path+tMatrix[Pos][i],vertices,returnVal);
				
			}
		}
		
		return returnVal;
	}

	

}
