import java.util.ArrayList;
import java.util.List;


public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<String> input= new ArrayList<String>();
		input.clear();
		input.add("a");
		input.add("b");
		input.add("c");
		input.add("d");
		input.add("e");
	
		Combinations(input,"");
	}

	private static void Combinations(List<String> input,String curStr) {
		System.out.println(curStr);
		
		
	}

}
