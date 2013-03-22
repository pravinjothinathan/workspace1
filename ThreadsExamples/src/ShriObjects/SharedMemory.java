package ShriObjects;

public class SharedMemory {
	
	Integer Sum;
	
	public SharedMemory()
	{
		Sum = new Integer(0);
	}
	
	public synchronized void add()
	{
		Sum++;
	}
	
	public void Print()
	{
		System.out.println("Sum -> "+ Sum);
	}

}
