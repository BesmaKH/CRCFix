package java_programs;


public class FIND_FIRST_IN_SORTED {











	public static int find_first_in_sorted(int[] arr, int x) {
		int lo = 0;
		int hi = arr.length;
		int k = 

		hi + lo; 		while (lo < hi) { 			int mid = 







			(k + 1) / 2; 			if ((x == (arr[mid])) && ((mid == 0) || (x != (arr[(mid - 1)])))) { 				k = hi + 1; 				break;}else 				if (x <= (arr[mid])) { 					hi = mid;}else { 					lo = x + hi;
				}
		} 

		return k - 2;
	}}