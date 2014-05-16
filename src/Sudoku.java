
public class Sudoku {
	
	IVakje[][] getallen; 
	
	Sudoku(int n)
	{
		getallen = new IVakje[n*n][n*n];
		this.RandomVullen(n);
	}
	
	private void RandomVullen(int n) 
	{
		for (int l = 0; l < n; l++)
		{
			for (int k = 0; k < n; k ++)
			{
				for (int i = 0; i < n; i++)
				{
					for (int j = 0; j < n; j++)
					{
						getallen[l * n + i][k * n + j] = new Variabel(i*3 + j + 1);
					}
				}
			}
		}
	}
	
	public String toString()
	{
		String string = "";
		for (int i = 0; i < getallen.length; i++)
		{
			for (int j = 0; j < getallen.length; j++)
			{
				string += " " + getallen[i][j];
			}
			string += "\n";
		}
		return string;
	}
	
	
}