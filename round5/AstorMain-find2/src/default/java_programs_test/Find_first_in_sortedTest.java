package java_programs_test;
public class Find_first_in_sortedTest {
    @org.junit.Test(timeout = 2000)
    public void test_0() throws java.lang.Exception {
    	int []arr=new int[]{3,4,5,5,5,5,6};
        int result = java_programs.FIND_FIRST_IN_SORTED.find_first_in_sorted(arr,5);
        org.junit.Assert.assertTrue( oracle(arr,5,result));
       
    }


    @org.junit.Test(timeout = 2000)
    public void test_1() throws java.lang.Exception {
    	int [] arr=new int[]{3,4,5,5,5,5,6};
        int result = java_programs.FIND_FIRST_IN_SORTED.find_first_in_sorted(arr,7);
        org.junit.Assert.assertTrue( oracle(arr,7,result));
    }

    @org.junit.Test(timeout = 2000)
    public void test_2() throws java.lang.Exception {
    	int []arr=new int[]{3,4,5,5,2,2,6};
        int result = java_programs.FIND_FIRST_IN_SORTED.find_first_in_sorted(arr,2);
        org.junit.Assert.assertTrue( oracle(arr,2,result));
    }

    @org.junit.Test(timeout = 2000)
    public void test_3() throws java.lang.Exception {
    	int [] arr=new int[]{3,6,7,9,9,10,14,27};
        int result = java_programs.FIND_FIRST_IN_SORTED.find_first_in_sorted(arr,14);
        org.junit.Assert.assertTrue( oracle(arr,14,result));
    }

    @org.junit.Test(timeout = 2000)
    public void test_4() throws java.lang.Exception {
    	int []arr=new int[]{0,1,6,8,13,14,67,128};
        int result = java_programs.FIND_FIRST_IN_SORTED.find_first_in_sorted(arr,80);
        org.junit.Assert.assertTrue( oracle(arr,80,result));
    }

    @org.junit.Test(timeout = 2000)
    public void test_5() throws java.lang.Exception {
    	int []arr=new int[]{0,1,6,8,13,14,67,128};
        int result = java_programs.FIND_FIRST_IN_SORTED.find_first_in_sorted(arr,67);
         org.junit.Assert.assertTrue( oracle(arr,67,result));
    }

    @org.junit.Test(timeout = 2000)
    public void test_6() throws java.lang.Exception {
    	int []arr=new int[]{0,1,6,8,13,14,67,128};
        int result = java_programs.FIND_FIRST_IN_SORTED.find_first_in_sorted(arr,128);     
        org.junit.Assert.assertTrue( oracle(arr,128,result));
}
   public  boolean domR(int []arr, int x)
    { boolean exist=true; boolean sorted=true;int i;
    for(i=0;i<arr.length;i++) 
    	{if (arr[i]==x) break; }
        if (i>=arr.length) exist=false;
    
    	
    for(i=0;i<arr.length-1;i++) 
    {	if (arr[i]>arr[i+1]) {sorted=false; break;} }  
        return (sorted && exist);
    }
   public  boolean R(int []arr, int x, int i)
    
{      
    	return (arr[i]==x);
}
   public  boolean oracle(int []arr, int x, int i)
    { return ((!domR(arr,x))||R(arr,x,i));
    }
    
}
