import java.util.Comparator;

class AreaComparator implements Comparator {
	
	public final int compare(Object pFirst, Object pSecond) {
	
		int aFirstArea = ((Area) pFirst).ps;
		int aSecondArea = ((Area) pSecond).ps;
		int diff = aFirstArea - aSecondArea;
		
		if (diff > 0)
			return -1;
		else if (diff < 0)
			return 1;
		else
			return 0;
	}
}