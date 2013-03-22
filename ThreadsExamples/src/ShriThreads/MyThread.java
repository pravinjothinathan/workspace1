package ShriThreads;

import ShriObjects.SharedMemory;

public class MyThread extends Thread{
	
	int threadId;
	SharedMemory sharedMemory;
	public MyThread(int Id,SharedMemory sum)
	{
		threadId = Id;
		sharedMemory = sum;
	}
	
	public void run()
	{
		/* System.out.println("My Thread Id is " + threadId); */
		
		doPrint();
		
	}
	
	public void doPrint()
	{
		for (int i = 0; i < 1000; i++) {
			
			sharedMemory.add();
			System.out.print("Thrread " + threadId +" updates "); 
			sharedMemory.Print();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
	}

}
