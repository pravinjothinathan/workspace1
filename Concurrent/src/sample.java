import java.util.HashMap;
import java.util.List;


public class Signature {
	

	public static void main(String args[])
	{
	
	}
	
	//set this in hte constructor
	HashMap<Integer,Integer> curSinaure;
	
	public boolean Compare (HashMap<Integer,Integer> map1)
	{
		try
		{
		if (map1.keySet().size()==curSinaure.keySet().size())
		{
			List<Integer> Keyset = (List<Integer>)map1.keySet();
			
			for (int i = 0; i < map1.keySet().size(); i++) {
				int freq1 = curSinaure.get(Keyset.get(i));
				int freq2 = map1.get(Keyset.get(i));
				if(freq1!=freq2)
					return false;
			}
			return true;
		}}catch(Exception e)
		{
			//print exception!
		}
		return false;
	}
}





