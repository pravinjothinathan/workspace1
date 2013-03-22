import java.util.ArrayList;
import java.util.List;


public class Program2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		int[][] tMatrix = {	{0,1,1,0},
							{0,0,1,1},
							{0,1,0,1},
							{0,0,0,0}
						};
		
		List<String> Vertices = new ArrayList<String>();
		
		Vertices.add("1");
		Vertices.add("2");
		Vertices.add("3");
		Vertices.add("4");
		
		String path ="";
		
		TracePath(tMatrix,"1","4",path,Vertices);

	}

	private static void TracePath(int[][] tMatrix, String cPos,
			String Dest, String path, List<String> vertices) {
		
		if(path.contains(cPos))
			return;
		
		int Pos = vertices.indexOf(cPos);
		path += cPos;
				
		if(Pos==vertices.size()-1)
			System.out.println(path);
		
		for (int i = 0; i < tMatrix.length; i++) {
				if(tMatrix[Pos][i]>0)
				{
					cPos = vertices.get(i);
					TracePath(tMatrix,cPos,Dest,path,vertices);
				}
		}
		
	}

}
