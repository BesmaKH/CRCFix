package java_programs;
import java.util.*;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author derricklin
 */
public class FIND_FIRST_IN_SORTED {

	  public static int find_first_in_sorted(int[] arr, int x) {
        int lo = 0;
        int hi = arr.length;
        int k=-1;
        while (lo < hi) {
            int mid = (lo + hi) / 2; // check if this is floor division

            if (x == arr[mid] && (mid == 0 || x != arr[mid-1])) {
               { k=mid;
                 break;}
            } else if (x <= arr[mid-1]) {//fault1
                hi =mid;
            } else {
                lo = mid +1;
            }
        }

        return k;
    }

}


