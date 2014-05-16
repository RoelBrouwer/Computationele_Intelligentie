
public class Sudoku {
	
	IVakje[][] getallen; 
	
	Sudoku(int n)
	{
		getallen = new IVakje[n*n][n*n];
		this.RandomVullen(n);
	}
	
	private void RandomVullen(int n) 
	{
		for (int i = 0; i < n; i++)
		{
			for (int j = 0; j < n; j++)
			{
				getallen[i][j] = new Variabel(3);
			}
		}
	}
}