
public class Sudoku {
	
	IVakje[][] getallen; 
	
	Sudoku(int n)
	{
		getallen = new IVakje[n*n][n*n];
		this.RandomVullen(n);
	}
	
	Sudoku(int n, int[][] sud)
	{
		getallen = new IVakje[n*n][n*n];
		for (int l = 0; l < n; l++)
		{
			for (int k = 0; k < n; k ++)
			{
				boolean[] alreadyplaced = {false, false, false, false, false, false, false, false, false};
				for (int i = 0; i < n; i++)
				{
					for (int j = 0; j < n; j++)
					{
						int waarde = sud[l * n + i][k * n + j];
						if (waarde != 0)
						{
							alreadyplaced[waarde - 1] = true;
							getallen[l * n + i][k * n + j] = new Vast(waarde);
						}
					}
				}
				for (int i = 0; i < n; i++)
				{
					for (int j = 0; j < n; j++)
					{
						int waarde = sud[l * n + i][k * n + j];
						if (waarde == 0)
						{
							int tevullen = FirstTrue(alreadyplaced);
							getallen[l * n + i][k * n + j] = new Variabel(tevullen);
							alreadyplaced[tevullen - 1] = true;
						}
					}
				}
			}
		}
	}
	
	private int FirstTrue(boolean[] array)
	{
		for (int i = 0; i < array.length; i++)
		{
			if (!array[i])
				return i + 1;
		}
		return -1;
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