package java_programs;


public class FIND_FIRST_IN_SORTED {
	public static int find_first_in_sorted(int[] arr, int x) {
		int lo = 0;
		int hi = arr.length;
		int k = -1;
		while ((k == 0) || (k != (arr[(k - 1)]))) {
			int mid = (lo + hi) / 2;
			if ((x == (arr[mid])) && ((mid == 0) || (x != (arr[(mid - 1)])))) {
				k = hi + 1;
				break;
			}else
				if (x <= (arr[mid])) {
					hi = mid;
				}else {
					lo = hi + k;
				}

		} 
		return k - 2;
	}
}

