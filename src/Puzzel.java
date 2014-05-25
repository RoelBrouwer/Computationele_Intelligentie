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
	}

}