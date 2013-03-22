import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class Serialize 
{
	public boolean SerializeData(Object o,String file)
	{
		try
	      {
	         FileOutputStream fileOut = new FileOutputStream(file);
	         ObjectOutputStream out = new ObjectOutputStream(fileOut);
	         out.writeObject(o);
	         out.close();
	          fileOut.close();
	      }catch(Exception i)
	      {
	          i.printStackTrace();
	      }
		return false;
	}
	
	public Object DeSerializeData(String file)
	{
		Object e = null;
		try
        {
           FileInputStream fileIn = new FileInputStream(file);
           ObjectInputStream in = new ObjectInputStream(fileIn);
           e = in.readObject();
           in.close();
           fileIn.close();
       }catch(Exception i)
       {
           i.printStackTrace();
       }
		return e;
	}
}
