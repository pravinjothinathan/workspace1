
public class Message {
	int Operation;
	int file;
	int ClientId;
	
	public Message(int op,int fle,int cId)
	{
		Operation = op;
		file = fle;
		ClientId = cId;
	}
	
	public String StrMessage()
	{
		return String.format("operation:%d;fle:%d;ClientId:%d", Operation,file,ClientId);
	}
	
	public Message(String msg)
	{
		String[] MsgSplit = msg.split(";");
		Operation = Integer.parseInt(MsgSplit[0].split(":")[1]);
		file = Integer.parseInt(MsgSplit[1].split(":")[1]);
		ClientId = Integer.parseInt(MsgSplit[2].split(":")[1]);
	}
}
