
public class ParseData {

	String[] Headings;
	int[][] n;
	
	public void parse(String s) {
		// TODO Auto-generated method stub
		String[] lines = s.split(";");
		Headings = lines[0].split(",");
		
		n = new int[lines.length-1][Headings.length];
		
		for(int i=1;i<lines.length;i++)
		{
			String[] temp = lines[i].split(",");
			for(int j=0;j<temp.length;j++)
			{
				n[i-1][j] = Integer.parseInt(temp[j]);
			}
		}
	}
	
	public int[][] GetMatrix()
	{
		return n;
	}
	
	public String[] GetColHeaders()
	{
		return Headings;
	}

}
