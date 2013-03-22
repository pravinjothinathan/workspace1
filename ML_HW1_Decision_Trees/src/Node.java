import java.io.Serializable;


public class Node implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String Heading;
	int Column;
	//int count;
	//int ZeroNodesCount;
	int NodeNo;
	Node ZeroNode;
	Node OneNode;
	
	public Object clone()
    {
        try
    {
            return super.clone();
        }
    catch( CloneNotSupportedException e )
    {
            return null;
        }
    } 
}
