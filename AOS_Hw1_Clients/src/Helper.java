import java.lang.management.ManagementFactory;


public class Helper {
	
	public void Print(String strtoPrint)
	{
		System.out.println(strtoPrint);
	}
	
	public int GetRandomNo(int n)
	{
		return (int)(Math.random()*100)%n;
	}

	public void Print(String[] files) {
		// TODO Auto-generated method stub
		for(int i=0;i<files.length;i++)
		{
			System.out.println(files[i]);
		}
	}

	public int GetPID() {
		// TODO Auto-generated method stub
		String[] arr = ManagementFactory.getRuntimeMXBean().getName().split("@"); 
		Print("Process Id -" + arr[0]);
		return Integer.parseInt(arr[0]);
	}

}
