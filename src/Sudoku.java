
public class Sudoku {
	
	IVakje[][] getallen; 
	Constraint[][][] constraints;
	
	// Constructor voor een wilekeurige begintoestand voor hill-climbing zonder vast vakjes
	Sudoku(int n)
	{
		getallen = new IVakje[n*n][n*n];
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
	
	// Constructor voor hill-climbing
	Sudoku(int n, int[][] sud)
	{
		getallen = new IVakje[n*n][n*n];
		for (int l = 0; l < n; l++)
		{
			for (int k = 0; k < n; k ++)
			{
				boolean[] alreadyplaced = new boolean[n * n];
				for (int q = 0; q < n * n; q++)
				{
					alreadyplaced[q] = false;
				}
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
							//int tevullen = FirstTrue(alreadyplaced);
							int tevullen = randomTrue(alreadyplaced);
							getallen[l * n + i][k * n + j] = new Variabel(tevullen);
							alreadyplaced[tevullen - 1] = true;
						}
					}
				}
			}
		}
	}
	
	// Constructor voor Constraint Satisfaction
	Sudoku(int[][] sud)
	{
		// Variabelen en domeinen
		int n = sud.length;
		getallen = new Variabelen[n*n][n*n];
		boolean[] alltrue = new boolean[n * n]; 
		boolean[] allfalse = new boolean[n * n];
		for (int z = 0; z < n * n; z++)
		{
			alltrue[z] = true;
			allfalse[z] = false;
		}
		for (int l = 0; l < n; l++)
		{
			for (int k = 0; k < n; k ++)
			{
				for (int i = 0; i < n; i++)
				{
					for (int j = 0; j < n; j++)
					{
						int waarde = sud[l * n + i][k * n + j];
						if (waarde == 0)
						{
							getallen[l * n + i][k * n + j] = new Variabelen(0, alltrue, l * n + i, k * n + j);
						}
						else
						{
							allfalse[waarde] = true;
							getallen[l * n + i][k * n + j] = new Variabelen(waarde, allfalse, l * n + i, k * n + j);
							allfalse[waarde] = false;
						}
					}
				}
			}
		}
		// To-Do: Constraints
		// Elk vakje krijgt een eigen array met de constraints waar hij in voorkomt.
		// Dat zijn er 2(n * n - 1) + (n * n - (1 + 2(n - 1))). n * n -1 voor elk vakje in de rij,
		// n * n -1 voor elk vakje in de kolom, en n * n voor alle vakjes in hetzelfde blok,
		// behalve het vakje zelf (1) en de vakjes die al in de rij/kolom zitten (2(n - 1)).
		constraints = new Constraint[n*n][n*n][2 * (n * n - 1) + (n * n - (1 + 2 * (n - 1)))];
		// Eerst alle rijen
		for (int i = 0; i < n * n; i++)
		{
			// Voor alle vakjes in de rij
			for (int j = 0; j < n * n; j++)
			{
				// Voeg een constraint toe voor de vakjes waar het vakje nog geen constraint mee heeft.
				for (int k = j + 1; k < n * n; k++)
				{
					Constraint add = new Constraint(getallen[i][j], getallen[i][k]);
					constraints[i][j][k - 1] = add;
					constraints[i][k][j] = add;
				}
			}
		}
		// Dan de kolommen
		for (int i = 0; i < n * n; i++)
		{
			// Voor alle vakjes in de kolom
			for (int j = 0; j < n * n; j++)
			{
				// Voeg een constraint toe voor de vakjes waar het vakje nog geen constraint mee heeft.
				for (int k = j + 1; k < n * n; k++)
				{
					Constraint add = new Constraint(getallen[i][j], getallen[i][k]);
					constraints[i][j][n * n - 1 + k - 1] = add;
					constraints[i][k][n * n - 1 + j] = add;
				}
			}
		}
		// En de blokken
		// l & k = voor alle blokken
		for (int l = 0; l < n; l++)
		{
			for (int k = 0; k < n; k ++)
			{
				// f & h voor alle vakjes in het blok
				for (int f = 0; f < n; f++)
				{
					for (int h = 0; h < n; h++)
					{
						for (int i = f + 1; i < n; i++)
						{
							for (int j = h + 1; j < n; j++)
							{
								Constraint add = new Constraint(getallen[l * 3 + f][k * 3 + h], getallen[l * 3 + i][k * 3 + j]);
								constraints[l * 3 + f][k * 3 + h][2 * (n * n) - 2 + 2 * f + ((i - f) - 1) * 2 + (j - h - 1)] = add;
								constraints[l * 3 + i][k * 3 + j][2 * (n * n) - 2 + 2 * f + ((i - f) - 1) * 2 + (j - h - 1)] = add;
							}
						}
					}
				}
			}
		}
	}
	
	// Getter
	public IVakje[][] getGrid() {
		return getallen;
	}
	
	// Kiest een willekeurig oningevuld vakje om te vullen (gebruikt voor de constructor voor hill-climbing)
	private int randomTrue(boolean[] array)
	{
		int aantalOngevuld = 0;
		for(int i = 0; i < array.length; i++) if(!array[i]) aantalOngevuld++;
		
		int tevullen = (int)(Math.random() * aantalOngevuld);
		int j = 0;
		for (int i = 0; i < array.length; i++)
		{
			if(!array[i]) {
				if(j==tevullen) return i+1;
				else j++;
			}
		}
		return -1;
	}
	
	// Operator voor het verwisselen van twee vakjes (gebruikt voor het oplossen van problemen met hill-climbing)
	public Sudoku verwissel(int r1, int k1, int r2, int k2) 
	{		
		int temp = this.getallen[r1][k1].getWaarde();
		this.getallen[r1][k1].setWaarde(this.getallen[r2][k2].getWaarde());
		this.getallen[r2][k2].setWaarde(temp);
		return this;
	}
	
	// Evaluatiefunctie
	public int evalueer() 
	{
		int tereturnen = 0;
		boolean[] contains = new boolean[getallen.length];
		// Telt alle ontbrekende cijfers in de rijen op
		for(int i = 0; i < getallen.length; i++) {
			for(int k = 0; k < contains.length;k++) 
				contains[k] = false;
			for(int j = 0; j < getallen[0].length;j++)
				contains[getallen[i][j].getWaarde()-1] = true;
			for(int k = 0; k < contains.length;k++) {
				if(!contains[k]) 
					tereturnen++;
			}
		}
		// Telt alle ontbrekende cijfers in de kolommen op
		for(int i = 0; i < getallen.length; i++) {
			for(int k = 0; k < contains.length;k++) 
				contains[k] = false;
			for(int j = 0; j < getallen[0].length; j++) 
				contains[getallen[j][i].getWaarde()-1] = true;
			for(int k = 0; k < contains.length;k++) {
				if(!contains[k]) 
					tereturnen++;
			}
		}
		return tereturnen;
	}
	
	// toString() functie
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