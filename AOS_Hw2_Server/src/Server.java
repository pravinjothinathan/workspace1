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
	int Tail;
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
	static Map<Integer,Message> History = Collections.synchronizedMap(new HashMap<Integer, Message>()); 
	static Semaphore ss = new Semaphore(1);
	static Semaphore ss2 = new Semaphore(2);
	static Helper h = new Helper();
	
	public Server(Socket tempsock)
	{
		mainServer = false;
		sock = tempsock;
		System.out.println("Entering other Servers constructor ");
		//creation for other server objects
		try {
			disDatain = new DataInputStream(tempsock.getInputStream());
			psDataout = new PrintStream(tempsock.getOutputStream());
			System.out.println("Stream Creation Succesful");

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
	
	public Server(List<Server> lstServers, List<Client> lstClients, boolean tail2, int serverId,int serverCount, String pathFolder) {
		//Server Creation for the current Server
		System.out.println("Entering Main Servers constructor ");
		mainServer = true;
		Servers = lstServers;
		Clients = lstClients;
		tailServer = tail2;
		currServerId = serverId;
		SequenceNo = currServerId * 10000;
		ServerCount = serverCount;
		Path = pathFolder;
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
			List<Integer> Seqno = new ArrayList<Integer>();
			try {
				ss.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for (Integer Seq_No : VectorClock.keySet()) {
				if(Seq_No>Seq_No){
					//Request directly to the Tail
					
				}
				else{
					List<Integer> temp = VectorClock.get(Seq_No);
					boolean incomplete = false;
					if(temp.size()<ServerCount)
						incomplete = true;
					if(incomplete==false){
						//CompletedTasks.add(temp);
						Seqno.add(Seq_No);
						System.out.println("waiting on write lock!!!");
						System.out.println("Remove Seq no -> "+ Seq_No +" ClientId ->"+SeqtoClient.get(Seq_No));
					}
				}
			}
			
			if(Seqno!=null){
				for (Integer seq : Seqno) {
					VectorClock.remove(seq);
					Message m = History.get(seq);
					h.FileWrite(String.format(Path, m.file+1), m.StrMessage()+" Seq->"+TailCommitSeq);
					Clients.get(m.ClientId-1).psDataOut.println("ACK:"+m.file+": seq ->"+seq);
					for (Server tServer : Servers) {
						 tServer.psDataout.println("commit:"+seq+":"+TailCommitSeq);
					}
					TailCommitSeq++;
				}
			}
			ss.release();
			//fWriteLock.unlock();
		}
	}

	private synchronized void OtherServerOperations() {
		System.out.println("Entering run - other Server operations");
		while(true){
			//System.out.println("Read wait :");
			String line = Read();
			if(line!=null){
			System.out.println(line);
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
			else if(line.contains("commit")){
				try {
					ss2.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("commit : "+line);
				int SeqNo = Integer.parseInt(line.split(":")[1]);
				int TailCS = Integer.parseInt(line.split(":")[2]);
				ExtMessage eMsg = PendingRequests.get(SeqNo);
				Message msg = new Message(eMsg.Operation,eMsg.file,eMsg.ClientId);
				h.FileWrite(String.format(Path,msg.file+1), msg.StrMessage()+" Seq->"+TailCS);
				PendingRequests.remove(SeqNo);
				ss2.release();
			}
			 else{
				 //Extended message
				 if(tailServer==false){
					 try {
						ss2.acquire();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					 LogicalClock++;
					 ExtMessage eMsg = new ExtMessage(new ExtMessage(line),LogicalClock);
					 System.out.println("Message Sequence no - > "+ eMsg.sequenceNo);
					 PendingRequests.put(eMsg.sequenceNo, eMsg);
					 System.out.println(eMsg.strExtMessage());
					 TailServer.psDataout.println(eMsg.strExtMessage());
					 ss2.release();
				 }
				 else{
					 ExtMessage eMsg = new ExtMessage(line);
					 if(eMsg.Operation==1)
					 {
						 try {
							ss.acquire();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						// fWriteLock.lock();
						 if(VectorClock.containsKey(eMsg.sequenceNo)){
							 List<Integer> temp = VectorClock.get(eMsg.sequenceNo);
							 temp.add(eMsg.logicalClock2);
							 //temp[ServerId-1]=eMsg.logicalClock2;
							 System.out.println("Inside if present Curr Server Id ->" +ServerId+" Seq no ->"+eMsg.sequenceNo);
							 VectorClock.remove(eMsg.sequenceNo);
							 VectorClock.put(eMsg.sequenceNo, temp);
						 }
						 else{
							 List<Integer> temp = new ArrayList<Integer>();
							 System.out.println("During Vector clock creation");
							 System.out.println("Inside else eMsg.serverId ->"+eMsg.serverId +" Seq no ->"+eMsg.sequenceNo);
							 System.out.println("Inside else Curr Server Id ->" +ServerId +" Seq no ->"+eMsg.sequenceNo);
							 //temp[eMsg.serverId-1]= eMsg.logicalClock1;
							 //temp[ServerId-1]= eMsg.logicalClock2;
							 temp.add(eMsg.logicalClock1);
							 temp.add(eMsg.logicalClock2);
							 History.put(eMsg.sequenceNo, new Message(eMsg.Operation,eMsg.file,eMsg.ClientId));
							 VectorClock.put(eMsg.sequenceNo, temp);
							 SeqtoClient.put(eMsg.sequenceNo, eMsg.ClientId);
						 }
						 ss.release();
						 //fWriteLock.unlock();
					 }
					 else
					 {
						 //fWriteLock.lock();
						 //Sending the actual file
						 System.out.println("eMsg.ClientId-"+eMsg.ClientId+" ClientLen"+Clients.size());
						 Clients.get(eMsg.ClientId-1).psDataOut.println("read Succesful:"+eMsg.file +":tail read val - "+h.FileReadLastLine(String.format(Path, eMsg.file+1)));
						 System.out.println("Read value sent from tail to the corresponding client");
						 //SequenceNo++;
						 //fWriteLock.unlock();
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
			return disDatain.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void MainServerOperations() {
		while(true)
		{
			try {
				ss2.acquire();
				//System.out.println("Pending Requests Size -> "+PendingRequests.size());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for (int i=0;i<Clients.size();i++) {
				Client client = Clients.get(i);
				Message msg = client.ReadStream();
				if(msg!=null)
				{
					System.out.println("op - "+msg.Operation+"fle - "+msg.file);
					if(msg.Operation==1)
					{
						LogicalClock++;
						System.out.println("Write on file "+msg.file);
						ExtMessage eMsg = new ExtMessage(msg,currServerId,SequenceNo,LogicalClock);
						String messageToSend = eMsg.strExtMessage();
						for (Server tempServ : Servers) {
							if(tempServ.ServerId!=tempServ.Tail)
							{
								System.out.println("Message to send : "+messageToSend);
								tempServ.psDataout.println(messageToSend);
							}
						}
						PendingRequests.put(SequenceNo, eMsg);
						SequenceNo++;
					}
					else if(msg.Operation==0)
					{
						LogicalClock++;
						System.out.println("Read on file "+msg.file+"Client Id "+i);
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
							client.psDataOut.println("read Succesful:"+msg.file +":local read val - "+h.FileReadLastLine(String.format(Path, msg.file+1)));
						}
						//SequenceNo++;
					}
					
				}
			}
			ss2.release();
		}
	}
}
