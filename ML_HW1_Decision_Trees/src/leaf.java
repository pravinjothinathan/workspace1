
public class leaf extends Node{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int ClassInfo;
	String strinfo;
	int items;
	
	public leaf(int info,int n) {
		// TODO Auto-generated constructor stub
		//System.out.println("info-"+info);
		ClassInfo = info;
		strinfo = String.format("%d", info);
		items = n;
	}

}
