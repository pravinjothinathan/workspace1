
public class SharedArea extends Thread{

	static int SharedVariable = 0;
	
	int ThreadID;
	
	public SharedArea(int i)
	{
		ThreadID = i;
		System.out.println("Thread Initialization");
	}
	
	public void run()
	{
		for (int i = 0; i < 10000; i++) {
			SharedVariable++;
			System.out.println("ThreadID "+ThreadID+"Shared Variable "+SharedVariable);
		}
	}
}
