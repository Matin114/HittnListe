package examples.zeitSchreibenFX.java.general;

import java.util.Comparator;

public class TimeComparator implements Comparator<String> {
	@Override
	public int compare(String o1, String o2) {
		if(o1.equals(o2)) {
			return 0;
		}
		if (o1.equals("")) {
			return -1;
		}
		if (o2.equals("")) {
			return -1;
		}
		return o1.compareTo(o2);
	}
}
