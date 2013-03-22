import java.util.ArrayList;
import java.util.List;


public class Generator {

	/**
	 * @param args
	 */
	static char [] c = {'A','B','C','D','E'};
	static List<String> vals = new ArrayList<String>();
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		int len =6;
		int start =1;
		
		vals.add("A");
		vals.add("B");
		vals.add("C");
		vals.add("D");
		vals.add("E");
		List<String> Combinations = new ArrayList<String>();
		Combinations = GeneNoRepeat("",len-1,start,0,Combinations);
		
		System.out.println(Combinations.size());
		System.out.println(Combinations.toString());
	}

	private static void Gene(String s, int len, int start) {
		System.out.println(s);
		
		
		for (int i = 0; i < c.length; i++) {
			String temp = String.format("%s", s);
			temp = temp+ c[i];
			
			if(len>=start)
			{
				Gene(temp, len, start+1);
			}
		}
	}
	
	private static List<String> GeneNoRepeat(String s, int len, int start, int lvl, List<String> combinations) {
		System.out.println(s);
		combinations.add(s);
		
		for (int i = 0; i < vals.size(); i++) {
			
			if(s.contains(vals.get(i)))
				continue;
			
			String temp = String.format("%s", s);
			temp = temp+ vals.get(i);
				
			if(len>=start)
			{
				combinations = GeneNoRepeat(temp, len, start+1,lvl+1,combinations);
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

}
