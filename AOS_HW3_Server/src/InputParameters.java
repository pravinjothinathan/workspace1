import java.io.BufferedReader;
import java.io.FileReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;


public class InputParameters {
	
	int ServerPortNo;
	int ClientPortNo;
	List<Info> Servers;
	boolean tail;
	int ServerId;
	String pathFolder;
	Info infoMasterServer;
	
	public InputParameters(String file) {
		try 
		 {
			BufferedReader input =  new BufferedReader(new FileReader(file));
			String line = null; //not declared within while loop
			int lineNo =0;
	        while (( line = input.readLine()) != null)
	        {
	        	switch(lineNo)
	        	{
	        	case 0:
	        		String[] ports = line.split(";");
	        		ServerPortNo = Integer.parseInt(ports[0]);
	        		ClientPortNo = Integer.parseInt(ports[1]);
	        		break;
	        	case 1:
	        		Servers = ConverttoInfo(line);
	        		break;
	        	case 2:
	        		tail = Boolean.parseBoolean(line);
	        		break;
	        	case 3:
	        		ServerId = Integer.parseInt(line);
	        		break;
	        	case 4:
	        		pathFolder = line;
	        		break;
	        	case 5:
	        		infoMasterServer = GetFirstInfo(ConverttoInfo(line));
	        		break;
	        	}
	        	lineNo++;
	        }
	        input.close();
		 } 
		 catch (Exception e) 
		 {
			e.printStackTrace();
		 }
	}

	private Info GetFirstInfo(List<Info> converttoInfo) {
		Info first = converttoInfo.get(0);
		return first;
	}

	private List<Info> ConverttoInfo(String line) {
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
				e.printStackTrace();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}

		return Servers;
	}

}
