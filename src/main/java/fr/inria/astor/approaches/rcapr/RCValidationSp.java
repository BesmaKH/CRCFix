package fr.inria.astor.approaches.rcapr;

import fr.inria.astor.core.entities.VariantValidationResult;

public class RCValidationSp implements VariantValidationResult  {
	boolean  abscorforall = true;
	boolean relcorforall  = true;
	boolean strictforone = false;
	boolean strictRC=false; 
	

	 
	public int isAC(int []tab1,int []tab2)
	{ //the evolution of the working directory
		boolean  abscorforall = true;
		boolean relcorforall  = true;
		boolean strictforone = false;
		boolean strictRC=false; 
		
	      for(int i=0;relcorforall && i<tab1.length;i++)
	         
	      {      
	                   
	            abscorforall = abscorforall && (tab2[i]==1);
	            
	            relcorforall = relcorforall && ( (tab1[i]==0) || (tab2[i]==1));
	            
	            strictforone = strictforone || ((tab1[i]==0) && (tab2[i]==1));
	            strictRC=strictforone &&  relcorforall;
	          	           	           	
	        }     
	      //just printing to clarify
			
				if(abscorforall) return 1;
				else if(strictRC) return 0;
				return -2;
			
	}	
	
		
	public boolean isSuccessful()
	{ 
		return (abscorforall);
	}


	public int isAC(int[] tab0, int[] tab1, int[] tab2) {
		// TODO Auto-generated method stub
		return 0;
	}


}
