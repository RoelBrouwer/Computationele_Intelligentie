import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Puzzel {

	public static void main(String[] args) {
		
		BufferedReader reader = null;
		int n = 0;
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
		
		// swappen gaat per nxn-blok. Is nu nog gehardode als 3x3-blok.
		for(int g = 0; g < sudoku.getGrid().length; g += 3) {
			for(int h = 0; h < sudoku.getGrid()[0].length; g += 3) {
				for(int i = 0; i < 3; i++) {
					for(int j = 0; j < 3; j++) {
						for(int k = 0; k < 3; k++) {
							for(int l = 0; l < 3; l++) {
								if(!(i==k && j == l)) { // want je kan niet vakje met zichzelf verwisselen
									nieuw = sudoku.verwissel(g+i, h+j, g+k, h+l);
									
									if(nieuw.evalueer() >= eval) return nieuw;
								}
							}
						}
					}
				}
			}
		}
		return sudoku;
	}

}
