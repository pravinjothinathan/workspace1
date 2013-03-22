import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

public class Server extends Thread
{
	//this is not object oriented have to revisit this approach
	List<Server> Servers;//current Server object
	static List<Client> Clients;//current Server object
	boolean mainServer;
	static boolean tailServer;
	int ServerId;
	Socket sock;
	static int currServerId;
	static int Tail;
	static int TailCommitSeq =0;
	static Server TailServer;
	static int SequenceNo;
	static int LogicalClock;
	static int ServerCount;
	static String Path;
	DataInputStream disDatain;//other server objects
	PrintStream psDataout;//other server objects
	static Map<Integer,ExtMessage> PendingRequests = Collections.synchronizedMap(new ConcurrentHashMap<Integer,ExtMessage>());
	static Map<Integer,List<Integer>> VectorClock = Collections.synchronizedMap(new ConcurrentHashMap<Integer, List<Integer>>());
	static Map<Integer,Integer> SeqtoClient = Collections.synchronizedMap(new  HashMap<Integer, Integer>());
	static Map<Integer,Integer> SeqtoHeadServer = Collections.synchronizedMap(new  HashMap<Integer, Integer>());
	static Map<Integer,Message> History = Collections.synchronizedMap(new HashMap<Integer, Message>());
	List<Message> MessagesInQueue;
	List<Message> OrphanMessages;
	static Semaphore ss = new Semaphore(1);
	static Semaphore ss2 = new Semaphore(2);
	static Helper h = new Helper();
	
	public Server(Socket tempsock)
	{
		mainServer = false;
		sock = tempsock;
		try {
			disDatain = new DataInputStream(tempsock.getInputStream());
			psDataout = new PrintStream(tempsock.getOutputStream());

			if(tailServer==true)
			{
				System.out.println("tail ServerId:"+currServerId);
				psDataout.println("tail ServerId:"+currServerId);
			}
			else
			{
				System.out.println("node ServerId:"+currServerId);
				psDataout.println("node ServerId:"+currServerId);
			}

			} catch (IOException e) {
			System.out.println("Error creatio of Streams in Existing Server connections!!");
			e.printStackTrace();
		}
		
	}
	
	public Server(List<Server> lstServers, List<Client> lstClients, boolean tail2, int serverId,int serverCount, String pathFolder, List<Message> messageInQueue) {
		mainServer = true;
		Servers = lstServers;
		Clients = lstClients;
		tailServer = tail2;
		currServerId = serverId;
		SequenceNo = (currServerId+1) * 10000;
		ServerCount = serverCount;
		Path = pathFolder;
		MessagesInQueue = messageInQueue;
	}

	public void run()
	{
		if(mainServer==true && tailServer==false){
			//node main server
			MainServerOperations();
		}
		else if(mainServer==true && tailServer==true){
			//tail main server
			System.out.println("Only three cann enter!!!");
			TailServerOperations();
		}
		else{
			//other servers
			OtherServerOperations();
		}
	}

	private void TailServerOperations() {
		while(true){
		//System.out.println("i am tail");
			TailServerClientOperations();
			List<Integer> Seqno = new ArrayList<Integer>();
			try {
				ss.acquire();
			
			for (Integer Seq_No : VectorClock.keySet()) {
				//System.out.println(Servers.size());
				List<Integer> temp = VectorClock.get(Seq_No);
				boolean incomplete = false;
				if(temp.size()<Servers.size())
					incomplete = true;
				if(incomplete==false){
					Seqno.add(Seq_No);
				}
			}
			
			if(Seqno!=null){
				for (Integer seq : Seqno) {
					
					VectorClock.remove(seq);
					int iServer = SeqtoHeadServer.get(seq);
					Message m = History.get(seq);
					System.out.println("complete Seqno ->"+seq + " file -> "+ m.file);
					String strMsg = seq+":"+TailCommitSeq+":"+iServer+":"+m.file+":"+m.ClientId;
					h.FileWrite(String.format(Path, m.file+1), "commit:" + strMsg);
					for (Server tServer : Servers) {
						 tServer.psDataout.println("commit:"+strMsg);
					}
					TailCommitSeq++;
				}
			}
			ss.release();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void OtherServerOperations() {
		while(true){
			String line = Read();
			if(line!=null){
			if(line.contains("tail")||line.contains("node")){
					int serv = Integer.parseInt(line.split(":")[1]);
					if(line.contains("tail")){
						TailServer = this;
						Tail = serv;
						ServerId = serv;
						System.out.println("Tail - ServerId - "+ServerId);
					}
					else{
						ServerId = serv;
						System.out.println("Node - ServerId - "+ServerId);
					}
				}
			else if(line.contains("head"))
			{
				System.out.println(line);
				Crash.Crash = false;
				Crash.intServer =-1;
				
			}
			
			else if(line.contains("commit")){
				try {
					ss2.acquire();
				int SeqNo = Integer.parseInt(line.split(":")[1]);
				int ServerNo = Integer.parseInt(line.split(":")[3]);
				int FleNo = Integer.parseInt(line.split(":")[4]);
				int ClientId = Integer.parseInt(line.split(":")[5]);
				String Msg = line.replace("commit :", "");
				if(currServerId==ServerNo){
					Clients.get(ClientId).psDataOut.println("ACK:"+FleNo+": seq ->"+SeqNo);
					}
				h.FileWrite(String.format(Path,FleNo+1), Msg);
				PendingRequests.remove(SeqNo);
				ss2.release();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			else{
				 //Extended message
				 if(tailServer==false){
					 try {
						ss2.acquire();
					
					 LogicalClock++;
					 ExtMessage eMsg = new ExtMessage(new ExtMessage(line),LogicalClock);
					 System.out.println("Message Sequence no - > "+ eMsg.sequenceNo);
					 PendingRequests.put(eMsg.sequenceNo, eMsg);
					 TailServer.psDataout.println(eMsg.strExtMessage());
					 ss2.release();
					 } catch (InterruptedException e) {
							e.printStackTrace();
					}
				 }
				 else{
					 ExtMessage eMsg = new ExtMessage(line);
					 if(eMsg.Operation==1)
					 {
						 try {
							ss.acquire();
						
						 if(VectorClock.containsKey(eMsg.sequenceNo)){
							 List<Integer> temp = VectorClock.get(eMsg.sequenceNo);
							 temp.add(eMsg.logicalClock2);
							 VectorClock.remove(eMsg.sequenceNo);
							 VectorClock.put(eMsg.sequenceNo, temp);
						 }
						 else{
							 List<Integer> temp = new ArrayList<Integer>();
							 temp.add(eMsg.logicalClock1);
							 temp.add(eMsg.logicalClock2);
							 History.put(eMsg.sequenceNo, new Message(eMsg.Operation,eMsg.file,eMsg.ClientId));
							 VectorClock.put(eMsg.sequenceNo, temp);
							 SeqtoClient.put(eMsg.sequenceNo, eMsg.ClientId);
							 SeqtoHeadServer.put(eMsg.sequenceNo, eMsg.serverId);
						 }
						 ss.release();
						 } catch (InterruptedException e) {
								e.printStackTrace();
							}
					 }
					 else
					 {
						 System.out.println("eMsg.ClientId-"+eMsg.ClientId+" ClientLen"+Clients.size());
						 Clients.get(eMsg.ClientId).psDataOut.println("read Succesful:"+eMsg.file +":tail read val - "+h.FileReadLastLine(String.format(Path, eMsg.file+1)));
						 System.out.println("Read value sent from tail to the corresponding client");
					 }
				 }
			 }
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public String Read()
	{
		try {
			//System.out.println("Read before!! "+ServerId);
			String line = disDatain.readLine();
			//System.out.println("Read after!!"+ServerId);
			return line;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private void TailServerClientOperations()
	{
		try {
			ss2.acquire();
			
			if(Crash.Crash==true)
			{
				CrashSequence();
			}
			
		while(MessagesInQueue.size()>0)
		{
			Message msg = MessagesInQueue.get(0);
			
			if(msg!=null)
			{
				LogicalClock++;
				if(msg.Operation==1)
				{
					String strMsg = SequenceNo+":"+TailCommitSeq+":"+currServerId+":"+msg.file+":"+msg.ClientId;
					h.FileWrite(String.format(Path, msg.file+1), "commit" + strMsg);
					Clients.get(msg.ClientId).psDataOut.println("ACK:"+msg.file+": seq ->"+SequenceNo);
					for (Server tServer : Servers) {
						 tServer.psDataout.println("commit:"+strMsg);
					}
					System.out.println("seq no created :"+SequenceNo);
					SequenceNo++;
					TailCommitSeq++;
				}
				else if(msg.Operation==0)
				{
					//System.out.println("Perform a local read in tail !!");
					Client client = Clients.get(msg.ClientId);
					client.psDataOut.println("read Succesful:"+msg.file +":local read val - "+h.FileReadLastLine(String.format(Path, msg.file+1)));
				}
				
			}
			
			MessagesInQueue.remove(0);
		}
		ss2.release();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
	
	@SuppressWarnings("deprecation")
	private void CrashSequence() 
	{
		//System.out.println("Tail "+Tail);
		if(Crash.intServer == Tail)
		{
			/**/
			
			
			System.out.println("IS this coming in !!!!!");
			Server temp =null;
			for (Server s : Servers) {
				System.out.println("s.ServerId "+s.ServerId+" Crash.intServer "+Crash.intServer);
				if(s.ServerId == Crash.intServer)
				{
					System.out.println("Thread Stopped!!");
					s.stop();
					temp = s;
				}
			}
			Servers.remove(temp);
			int x =1;
			for (Server s : Servers) {
				
				System.out.println("s.ServerId "+s.ServerId+" Crash.intServer "+Crash.intServer);
				if(s.ServerId == Tail-1)
				{
					System.out.println("Setting Tail configurations!!");
					TailServer = s;
					Tail = Tail -1;
					x = 0;
					break;
				}
			}
			
			System.out.println("prev count "+Servers.size());
			Servers.remove(temp);
			System.out.println("new count "+Servers.size());

			if(Tail == Crash.intServer && x==1)
			{
				System.out.println("Server count "+Servers.size());
				System.out.println("curr sev id - "+currServerId+"Tail-1"+(Tail-1));
				System.out.println("Making the highest Server as Tail Server");
				tailServer = true;
				TailServer = this;
				Tail = Tail -1;
				
				System.out.println("Start Tail server operations !!");
				System.out.println("Push Peding to VC !!");
				
				PushPendingRequeststoVectorClock();
				
				Crash.intServer =-1;
				Crash.Crash = false;
				
				TailServerOperations();
			}
			else
			{
				System.out.println("Push Pending to tail");
				PushPendingRequeststoTail();
				
				Crash.intServer =-1;
				Crash.Crash = false;
			}
			
		}
		else
		{
			OrphanMessages = new ArrayList<Message>();
			//since s crashed am stopping that thread
			Server temp =null;
			for (Server s : Servers) {
				System.out.println("s.ServerId "+s.ServerId+" Crash.intServer "+Crash.intServer);
				if(s.ServerId == Crash.intServer)
				{
					System.out.println("Thread Stopped!!");
					s.stop();
					temp = s;
				}
			}
			System.out.println("prev count "+Servers.size());
			Servers.remove(temp);
			System.out.println("new count "+Servers.size());
	
			System.out.println("PendingRequests size :"+PendingRequests.size());
			for (Integer seq : PendingRequests.keySet())
			{
				ExtMessage eMsg = PendingRequests.get(seq);
				int SeqNo = eMsg.sequenceNo;
				System.out.println("SeqNo -> "+SeqNo);
				if(SeqNo/10000==Crash.intServer+1)
				{
					PendingRequests.remove(seq);
					System.out.println("pushed in!!");
					Message m = new Message(eMsg.Operation, eMsg.file, eMsg.ClientId);
					OrphanMessages.add(m);
				}
			}
			System.out.println("OrphanMessages count :" + OrphanMessages.size());
			
			while(Crash.Crash==true)
			{
				//rtt * j
				try {
					Thread.sleep(3000*(ServerId+1));
					if(Crash.Crash==true)
					{
						System.out.println("I am daddy !!");
						MessagesInQueue.addAll(OrphanMessages);
						for (Server tempServ : Servers) {
							tempServ.psDataout.println("server "+ ServerId +"i am head");
						}
						Crash.Crash = false;
						Crash.intServer = -1;
						break;
					}
					else
						break;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println("out of Creash Sequence!");
			OrphanMessages.clear();
		}
	}

	private void PushPendingRequeststoTail() {
		// TODO Auto-generated method stub
		for (int seq : PendingRequests.keySet()) {
			ExtMessage eMsg = PendingRequests.get(seq);
			TailServer.psDataout.println(eMsg.strExtMessage());
		}
		
	}

	private void PushPendingRequeststoVectorClock() {
		// TODO Auto-generated method stub
		for (int seq : PendingRequests.keySet()) {
			ExtMessage eMsg = PendingRequests.get(seq);
			List<Integer> temp = new ArrayList<Integer>();
			temp.add(eMsg.logicalClock1);
			 History.put(eMsg.sequenceNo, new Message(eMsg.Operation,eMsg.file,eMsg.ClientId));
			 VectorClock.put(eMsg.sequenceNo, temp);
			 SeqtoClient.put(eMsg.sequenceNo, eMsg.ClientId);
			 SeqtoHeadServer.put(eMsg.sequenceNo, eMsg.serverId);
		}
	}
	
	

	private void MainServerOperations() {
		while(true)
		{
			
			try {
				ss2.acquire();
				
			if(Crash.Crash==true)
			{
				CrashSequence();
			}
			
			//System.out.println("out from Crash! Resuming normal operations!!");
			
				
			while(MessagesInQueue.size()>0)
			{
				Message msg = MessagesInQueue.get(0);
				
				if(msg!=null)
				{
					if(msg.Operation==1)//incoming message for read !!!
					{
						LogicalClock++;
						ExtMessage eMsg = new ExtMessage(msg,currServerId,SequenceNo,LogicalClock);
						String messageToSend = eMsg.strExtMessage();
						for (Server tempServ : Servers) {
							if(!(tempServ.ServerId==Tail))
							{
								System.out.println("send "+eMsg.sequenceNo + " id " + tempServ.ServerId);
								tempServ.psDataout.println(messageToSend);
							}
						}
						PendingRequests.put(SequenceNo, eMsg);
						System.out.println("seq no created :"+SequenceNo);
						SequenceNo++;
					}
					else if(msg.Operation==0)
					{
						LogicalClock++;
						System.out.println("Read on file "+msg.file+"Client Id "+msg.ClientId);
						int x=0;
						for (Integer seq : PendingRequests.keySet()) {
							ExtMessage eMsg = PendingRequests.get(seq);
								if(eMsg.file==msg.file)
								{
									System.out.println("Read request sent to tail with client Id "+msg.ClientId);
									String tExtMsg = new ExtMessage(msg,ServerId,SequenceNo,LogicalClock).strExtMessage();
									System.out.println("Message to tail - > "+ tExtMsg);
									TailServer.psDataout.println(tExtMsg);
									x=1;
									break;
								}
						}
						
						if(x==0)
						{
							System.out.println("Perform a local read !!");
							Client client = Clients.get(msg.ClientId);
							client.psDataOut.println("read Succesful:"+msg.file +":local read val - "+h.FileReadLastLine(String.format(Path, msg.file+1)));
						}
					}
					
					MessagesInQueue.remove(0);
					
				}
			}
			//System.out.println("PendingRequests size -> "+PendingRequests.size());
			/*for (int i=0;i<Clients.size();i++) {
				Client client = Clients.get(i);
				Message msg = client.ReadStream(ServerId);
				
			}*/
			ss2.release();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
