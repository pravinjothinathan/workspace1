import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


@SuppressWarnings("serial")
public class Serializer implements Serializable{

	public void SerializeData(Object Data,String File)
	{
		ObjectOutputStream ooS =null;
		try {
			ooS = new ObjectOutputStream(new FileOutputStream(File));
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception is OOS Stream Creation !!!!");
		}
		try {
			if(ooS!=null)
			{
				ooS.writeObject(Data);
				ooS.flush();
				ooS.close();
			}
			else
				System.out.println("Error OOS Creation not Succesful !!!");
		} catch (Exception e) {
			// TODO: handle exception
			
		}
	}
	
	public Object DeSerializeData(String File)
	{
		ObjectInputStream oiS =null;
		try {
			oiS = new ObjectInputStream(new FileInputStream(File));
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception in creating OIS !!!!");
		}
		Object Value =null;
		
		try {
			if(oiS!=null){
				Value = oiS.readObject();
				oiS.close();
			}
			else{
				System.out.println("Error oiS is null !!!");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return Value;
	}
}
