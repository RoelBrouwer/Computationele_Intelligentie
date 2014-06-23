public class Variabelen implements IVakje {
	private int n;
	private boolean[] domein;
	// "Coordinaten" van het vakje.
	private int x, y;
	
	public Variabelen(int n, boolean[] domein, int x, int y) {
		this.n = n;
		this.domein = domein;
		this.x = x;
		this.y = y;
	}
	
	public int getWaarde() {
		return n;
	}
	
	public void setWaarde(int n) {
		this.n = n;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public boolean getVariabel() {
		return true;
	}
	
	public boolean[] getDomein() {
		return domein;
	}
	
	public boolean gevuld() {
		if (n == 0)
			return false;
		else
			return true;
	}
	
	
	public void domeinelementToevoegen(int x) {
		domein[x - 1] = true;
	}
	
	public void domeinelementVerwijderen(int x) {
		domein[x - 1] = false;
	}
	
	public boolean elementInDomein(int x) {
		return domein[x - 1];
	}
	
	// Geeft terug hoeveel getallen nog in het domein zitten.
	public int domeinGrootte() {
		int count = 0;
		for (int i = 0; i < domein.length; i++)
		{
			if (domein[i])
			{
				count++;
			}
		}
		return count;
	}
	
	public String toString()
	{
		return Integer.toString(n);
	}
}