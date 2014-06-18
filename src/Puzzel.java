import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class Puzzel {

	static int n;
	static Random randomGen = new Random();
	public static void main(String[] args) {
		
		BufferedReader reader = null;
		n = 0;
		int[][] sud = {{0}};
		 
		try 
		{ 
			reader = new BufferedReader(new FileReader(args[0]));
			String nextline = reader.readLine();
			n = Integer.parseInt(nextline);
			sud = new int[n*n][n*n];
			nextline = reader.readLine();
			for (int i = 0; i < (n*n); i++)
			{
				String[] comps = nextline.split(" ");
				for (int j = 0; j < (n*n); j++)
				{
					sud[i][j] = Integer.parseInt(comps[j]);
				}
				nextline = reader.readLine();
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
		finally 
		{
			try 
			{
				if (reader != null)
					reader.close();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		System.out.println("\n");
		
		Sudoku sudoku = new Sudoku(n, sud);
		System.out.println(sudoku);
		
		while(sudoku.evalueer() > 0) {
			if(Math.random() < 0.00001) sudoku = new Sudoku(n,sud); //herstart
			
			sudoku = zoekoperator(sudoku);
		}
	}
	
	public static Sudoku zoekoperator(Sudoku sudoku) {
		int eval = sudoku.evalueer();
		Sudoku nieuw;
		
		// swappen gaat per nxn-blok.
		for (int g = 0; g < sudoku.getGrid().length; g += n) 
		{
			for (int h = 0; h < sudoku.getGrid()[0].length; g += n) 
			{
				for (int i = 0; i < n; i++) 
				{
					for (int j = 0; j < n; j++) 
					{
						for (int k = 0; k < n; k++) 
						{
							for (int l = 0; l < n; l++) 
							{
								if (!(i==k && j == l) 
										&& (sudoku.getGrid()[g+i][h+j].getVariabel()) 
										&& (sudoku.getGrid()[g+k][h+l].getVariabel())) 
									// want je kan niet vakje met zichzelf verwisselen, en de vakjes moeten variabel zijn
								{ 
									nieuw = sudoku.verwissel(g+i, h+j, g+k, h+l);
									
									if (nieuw.evalueer() >= eval) 
										return nieuw;
								}
							}
						}
					}
				}
			}
		}
		return sudoku;
	}
	
	public static Sudoku randomZoekOperator(Sudoku sudoku) {
		int eval = sudoku.evalueer();
		Sudoku nieuw;
		int g = randomGen.nextInt(n) * 3;
		int h = randomGen.nextInt(n) * 3;
		int i = randomGen.nextInt(n);
		int j = randomGen.nextInt(n);
		int k = randomGen.nextInt(n);
		int l = randomGen.nextInt(n);
		// Als we een vakje met zichzelf dreigen te gaan verwisselen,
		// of een vast vakje geselecteerd hebben, moeten we onze
		// keuze heroverwegen.
		while ((i == k && j == l) 
				|| !(sudoku.getGrid()[g+i][h+j].getVariabel()) 
				|| !(sudoku.getGrid()[g+k][h+l].getVariabel()))
		{
			i = randomGen.nextInt(n);
			j = randomGen.nextInt(n);
			k = randomGen.nextInt(n);
			l = randomGen.nextInt(n);
		}
		nieuw = sudoku.verwissel(g+i, h+j, g+k, h+l);
		return nieuw;
	}

}
