package SecFeatures;

public class PortKey {
	
	public Key key;
	public int PortNo;

	public PortKey(String skey,String HexKey, int portno) {
		key = new Key(skey,HexKey);
		PortNo = portno;
	}

}
