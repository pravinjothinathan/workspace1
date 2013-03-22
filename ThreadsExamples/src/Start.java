import ShriThreads.*;
import ShriObjects.SharedMemory;

public class Start {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		
		/* Its just a simple thread example		
		 * MyThread thread = new MyThread(666);
		
		thread.start();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(thread!=null)
			thread.stop();
			*/
		
		/* Small join example
		for (int i = 0; i < 5; i++) {
			MyThread temp = new MyThread(i);
			temp.start();
			try {
				temp.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				System.out.println("Exception in join !!");
			}
		}
		*/
		
		SharedMemory mem = new SharedMemory();
		
		for (int i = 0; i < 5; i++) {
			MyThread temp = new MyThread(i+1, mem);
			temp.start();
		}
	}

}
