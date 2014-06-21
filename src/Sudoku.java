
public class Sudoku implements Cloneable {
	
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
	
	//kiest een willekeurig oningevuld vakje om te vullen
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
	
	//evaluatiefunctie
	public int evalueer() {
		int tereturnen = 0;
		boolean[] contains = new boolean[getallen.length];
		
		// telt alle ontbrekende cijfers in de rijen op
		for(int i = 0; i < getallen.length; i++) {
			for(int k = 0; k < contains.length;k++) contains[k] = false;
			
			for(int j = 0; j < getallen[0].length;j++) {
				contains[getallen[i][j].getWaarde()-1] = true;
			}
			
			for(int k = 0; k < contains.length;k++) {
				if(!contains[k]) 
					tereturnen++;
			}
		}
		
		// telt alle ontbrekende cijfers in de kolommen op
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
	
	public Sudoku verwissel(int r1, int k1, int r2, int k2) {
		Sudoku nieuw;
		try {
			nieuw =(Sudoku) this.clone();
		} catch (CloneNotSupportedException e) {throw new Error();}
		
		nieuw.getGrid()[r1][k1].setWaarde(this.getallen[r2][k2].getWaarde());
		
		nieuw.getGrid()[r2][k2].setWaarde(this.getallen[r1][k1].getWaarde());
		
		return nieuw;
	}
	
	public IVakje[][] getGrid() {
		return getallen;
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
