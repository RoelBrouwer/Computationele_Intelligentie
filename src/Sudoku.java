
public class Sudoku {
	
	IVakje[][] getallen; 
	IVakje[] gesorteerd; // Alleen gebruikt voor de tweede variant van backtracking
	Constraint[][][] constraints;
	
	// Constructor die een kopie maakt van het meegegeven object, alleen voor hill-climbing
	Sudoku(Sudoku sudoku)
	{
		this.getallen = new IVakje[sudoku.getallen.length][sudoku.getallen[0].length];
		for (int i = 0; i < getallen.length; i++)
		{
			for (int j = 0; j < getallen[i].length; j++)
			{
				if (sudoku.getallen[i][j].getVariabel())
				{
					this.getallen[i][j] = new Variabel(sudoku.getallen[i][j].getWaarde());
				}
				else
				{
					this.getallen[i][j] = new Vast(sudoku.getallen[i][j].getWaarde());
				}
			}
		}
		this.gesorteerd = sudoku.gesorteerd;
		this.constraints = sudoku.constraints;
	}
	
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
		int n = (int)Math.sqrt(sud.length);
		System.out.println(n);
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
							allfalse[waarde-1] = true;
							getallen[l * n + i][k * n + j] = new Variabelen(waarde, allfalse, l * n + i, k * n + j);
							allfalse[waarde-1] = false;
						}
					}
				}
			}
		}
			// Elk vakje krijgt een eigen array met de constraints waar hij in voorkomt.
		// Dat zijn er 2(n * n - 1) + (n * n - (1 + 2(n - 1))). n * n -1 voor elk vakje in de rij,
		// n * n -1 voor elk vakje in de kolom, en n * n voor alle vakjes in hetzelfde blok,
		// behalve het vakje zelf (1) en de vakjes die al in de rij/kolom zitten (2(n - 1)).
		// = (n - 1) * (n - 1)
		constraints = new Constraint[n*n][n*n][2 * (n * n - 1) + ((n - 1) * (n - 1))];
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
					Constraint add = new Constraint(getallen[j][i], getallen[k][i]);
					constraints[j][i][n * n - 1 + k - 1] = add;
					constraints[k][i][n * n - 1 + j] = add;
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
							for (int j = 0; j < n; j++)
							{
								if (h != j)
								{
									Constraint add = new Constraint(getallen[l * n + f][k * n + h], getallen[l * n + i][k * n + j]);
									constraints[l * n + f][k * n + h][2 * (n * n) - 3 + findNextFree(constraints, l * n + f, k * n + h, n)] = add;
									constraints[l * n + i][k * n + j][2 * (n * n) - 3 + findNextFree(constraints, l * n + i, k * n + j, n)] = add;
								}
							}
						}
					}
				}
			}
		}
	}
	
	private int findNextFree(Constraint[][][] constraints, int first, int second, int n)
	{
		int ret = 0;
		while (constraints[first][second][2 * (n * n) - 3 + ret] != null)
		{
			ret++;
		}
		return ret;
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
	
	// om te kijken of ieder vakje van de sudoku is ingevuld
	public boolean volledigIngevuld() {
		for(int i = 0; i < getallen.length; i++) {
			for(int j = 0; j < getallen[0].length; j++) {
				if(getallen[i][j].getWaarde() == 0) return false;
			}
		}
		return true;
	}
	
	public boolean consistent(int x, int y) {
		for (int i = 0; i < constraints[x][y].length; i++)
		{
			if(constraints[x][y][i].constraintGeschonden())
			{
				return false;
			}
		}
		/*for(int i = 0; i < constraints.length;i++) {
			for(int j = 0; j < constraints[i].length; j++) {
				for(int k = 0; k < constraints[i][j].length; k++) {
					if(constraints[i][j][k].constraintGeschonden()) {
						return false;
					}
				}
			}
		}*/
		return true;
	}
	
	public void bepaalInitieleVrijheidsgraad()
	{
		//TODO
		// Voor elk vakje lopen we alle constraints af die voor het vakje gelden.
		for (int i = 0; i < getallen.length; i++)
		{
			for (int j = 0; j < getallen[i].length; j++)
			{
				for (int c = 0; c < constraints[i][j].length; c++)
				{
					if (constraints[i][j][c].getV1() == getallen[i][j])
					{
						// Als het andere vakje dat meedoet aan het constraint een vast vakje is,
						// verwijderen we de waarde van dat vakje uit het domein van ons vakje.
						if (constraints[i][j][c].getV2().domeinGrootte() == 1)
						{
							getallen[i][j].domeinelementVerwijderen(constraints[i][j][c].getV2().getWaarde());
						}
					}
					else
					{
						// Symmetrisch
						if (constraints[i][j][c].getV1().domeinGrootte() == 1)
						{
							getallen[i][j].domeinelementVerwijderen(constraints[i][j][c].getV1().getWaarde());
						}
					}
				}
			}
		}
	}
	
	public void eenmaligSorteren()
	{
		//TODO
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