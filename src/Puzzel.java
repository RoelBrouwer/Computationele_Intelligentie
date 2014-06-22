import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

public class Puzzel {

	static int n; 
	static int s = 30;
	static Random randomGen = new Random();
	static boolean ils;
	
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
		
		ils = ilsOfRrhc();
		
		Sudoku sudoku = new Sudoku(n, sud);
		System.out.println(sudoku);
		System.out.println(sudoku.evalueer());
		
		if (ils)
		{
			iteratedLocalSearch(sudoku);
		}
		else
		{
			randomRestart(sudoku, sud);
		}
	}
	
	public static void iteratedLocalSearch(Sudoku sudoku)
	{
		Sudoku nieuwesudoku;
		int tellerGlob;
		int tellerLok;
		long tijdGlob = System.currentTimeMillis();
		long tijdLok = System.currentTimeMillis();
		for(tellerGlob = 0, tellerLok = 0; sudoku.evalueer() > 0; tellerGlob++, tellerLok++) 
		{
			nieuwesudoku = zoekoperator(sudoku);
			if(nieuwesudoku == null) 
			{
				//System.out.println("Lokaal maximum gevonden!");
				//System.out.println("Aantal stappen: " + tellerLok);
				//long duurLok = System.currentTimeMillis() - tijdLok;
				//System.out.println("Tijd: " + duurLok);
				//System.out.println("Evalueerfunctie: " + sudoku.evalueer());
				//System.out.println(sudoku);
				//System.out.println();
				// Random walk van s lang, s is bovenaan deze file in te stellen.
				for (int i = 0; i < s; i++)
				{
					sudoku = randomZoekOperator(sudoku);
				}
			}
			else 
				sudoku = nieuwesudoku;
		}
		System.out.println("Globaal optimum: ");
		System.out.println("Aantal stappen in totaal: " + tellerGlob);
		System.out.println("Aantal stappen sinds begin hill-climb: "+ tellerLok);
		long duurGlob = System.currentTimeMillis() - tijdGlob;
		System.out.println("Tijd in totaal: "+ duurGlob);
		System.out.println("Tijd sinds begin hill-climb: " + tijdLok);
		System.out.println(sudoku);
	}
	
	public static void randomRestart(Sudoku sudoku, int[][] sud)
	{
		Sudoku nieuwesudoku;		
		int tellerGlob;
		int tellerLok;
		long tijdGlob = System.currentTimeMillis();
		long tijdLok = System.currentTimeMillis();
		
		for(tellerGlob = 0, tellerLok = 0; sudoku.evalueer() > 0; tellerGlob++, tellerLok++) 
		{
					
			nieuwesudoku = zoekoperator(sudoku);
			//System.out.println(nieuwesudoku.evalueer());
			//System.out.println(nieuwesudoku);
			
			//indien een lokaal optimum wordt bereikt
			if(nieuwesudoku == null) 
			{
				//System.out.println("Lokaal maximum gevonden!");
				//System.out.println("Aantal stappen: " + tellerLok);
				//long duurLok = System.currentTimeMillis() - tijdLok;
				//System.out.println("Tijd: " + duurLok);
				//System.out.println("Evalueerfunctie: " + sudoku.evalueer());
				//System.out.println(sudoku);
				//System.out.println();
				
				// reset voor de volgende Hill-climb search.
				sudoku = new Sudoku(n,sud);
				tellerLok = 0;
				tijdLok = System.currentTimeMillis();
			}
			else sudoku = nieuwesudoku;
		}
		System.out.println("Globaal optimum: ");
		System.out.println("Aantal stappen in totaal: " + tellerGlob);
		System.out.println("Aantal stappen sinds begin hill-climb: "+ tellerLok);
		long duurGlob = System.currentTimeMillis() - tijdGlob;
		System.out.println("Tijd in totaal: "+ duurGlob);
		System.out.println("Tijd sinds begin hill-climb: " + tijdLok);
		System.out.println(sudoku);
	}
	
	public static Sudoku zoekoperator(Sudoku sudoku) {
		int eval = sudoku.evalueer();
		
		// swappen gaat per nxn-blok.

		for (int g = 0; g <= n * (n - 1); g += n) 
		{
			for (int h = 0; h <= n * (n - 1); h += n) 
			{
				for (int i = 0; i < n; i++) 
				{
					for (int j = 0; j < n; j++) 
					{
						for (int k = 0; k < n; k++) 
						{
							for (int l = 0; l < n; l++) 
							{
								//System.out.println("g: " + g + " i: " + i + " h: " + h + " j: " + j);
								if (!(i==k && j == l) 
										&& (sudoku.getGrid()[g+i][h+j].getVariabel()) 
										&& (sudoku.getGrid()[g+k][h+l].getVariabel())) 
									// want je kan niet vakje met zichzelf verwisselen, en de vakjes moeten variabel zijn
								{ 

									// Verwissel de vakjes
									sudoku.verwissel(g+i, h+j, g+k, h+l);
									// Test of het beter is
									if (sudoku.evalueer() < eval)
									{
										return sudoku;
									}
									else
									{
										// Anders: wissel ze terug
										sudoku.verwissel(g+i, h+j, g+k, h+l);
									}
								}
							}
						}
					}
				}
			}
		}
		return null;
	}
	
	public static Sudoku randomZoekOperator(Sudoku sudoku) {
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
		sudoku.verwissel(g + i, h + j, g + k, h + l);
		return sudoku;
	}
	
	private static boolean ilsOfRrhc() {
		System.out.println("Wil je Iterated Local Search? Voer dan een \"i\" in. \nWil je Random Restart Hill-Climbing? Voer dan een \"r\" in.");
		String s = readLine();
		if(s.equals("i")) return true;
		else if(s.equals("r")) return false;
		else return ilsOfRrhc();
	}
	
	public static String readLine()
	{
		String s = "";
		try {
			InputStreamReader irs = new InputStreamReader(System.in);
			BufferedReader in = new BufferedReader(irs);
			s = in.readLine();
		} catch (IOException e) {
			System.out.println(); 
		}
		return s;
	}

}
