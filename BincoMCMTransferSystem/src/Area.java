
public class Area {
	
	public double x;
	public double y;
	public double s;
	public double r;
	public int ps;
	
	public Area(double x, double y, double s) {
		this.x = x;
		this.y = y;
		this.s = s;
		this.r = Math.sqrt(s / Math.PI);
		this.ps = (int)s;
	}
	
}
