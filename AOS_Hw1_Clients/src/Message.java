
public class Message {
	
	static Helper h = new Helper();
	
	public Message(int ts, String file, int operation, int pid) {
		// TODO Auto-generated constructor stub
		timeStamp = ts;
		fileName = file;
		fileOperation = operation;
		processId = pid;
		ackCount = 0;
		reqCount = 0;
	}
	
	public Message(String s) {
		// TODO Auto-generated constructor stub
		String[] temp = s.split(" ");
		fileName = temp[1];
		fileOperation = Integer.parseInt(temp[2]);
		timeStamp = Integer.parseInt(temp[3]);
		processId = Integer.parseInt(temp[4]);
		h.Print("Inside Message constructor FN-"+fileName+"FO-"+fileOperation+"Ts-"+timeStamp+"processId"+processId);
		ackCount = 0;
		reqCount = 0;
	}

	int timeStamp;
	int fileOperation;
	String fileName;
	int processId;
	int ackCount;
	int reqCount;
	
	public String FormMessage() {
		// TODO Auto-generated method stub
		return String.format("%s %d %d %d",fileName,fileOperation,timeStamp,processId);
	}

	public boolean compare(Message m) {
		// TODO Auto-generated method stub
		if(timeStamp == m.timeStamp && fileOperation == m.fileOperation && fileName.equals(m.fileName) && processId == m.processId) 
			return true;
		else
			return false;
	}

	public void addAckCount() {
		// TODO Auto-generated method stub
		ackCount++;
	}

	public void addReqCount() {
		// TODO Auto-generated method stub
		reqCount++;
	}

	public String FormlongMessage() {
		// TODO Auto-generated method stub
		return String.format("%s %d %d %d %d %d",fileName,fileOperation,timeStamp,processId,ackCount,reqCount);
	}

	public boolean compareignoringTimeStamp(Message m) {
		// TODO Auto-generated method stub
		if(fileOperation == m.fileOperation && fileName.equals(m.fileName) && processId == m.processId) 
			return true;
		else
			return false;
	}

}
