public interface IVakje {
	
	public void setWaarde(int n);
	public int getWaarde();
	
	// returnt of het vakje variabel is (true) of dat het gefixeerd is (false)
	public boolean getVariabel();
	
	public void domeinelementToevoegen(int x);
	public void domeinelementVerwijderen(int x);
	public int domeinGrootte();
	public boolean elementInDomein(int x);
	public boolean[] getDomein();
	public void setDomein(boolean[] domein);

}