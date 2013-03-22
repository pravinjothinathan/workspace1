import java.io.DataInputStream;


public class MasterMind {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		DataInputStream dis = new DataInputStream(System.in);
		int c = 0;
		try {
			c = Integer.parseInt(dis.readLine());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		System.out.println("c:"+(c+1));
		
		for (int i = 0; i < c; i++) {
			//Looping through each entry
			String line =null;
			try{
			line = dis.readLine();
			String[] vals= line.split(" ");
			int n = Integer.parseInt(vals[0]);
			int k = Integer.parseInt(vals[1]);
			int q = Integer.parseInt(vals[2]);
			
			System.out.println("n:"+n+"k:"+k+"q:"+q);
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}

}
