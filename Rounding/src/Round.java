import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class Round {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Map<String,String> s = new HashMap<String,String>();
		int n =7;
		int tot = 1337;
		int curr =1;
		int x =1;
		
		for(int i =1;i<tot+1;i++)
		{
			 
			System.out.print(curr+"->"+i+" ");
			s.put(String.format("%d",i), String.format("%d",curr));
			if(String.format("%d", i).contains("7")||i%n==0)
			{
				x = change(x);
			}
			
			if(x==1)
				curr =  increment(curr);
			else
				curr = decrement(curr);
			
		} 
		
		for(int i=0;i<n;i++)
		{
			System.out.print("Enter a no :");
			Scanner kb = new Scanner(System.in);
			int y = kb.nextInt();
			System.out.println("val is "+s.get(String.format("%d", y)));
			
		}

	}

	private static int change(int x) {
		// TODO Auto-generated method stub
		System.out.println("");
		if (x== 1)
			return 0;
		else
			return 1;
	}

	private static int decrement(int curr) {
		// TODO Auto-generated method stub
		if(curr>1)
			return curr -1;
		else if (curr ==1)
			return 1337;
		return 0;
	}

	private static int increment(int curr) {
		// TODO Auto-generated method stub
		//if (curr>0)
		return curr%1337 + 1 ;
	}

}
