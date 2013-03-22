package SecFeatures;

public class Key {
	
	public String strKey;
	public String strHexKey;
	
	public Key (String key, String hexKey)
	{
		strKey = key;
		strHexKey = hexKey;
	}

	public Key() {
		strKey = null;
		strHexKey = null;
	}

}
