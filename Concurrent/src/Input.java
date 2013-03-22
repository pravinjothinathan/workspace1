
public class Input {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int noOfThreads = Integer.parseInt(args[0]);
		
		SharedArea[] SAThreads =new SharedArea[noOfThreads]; 
		
		for (int i = 0; i < noOfThreads; i++) 
		{
			SAThreads[i] = new SharedArea(i+1);
			SAThreads[i].start();
		}
		//List<Integer> vals = new ArrayList<Integer>();
	}

}
