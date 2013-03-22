import java.io.BufferedReader;
import java.io.FileReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;


public class InputParameters {
	
	List<Info> Servers;
	
	public InputParameters(String file) {
		// TODO Auto-generated constructor stub
		try 
		 {
			BufferedReader input =  new BufferedReader(new FileReader(file));
			String line = null; //not declared within while loop
	        line = input.readLine();
	        Servers = ConverttoInfo(line);
	        input.close();
		 } 
		 catch (Exception e) 
		 {
			e.printStackTrace();
		 }
	}

	private List<Info> ConverttoInfo(String line) {
		// TODO Auto-generated method stub
		if(line.contains("*"))
			return null;
		List<Info> Servers = new ArrayList<Info>();
		String[] Infos = line.split(";");
		for(int i=0;i<Infos.length;i++)
		{
			String[] paras = Infos[i].split(",");
			try {
				Servers.add(new Info(InetAddress.getByName(paras[0]),Integer.parseInt(paras[1])));
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return Servers;
	}

}
