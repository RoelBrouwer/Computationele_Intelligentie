public class Variabel implements IVakje {
	int n;
	
	public Variabel(int n) {
		this.n = n;
	}
	
	public int getWaarde() {
		return n;
	}
	
	public void setWaarde(int n) {
		this.n = n;
	}
	
	public boolean getVariabel() {
		return true;
	}
	
	public String toString()
	{
		return Integer.toString(n);
	}
}