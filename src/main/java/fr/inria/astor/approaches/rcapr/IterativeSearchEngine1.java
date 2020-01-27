package fr.inria.astor.approaches.rcapr;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
//import java.awt.image.BufferedImage;
//import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.martiansoftware.jsap.JSAPException;

import fr.inria.astor.core.entities.ModificationPoint;
import fr.inria.astor.core.entities.OperatorInstance;
import fr.inria.astor.core.entities.ProgramVariant;
import fr.inria.astor.core.entities.SuspiciousModificationPoint;
import fr.inria.astor.core.ingredientbased.IngredientBasedApproach;
import fr.inria.astor.core.ingredientbased.IngredientBasedEvolutionaryRepairApproachImpl;
import fr.inria.astor.core.manipulation.MutationSupporter;
import fr.inria.astor.core.manipulation.bytecode.entities.CompilationResult;
import fr.inria.astor.core.setup.ConfigurationProperties;
import fr.inria.astor.core.setup.ProjectRepairFacade;
import fr.inria.astor.core.solutionsearch.navigation.SuspiciousNavigationValues;
import fr.inria.astor.core.stats.Stats;
import fr.inria.astor.core.stats.Stats.GeneralStatEnum;
import fr.inria.astor.util.TimeUtil;
import fr.inria.main.AstorOutputStatus;
import fr.inria.main.evolution.ExtensionPoints;
/*import guru.nidi.graphviz.attribute.Shape;
import guru.nidi.graphviz.attribute.Style;
import guru.nidi.graphviz.engine.Engine;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.engine.Rasterizer;
import guru.nidi.graphviz.model.Graph;
//to daw the graph
import static guru.nidi.graphviz.model.Factory.*;*/
   public class IterativeSearchEngine1 extends fr.inria.astor.core.ingredientbased.ExhaustiveIngredientBasedEngine  implements IngredientBasedApproach  {
   public long time;
   protected int oracleResult=-2;
   protected int oracleResult1=-2;
   protected int oracleResult2=-2;
   public  boolean AC,SMC;
   public boolean MAxGen;
   public boolean timeout;
   public boolean exhausted; 
   public String failt;
   public static int f;
   int ML=1;
   int MM=0;
   public int []nbmp;
   public String cpy;
   public String cpy1;
   public String tabvm[][];
   public String newLocation;
   public String oldLocation;
   public static int tt_size;
   public static List <String> ts;
   public int nbM=0;
   public int modifPointsAnalyzed=0;
   public int operatorExecuted=0 ;
   List<ProgramVariant> l;
   int totalmodfpoints;
   public int []tab0;
   public int []tab1;
   public int []tab2;
   protected RCValidator programValidator=new RCValidator();
   protected RCValidationSp validationResult=new RCValidationSp();
   public ProgramVariant nodesmc;
   //Initialization// for now we use this timeout & max generation or limited by modification point and operator
   int maxMinutes = ConfigurationProperties.getPropertyInt("maxtime");
   int maxGenerations = ConfigurationProperties.
		   getPropertyInt("maxGeneration"); 
   public IterativeSearchEngine1(MutationSupporter mutatorExecutor, ProjectRepairFacade projFacade)
			throws JSAPException {
	     super(mutatorExecutor, projFacade);
	      //this.pluginLoaded = new IngredientBasedPlugInLoader();  
		ConfigurationProperties.properties.setProperty(ExtensionPoints.SUSPICIOUS_NAVIGATION.identifier,
				SuspiciousNavigationValues.INORDER.toString());
			
			}

				     
	@Override
	public void startEvolution() throws Exception {
		if (this.ingredientSpace == null) {
			this.ingredientSpace = IngredientBasedEvolutionaryRepairApproachImpl.getIngredientPool(getTargetElementProcessors());
}
		String s=ConfigurationProperties.getProperty("location")+File.separator+ConfigurationProperties.getProperty("srctestfolder");
		//test suite size
		List <String> ts=getMethod (new File(s));
		tt_size=ts.size();
		log.info(tt_size);
		tab0=new int[tt_size];
		nbmp=new int[tt_size];
		// the correct program has no failure
	   for(int i=0; i<tt_size;i++)tab0[i]=1;
       generationsExecuted = 1;
       AC=false;
       SMC=false;
       exhausted=false;
       MAxGen=false;
       timeout=false;
       dateInitEvolution=new Date();
	   getIngredientSpace().defineSpace(originalVariant);
	       
		l=new ArrayList<ProgramVariant>();
            f=0;
	    	
		   //execute the base program that will serve as a component in the oracle	    
		   validationResult = validateInstance1(this.originalVariant,projectFacade,true,ts);
					  l=MutationV(variants.get(0),ts);
					 
			//   if(AC || MAxGen || timeout || SMC ) return;
			   // else {
		  //multiple mutation	
			//ML++;
			
			//MutationML1(l,ts);  
			//generationsExecuted++;  
			    
         // if(AC || MAxGen || timeout || SMC )return;
        //  else  exhausted=true;
     //  }
	}
	   
   
public void MutationML1(List<ProgramVariant> L,List <String> tss) throws Exception

{  		 List<ProgramVariant> VRL1=l;
         List<ProgramVariant> VRL2=l;
         ProgramVariant v;
         int i1=0; int j1;
        		 
           for (ProgramVariant v1 : VRL1) {	
        	  // j1=0;
        	   //for (ProgramVariant v2 : VRL2) {	
        		//   if(nbmp[i1]<nbmp[j1]&& i1<j1 && v1!=v2)
        		 //  {
		         //try crossover the two variants
        		// we randomly select the generations to apply
        			//   System.out.println(v1.getOperations());
        			   System.out.println("*****"+v1.getOperations());
        			//int rgen1index = v1.getOperations().keySet().size();
        			//int rgen2index = v2.getOperations().keySet().size();
        			//System.out.println(rgen1index+"*********"+rgen2index);
        			//ProgramVariant solutionVariant = variantFactory.createProgramVariantFromAnother(v1,
        					//generationsExecuted);
        			//List<OperatorInstance> ops1 = v1.getOperations((int) v1.getOperations().keySet().toArray()[rgen1index-1]);
        			//List<OperatorInstance> ops2 = v2.getOperations((int) v2.getOperations().keySet().toArray()[rgen2index-1]);
        			//System.out.println(ops1+"***********"+ops2);
        			/*OperatorInstance opinst1 = ops1.get(rgen1index-1);
        			OperatorInstance opinst2 = ops2.get(rgen2index-1);
        			System.out.println(opinst1+"+++++++++++"+opinst2);
        					if (opinst1 == null || opinst2 == null) {
        				log.debug("CO|We could not retrieve a operator");
        				return;
        			}

        			// The generation of both new operators is the Last one.
        			// In the first variant we put the operator taken from the 2 one.
        					solutionVariant.putModificationInstance(rgen1index, opinst2);
        			// In the second variant we put the operator taken from the 1 one.
        			//v2.putModificationInstance( generationsExecuted, opinst1);
        	//
		try {
			  oracleResult1 = processCreatedVariant1(solutionVariant,tss);
			  //oracleResult2 = processCreatedVariant1(v2,tss);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (oracleResult1==1 ) {AC=true;
		v=solutionVariant;
		log.info("-Found Solution, child variant #" +v .getId());
			this.solutions.add(v );
			saveStaticSucessful(v.getId(), operatorExecuted);
			nodesmc=v;
		     //save location of the solution
		    //oldLocation=ConfigurationProperties.properties.getProperty("location");
			//copy src code
		    String bytecodeOutput = projectFacade.getOutDirWithPrefix(ProgramVariant.DEFAULT_ORIGINAL_VARIANT);
		    log.info(bytecodeOutput);
			int j=v.getId();			
			String newst="";
			String []st=bytecodeOutput.split("//");
			for (int i=0; i<st.length-2;i++) newst=st[i]+"/";
			//Save modified bin and src
			cpy=newst+"src/variant-"+j;
			cpy1=newst+"bin/variant-"+j;
			try {
				saveVariant(v);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}							
			this.setOutputStatus(AstorOutputStatus.STOP_BY_PATCH_FOUND);
				log.debug(" modpoint analyzed " + modifPointsAnalyzed + ", operators " + operatorExecuted);
									
			return;}
		
			
		
		//we found s more correct program we add it to potential candidate program
		else if(oracleResult1==0)
		{    SMC=true;
		v=solutionVariant;
		    failt=programValidator.failingList;
			log.info("- SRC Found Solution, child variant number "+ v .getId());
			this.solutions.add(v);
			saveStaticSucessful(v.getId(), operatorExecuted);
			saveVariant(v);
		    nodesmc=v;
		     //save location of the solution
		    //oldLocation=ConfigurationProperties.properties.getProperty("location");
			//copy src code
		    String bytecodeOutput = projectFacade.getOutDirWithPrefix(ProgramVariant.DEFAULT_ORIGINAL_VARIANT);
		    log.info(bytecodeOutput);
			int j=v.getId();			
			String newst="";
			String []st=bytecodeOutput.split("//");
			for (int i=0; i<st.length-2;i++) newst=st[i]+"/";
			//Save modified bin and src
			cpy=newst+"src/variant-"+j;
			cpy1=newst+"bin/variant-"+j;
			time=TimeUtil.deltaInMinutes(dateInitEvolution);
				return ;
		}
				
		

		//undo the operator to try the next one
		//undoOperationToSpoonElement(pointOperation);
		//verify if we can loop			
		   if (!belowMaxTime(dateInitEvolution, maxMinutes)) {timeout=true;
				this.setOutputStatus(AstorOutputStatus.TIME_OUT);

				System.out.println(" number of SRC candiate retained :"+1);
				log.debug("Max time reached");
				return;
			}

			if (maxGenerations <= operatorExecuted) {MAxGen=true;

				this.setOutputStatus(AstorOutputStatus.MAX_GENERATION);
				log.info("Stop-Max operator Applied " + operatorExecuted);
				log.info("modpoint:" + modifPointsAnalyzed + ":all:" + totalmodfpoints + ":operators:"
						+ operatorExecuted);
				return;
			}
	}
        		   j1++;
	
	}
        i1++;   }

    }*/
           }
	return;
}
	

public List<ProgramVariant> MutationV(ProgramVariant parentVariant,List <String> tss) throws Exception

{   tabvm=new String [200000][3];
	int m=0;
	 //save visited variants for multiple mutation if necessary
  
   
	for (ModificationPoint modifPoint : this.getSuspiciousNavigationStrategy()
			.getSortedModificationPointsList(parentVariant.getModificationPoints())) {
   
	modifPointsAnalyzed++;

	log.info("\n MP (" + modifPointsAnalyzed + "/" + parentVariant.getModificationPoints().size()
			+ ") location to modify: " + modifPoint);

	// We create all operators to apply in the modifpoint
	List<OperatorInstance> operatorInstances = createInstancesOfOperators(
			(SuspiciousModificationPoint) modifPoint);

	//log.info("--- List of operators (" + operatorInstances.size() + ") : " + operatorInstances);

	if (operatorInstances == null || operatorInstances.isEmpty())
		continue;

	for (OperatorInstance pointOperation : operatorInstances) {

		operatorExecuted++;
		// We validate the variant after applying the operator
		ProgramVariant solutionVariant = variantFactory.createProgramVariantFromAnother(parentVariant,
				generationsExecuted);
		solutionVariant.getOperations().put(generationsExecuted, Arrays.asList(pointOperation));

		applyNewMutationOperationToSpoonElement(pointOperation);

		log.info("Operator:\n " + pointOperation);
		 //new variant to add to the list			
		
		try {
			  oracleResult = processCreatedVariant1(solutionVariant,tss);
			  if(oracleResult!=-1) {  
			    saveVariant(solutionVariant);
			    String bytecodeOutput = projectFacade.getOutDirWithPrefix(ProgramVariant.DEFAULT_ORIGINAL_VARIANT);
			    int j=solutionVariant .getId();			
				String newst="";
				String []st=bytecodeOutput.split("//");
				for (int i=0; i<st.length-2;i++) newst=st[i]+"/";
				//Save modified src
				cpy=newst+"src/variant-"+j;
				cpy1=newst+"bin/variant-"+j;
				 tabvm[m][0]=cpy;
				 System.out.println(cpy+" folder");
			    tabvm[m][1]=pointOperation+"";
			    System.out.println(pointOperation+" operatio n");
			    tabvm[m][2]=modifPoint+"";
			    System.out.println(tabvm[m][2]+"modification point");
			    m++;
			    nbM=m;
			    
			  
			  
			  }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (oracleResult==1) {AC=true;
		log.info("-Found Solution, child variant #" +solutionVariant .getId());
			this.solutions.add(solutionVariant );
			saveStaticSucessful(solutionVariant .getId(), operatorExecuted);
			nodesmc=solutionVariant;
		     //save location of the solution
		    //oldLocation=ConfigurationProperties.properties.getProperty("location");
			//copy src code
		    String bytecodeOutput = projectFacade.getOutDirWithPrefix(ProgramVariant.DEFAULT_ORIGINAL_VARIANT);
		    log.info(bytecodeOutput);
			int j=solutionVariant .getId();			
			String newst="";
			String []st=bytecodeOutput.split("//");
			for (int i=0; i<st.length-2;i++) newst=st[i]+"/";
			//Save modified bin and src
			cpy=newst+"src/variant-"+j;
			cpy1=newst+"bin/variant-"+j;
			try {
				saveVariant(solutionVariant);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}							
			this.setOutputStatus(AstorOutputStatus.STOP_BY_PATCH_FOUND);
				log.debug(" modpoint analyzed " + modifPointsAnalyzed + ", operators " + operatorExecuted);
									
			return null;
		}
		//we found s more correct program we add it to potential candidate program
		else if(oracleResult==0)
		{    SMC=true;
		     failt=programValidator.failingList;
			log.info("- SRC Found Solution, child variant number "+ solutionVariant .getId());
			this.solutions.add(solutionVariant );
			saveStaticSucessful(solutionVariant .getId(), operatorExecuted);
			saveVariant(solutionVariant);
		    nodesmc=solutionVariant;
		     //save location of the solution
			//copy src code
		    String bytecodeOutput = projectFacade.getOutDirWithPrefix(ProgramVariant.DEFAULT_ORIGINAL_VARIANT);
		    log.info(bytecodeOutput);
			int j=solutionVariant .getId();			
			String newst="";
			String []st=bytecodeOutput.split("//");
			for (int i=0; i<st.length-2;i++) newst=st[i]+"/";
			//Save modified bin and src
			cpy=newst+"src/variant-"+j;
			cpy1=newst+"bin/variant-"+j;
			time=TimeUtil.deltaInMinutes(dateInitEvolution);
				return l;	
				
		}

		//undo the operator to try the next one
		undoOperationToSpoonElement(pointOperation);
		//verify if we can loop			
		   if (!belowMaxTime(dateInitEvolution, maxMinutes)) {timeout=true;
				this.setOutputStatus(AstorOutputStatus.TIME_OUT);

				System.out.println(" number of SRC candiate retained :"+1);
				log.debug("Max time reached");
				return l;
			}

			if (maxGenerations <= operatorExecuted) {MAxGen=true;

				this.setOutputStatus(AstorOutputStatus.MAX_GENERATION);
				log.info("Stop-Max operator Applied " + operatorExecuted);
				log.info("modpoint:" + modifPointsAnalyzed + ":all:" + totalmodfpoints + ":operators:"
						+ operatorExecuted);
				return l;
			}
	}

	}
    
	return l;
}
	



	public int processCreatedVariant1(ProgramVariant programVariant,List <String> tss) throws Exception {

		URL[] originalURL = projectFacade.getClassPathURLforProgramVariant(ProgramVariant.DEFAULT_ORIGINAL_VARIANT);

		CompilationResult compilation = compiler.compile(programVariant, originalURL);

		boolean childCompiles = compilation.compiles();
		programVariant.setCompilation(compilation);

		//storeModifiedModel(programVariant);

		if (ConfigurationProperties.getPropertyBool("saveall")) {
			this.saveVariant(programVariant);
		}

		if (childCompiles) {
				f++;
			log.debug("-The child compiles: id " + programVariant.getId());
			currentStat.increment(GeneralStatEnum.NR_RIGHT_COMPILATIONS);

			validationResult = validateInstance1(programVariant,projectFacade,true,tss);
			int vl=validationResult.isAC(tab1,tab2);log.info("****+++"+vl);
						return (vl);}
			
			 else  {
			log.debug("-The child does NOT compile: " + programVariant.getId() + ", errors: "
					+ compilation.getErrorList());
			currentStat.increment(GeneralStatEnum.NR_FAILLING_COMPILATIONS);
			// In case that the variant a) does not compile; b) compiles but it's
			// not adequate
			Stats.currentStat.getIngredientsStats().storeIngCounterFromFailingPatch(programVariant.getId());
			return -1;
			
		
			 }
	}
		protected RCValidationSp validateInstance1(ProgramVariant variant, ProjectRepairFacade projectFacade,boolean tr,List <String> tss) throws ClassNotFoundException, IOException {
			
			
			if (f==0) tab1 = programValidator.validate1(variant, projectFacade,tss);
			else tab2=programValidator.validate1(variant, projectFacade,tss);
			if (validationResult != null) {
				//variant.setIsSolution(validationResult.isSuccessful());
				variant.setValidationResult((validationResult));
			}
			
			return validationResult;
		}
	

private static List <String> getMethod (File directory)

{   List <String> l=new ArrayList <String>();
	BufferedReader br = null;	
    FileReader fr = null;
        
    if (!directory.exists())
    {
        return l;
    }
    File[] files = directory.listFiles();
    for (File file : files)
    {
        if (file.isDirectory())
        {   
          l.addAll(getMethod(file));
        }
        else if (file.getName().endsWith(".java"))
        { 
	try {


	//br = new BufferedReader(new FileReader(FILENAME));
	fr = new FileReader(file);
	br = new BufferedReader(fr);

	String line;

	while ((line = br.readLine()) != null) {
		       String ln=line.trim();
			if (ln.startsWith("public void test")|ln.startsWith("protected void test")||ln.startsWith("private void test")|| ln.startsWith("void test"))
					{String s=ln.substring((ln.indexOf("void")+5)
							,ln.indexOf("("));
					l.add(s);
					
					}
	}
			
	} catch (IOException e) { 
	
	e.printStackTrace();

}
        }
    }
return l;
	
}

@Override
public void reset(ProgramVariant programVariant) {

	// We remove previous variants
	this.solutions.clear();
	this.variants.clear();
	// We add the new one //TODO: we could do a for
	this.variants.add(programVariant);
	// The parameter becomes the original
	this.originalVariant = programVariant;
	// Removing patch info
	this.patchInfo.clear();

	this.outputStatus = null;

	this.originalVariant.setId(firstgenerationIndex);

	// Saving bytecode
	String bytecodeOutput = projectFacade.getOutDirWithPrefix(ProgramVariant.DEFAULT_ORIGINAL_VARIANT);
	File variantOutputFile = new File(bytecodeOutput);
	System.out.println(variantOutputFile.getAbsolutePath());
	MutationSupporter.currentSupporter.getOutput().saveByteCode(programVariant.getCompilation(), variantOutputFile);

	this.currentStat = Stats.createStat();
}

public  void copyData(File out,File in,String path) throws IOException
{   
	File[]f=out.listFiles();
	if(f==null)
	{
	 if(out.isFile() && ( out.getName().endsWith(".java")||  out.getName().endsWith(".class")))
	  {  path=path+File.separator+out.getName();
		 File f1= new File(in.getPath()+File.separator+path);
		//log.info(f1.getAbsolutePath());
	   // log.info(out);
		//System.out.println(f1.getAbsolutePath()+"** \n"+out.getPath());
	       if(f1.exists())
	    	f1.delete();
	        f1= new File(in.getPath()+File.separator+path);
	    FileUtils.copyFile(out, f1);
    //reitialize path 
    // path="";
	    
     }
	
	}
	else {
		path=path+File.separator+out.getName();
		//System.out.println(path);
		for (int i=0;i<f.length;i++)
	        copyData(f[i],in,path);
	}
	
	
}


//update the code in the input location

}