
public class Start {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}
	
	public boolean findPalindrome (String in)
	{
		
	int n = in.length();
	try{
	    if(in != null)
	    {
	        int k = n -1;
	        for(int i=0;i<n/2;i++)
	        {
	            if(in.charAt(i)!=in.charAt(k))
	                return false;
	            k--;
	        }
	        return true;
	    }
	    }catch(Exception ex)
	    {
	        ex.PrintStackTrace();
	    }
	    return false;
	}

}
