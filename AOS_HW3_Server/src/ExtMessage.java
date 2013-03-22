
public class ExtMessage {
	int Operation;
	int file;
	int ClientId;
	int sequenceNo;
	int logicalClock1;
	int logicalClock2;
	int serverId;
	
	public ExtMessage(Message m,int ServId,int seqNo,int logClock)
	{
		Operation = m.Operation;
		file = m.file;
		ClientId = m.ClientId;
		serverId = ServId;
		sequenceNo = seqNo;
		logicalClock1 = logClock;
		logicalClock2 =0;
	}
	
	public ExtMessage(ExtMessage m,int logClock2)
	{
		Operation = m.Operation;
		file = m.file;
		ClientId = m.ClientId;
		serverId = m.serverId;
		sequenceNo = m.sequenceNo;
		logicalClock1 = m.logicalClock1;
		logicalClock2= logClock2;
	}

	
	public String strExtMessage()
	{
		return String.format("operation:%d;fle:%d;ClientId:%d;serverId:%d;SequenceNo:%d;logClock:%d:%d",Operation,file,ClientId,serverId,sequenceNo,logicalClock1,logicalClock2);
	}
	
	public ExtMessage(String msg)
	{
		String[] vals = msg.split(";");
		Operation = Integer.parseInt(vals[0].split(":")[1]);
		file = Integer.parseInt(vals[1].split(":")[1]);
		ClientId = Integer.parseInt(vals[2].split(":")[1]);
		serverId = Integer.parseInt(vals[3].split(":")[1]);
		sequenceNo = Integer.parseInt(vals[4].split(":")[1]);
		logicalClock1 = Integer.parseInt(vals[5].split(":")[1]);
		logicalClock2 = Integer.parseInt(vals[5].split(":")[2]);
	}
}


