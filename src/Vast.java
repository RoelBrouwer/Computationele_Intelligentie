public class Vast implements IVakje {
	int n;
	
	public Vast(int n) {
		this.n = n;
	}
	
	public int getWaarde() {
		return n;
	}
	
	public void setWaarde(int n) {
		this.n = n;
	}
	
	public boolean getVariabel() {
		return false;
	}
	
	public String toString()
	{
		return Integer.toString(n);
	}
	public void domeinelementToevoegen(int x){}
	public void domeinelementVerwijderen(int x){}
	public int domeinGrootte(){return -1;}
	public boolean elementInDomein(int x) {return false;}
	public boolean[] getDomein() {return null;}
}