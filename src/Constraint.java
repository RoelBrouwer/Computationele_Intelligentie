public class Constraint {
	// "Coordinaten" van de vakjes waar deze constraint over gaat.
	IVakje v1, v2;
	
	public Constraint(IVakje v1, IVakje v2) {
		this.v1 = v1;
		this.v2 = v2;
	}
	
	public IVakje getV1() {
		return v1;
	}
	
	public IVakje getV2() {
		return v2;
	}
	
	public boolean constraintGeschonden() {
		if (v1.getWaarde() == v2.getWaarde() && v1.getWaarde() != 0) {
			return true;
		}
		return false;
	}
}
