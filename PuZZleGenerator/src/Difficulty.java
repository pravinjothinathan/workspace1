
/*Defines the problem's Difficulty level*/
public enum Difficulty
	{
	  VeryEasy(20),
	  Easy(50),
	  Medium(100),
	  Hard(200);

	  public int index;

	  private Difficulty(int index) {
	          this.index = index;
	  }
	}