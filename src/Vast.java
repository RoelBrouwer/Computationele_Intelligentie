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
}