package client;

import java.util.List;

/**
 * User: Neal Eric
 * Date: 11/13/13
 */
public class ListUtil {
	private ListUtil() {
	}

	public static double sum(List<Double> list) {
		int sum = 0;
		for (double i : list) {
			sum += i;
		}
		return sum;
	}
}
