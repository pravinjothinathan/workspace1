import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Program2_1 {

	/**
	 * @param args
	 */
public static void main(String[] args) {
		
		String[][] tMatrix = {	{"x","A","B","x"},
								{"x","x","C","D"},
								{"x","C","x","E"},
								{"x","x","x","x"}
						};
		
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
		
		List<String> Paths = new ArrayList<String>();
		Paths = TracePath(tMatrix,"1","4","",Vertices,Paths);
		
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



	private static List<String> TracePath(String[][] tMatrix, String start,
			String Dest, String path, List<String> vertices,List<String> Paths) {
		
		int Pos = vertices.indexOf(start);
				
		if(start.equals(Dest))
		{
			Paths.add(path);
			//System.out.println(path);
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
				Paths = TracePath(tMatrix,start,Dest,path+":"+tMatrix[Pos][i],vertices,Paths);
				
			}
		}
		
		return Paths;
	}
}
