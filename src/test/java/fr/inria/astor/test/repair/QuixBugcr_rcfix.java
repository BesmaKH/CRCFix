package fr.inria.astor.test.repair;

import static org.junit.Assert.assertEquals;



import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Ignore;
import org.junit.Test;

import fr.inria.astor.approaches.rcapr.IterativeSearchEngine1;
import fr.inria.astor.approaches.rcapr.RCFixCr;
import fr.inria.astor.approaches.rcapr.RCValidator;
import fr.inria.astor.core.entities.ProgramVariant;
import fr.inria.astor.core.faultlocalization.gzoltar.GZoltarFaultLocalization;
import fr.inria.astor.core.setup.ConfigurationProperties;
import fr.inria.astor.core.stats.PatchHunkStats;
import fr.inria.astor.core.stats.PatchStat;
import fr.inria.astor.core.stats.PatchStat.HunkStatEnum;
import fr.inria.astor.core.stats.PatchStat.PatchStatEnum;
import fr.inria.astor.core.stats.Stats;
import fr.inria.main.CommandSummary;
import fr.inria.main.evolution.AstorMain;

public class QuixBugcr_rcfix {
	public static CommandSummary getQuixBugsCommand(String name) {

		CommandSummary cs = new CommandSummary();
		String dep = new File("./examples/libs/junit-4.4.jar").getAbsolutePath();
		cs.command.put("-javacompliancelevel", "8");
		cs.command.put("-seed", "1");
		cs.command.put("-package", "java_programs");
		cs.command.put("-dependencies", dep);
		cs.command.put("-scope", "package");
		cs.command.put("-mode", "genprog");
		cs.command.put("-id", name);
		cs.command.put("-population", "1");
		cs.command.put("-srcjavafolder", "/src");
		cs.command.put("-srctestfolder", "/src");
		cs.command.put("-binjavafolder", "/bin");
		cs.command.put("-bintestfolder", "/bin");
		cs.command.put("-flthreshold", "0.0");
		cs.command.put("-loglevel", "INFO");
		cs.command.put("-stopfirst", "TRUE");
		cs.command.put("-parameters", "logtestexecution:TRUE:"
				+ "disablelog:TRUE:maxtime:120:autocompile:true:gzoltarpackagetonotinstrument:com.google.gson_engine"
				+ GZoltarFaultLocalization.PACKAGE_SEPARATOR + "java_programs_test");
		cs.command.put("-location", new File("./examples/quixbugs/" + name).getAbsolutePath());

		return cs;
	}
	@Ignore
	public void testMath85() throws Exception {
		AstorMain main1 = new AstorMain();
		String dep = new File("./examples/libs/junit-4.4.jar").getAbsolutePath();
		String[] args = new String[] { "-dependencies", dep, "-mode", "cardumen", "-failing",
				"org.apache.commons.math.distribution.NormalDistributionTest", "-location",
				new File("./examples/quixbugs/find_f").getAbsolutePath(), "-package", "org.apache.commons", "-srcjavafolder",
				"/src/java/", "-srctestfolder", "/src/test/", "-binjavafolder", "/target/classes/java", "-bintestfolder",
				"/target/classes/test", "-javacompliancelevel", "7", "-flthreshold", "0.5", "-stopfirst", "true",
				"-maxgen", "400", "-scope", "package", "-seed", "10", "-loglevel", "INFO" };
		System.out.println(Arrays.toString(args));
		main1.execute(args);

		List<ProgramVariant> solutions = main1.getEngine().getSolutions();
		assertTrue(solutions.size() > 0);

	}
//Cardumen junit Tests on find first in sorted program
	
@Ignore

	public void testfind1() throws Exception {
		//test case where we combine fault
		int nb=0;
		String mxt="180";
		String mxgn="5000";
	    String nbmodp="8";
	    String fl="";
		List<ProgramVariant> solutions=null ;
	    //file where we save smc node
	    String solLocation="./examples/solution";
	    File sol = new File(solLocation);
	    if(sol.exists()) sol.delete();
	    FileUtils.copyDirectory(new File("./examples/quixbugs/find1"), new File(solLocation));
		AstorMain main1 = new AstorMain();
		String dep = new File("./examples/libs/junit-4.4.jar").getAbsolutePath();
		File out = new File(ConfigurationProperties.getProperty("workingDirectory"));
		if (out.exists()) 
		out.delete();	
		CommandSummary command = (getQuixBugsCommand("find1"));
			
		boolean AC;
		boolean SMC;
		boolean timeout;
		boolean mxg;
		boolean exh;
		
		command.command.put("-loglevel", "INFO");
		command.command.put("-mode", "custom");
		command.command.put("-customengine",
				RCFixCr.class.getCanonicalName());
		command.command.put("-validation", RCValidator.class.getCanonicalName());
		command.command.put("-parameters","logtestexecution:TRUE:"
				+ "disablelog:TRUE:maxtime:120:autocompile:true:gzoltarpackagetonotinstrument:com.google.gson_engine"
				+ GZoltarFaultLocalization.PACKAGE_SEPARATOR + "java_programs_test:maxnumbersolutions:1:maxmodificationpoints:"+""+nbmodp);
		
		do {
					
		
		main1.execute(command.flat());
		
		//get result from the previous iteration
		AC= ((RCFixCr)main1.getEngine()).AC;
		SMC= ((RCFixCr)main1.getEngine()).SMC;
		timeout= ((RCFixCr)main1.getEngine()).timeout;
		mxg= ((RCFixCr)main1.getEngine()).MAxGen;
		exh=((RCFixCr)main1.getEngine()).exhausted;
		fl=((RCFixCr)main1.getEngine()).failt;
		String cps=((RCFixCr)main1.getEngine()).cpy;
		int nbop=((RCFixCr)main1.getEngine()).operatorExecuted;
		int g= Integer.parseInt(mxgn)-nbop;
		    if(AC || SMC)
		    {
		    	//find the modified File and update src and bin of the original project & save solution:
            	
	           	 Stats s=main1.getEngine().getCurrentStat();
	           	 PatchStat patchstats = main1.getEngine().getPatchInfo().get(0);

	   			Stats stats = Stats.getCurrentStat();

	   			assertNotNull(stats);

	   			assertEquals(1, main1.getEngine().getPatchInfo().size());

	   			List<PatchHunkStats> hunksApi = (List<PatchHunkStats>) patchstats.getStats().get(PatchStatEnum.HUNKS);

	   			assertNotNull(hunksApi);

	   			PatchHunkStats hunkStats = hunksApi.get(0);
	   			String mdf = hunkStats.getStats().get(HunkStatEnum.MODIFIED_FILE_PATH).toString();
	   			System.out.println("+++++++++++"+mdf+"########");
	   			String nc =mdf.substring(mdf.indexOf("java_programs")+14,mdf.length());
	   		    FileUtils.copyFile(new File(mdf),new File(solLocation+"/src/java_programs/"+nc));
	   			solutions = main1.getEngine().getSolutions();
	   		
	   			
		    }
		    if(AC) break;
			if(SMC) { 
			{ nb++;
			out = new File("round"+nb);
			if (out.exists()) 
			out.delete();
		    int  t=(int) ((Integer.parseInt(mxt))-((RCFixCr)main1.getEngine()).time);		    
		    command.command.put("-location",new File(solLocation).getAbsolutePath());
			command.command.put("-out",out.getAbsolutePath());}}
		   
		
		else {//multiple mutation for each saved variant
			String tab[][]= ((RCFixCr)main1.getEngine()).tabvm;
			int n=((RCFixCr)main1.getEngine()).nbM;	
			System.out.println("round "+n);
			for( int k=160;k<n;k++)
			{				
				  //file where we save new node
			    String solLocation1="./examples/solution"+k;
			    File sol1 = new File(solLocation1);
			    if(sol1.exists()) sol1.delete();
			    FileUtils.copyDirectory(new File(solLocation), new File(solLocation1));
			    //data for the current variant
			    String cpp = tab[k][0];
			    String mpath=tab[k][2];
				String sb2= mpath.substring((mpath.indexOf("=")+1),(mpath.indexOf("line")-1));
				//the line to be modified
				String lin=mpath.substring((mpath.indexOf("line")+6),(mpath.indexOf(",")));
				   System.out.println("path**************"+mpath);
				System.out.println("**"+lin+"**");
				String st=sb2.replace('.','/'); 
	   		    String sb= "/"+st+".java";
	   		    File f1 =new File(cpp+sb);	
	   		    System.out.println(f1.getAbsolutePath());	   		   
	   		    File f2= new File(solLocation1+"/src/"+sb);
	   		    System.out.println(f2.getAbsolutePath());
	   		    String newString=tab[k][1].substring(tab[k][1].indexOf("--> `")+5, tab[k][1].indexOf("` ("));
	   		    System.out.println(newString);
	   		    String oldString=tab[k][1].substring(tab[k][1].indexOf(") `")+3, tab[k][1].indexOf("` -topatch")-1);
	   		    System.out.println(oldString);
	   		    //replace old String by new String not the entire line
	   		    String Op=tab[k][1].substring(tab[k][1].indexOf(":")+2,tab[k][1].indexOf(":("));
	   		    System.out.println("OP"+Op);
	   		  	String oldContent="";
	   		    BufferedReader reader = new BufferedReader(new FileReader(f1));
	   		    String line = reader.readLine();
	   		    int i=0;
	   		    while (line != null)
	   		    {  i++;
	   		    if(i==Integer.parseInt(lin)) 		    
	   		         oldContent = oldContent +line.replaceAll(oldString, newString)+ System.lineSeparator();	   		    
	   		     else	   		      oldContent = oldContent + line + System.lineSeparator();	
	   		      line = reader.readLine();
	   		    }
	   		    //write the new content
	             FileWriter writer = new FileWriter(f2);
	             writer.write(oldContent);
	             reader.close();
	             writer.close(); 
	   		    
				//try to repair this new program at the new location
	   			nb++;
			    //new argument
			     out = new File("round"+nb);
				if (out.exists()) 
					
				  out.delete();
				command.command.put("-location",new File(solLocation1).getAbsolutePath());
				command.command.put("-out",out.getAbsolutePath());
				try {
				main1.execute(command.flat());}
				catch(Exception e) {e.printStackTrace();}
				//check for update
				//get result from the previous iteration
				AC= ((RCFixCr)main1.getEngine()).AC;
				SMC= ((RCFixCr)main1.getEngine()).SMC;
				timeout= ((RCFixCr)main1.getEngine()).timeout;
				mxg= ((RCFixCr)main1.getEngine()).MAxGen;
				exh=((RCFixCr)main1.getEngine()).exhausted;
				fl=((RCFixCr)main1.getEngine()).failt;
				cps=((RCFixCr)main1.getEngine()).cpy;
				nbop=((RCFixCr)main1.getEngine()).operatorExecuted;
				g= Integer.parseInt(mxgn)-nbop;
				    if(AC || SMC)
				    {
				    	//find the modified File and update src and bin of the original project & save solution:
		            	
			           	 Stats s=main1.getEngine().getCurrentStat();
			           	 PatchStat patchstats = main1.getEngine().getPatchInfo().get(0);

			   			Stats stats = Stats.getCurrentStat();

			   			assertNotNull(stats);

			   			assertEquals(1, main1.getEngine().getPatchInfo().size());

			   			List<PatchHunkStats> hunksApi = (List<PatchHunkStats>) patchstats.getStats().get(PatchStatEnum.HUNKS);

			   			assertNotNull(hunksApi);

			   			PatchHunkStats hunkStats = hunksApi.get(0);
			   			String mdf = hunkStats.getStats().get(HunkStatEnum.MODIFIED_FILE_PATH).toString();
			   			String nc =mdf.substring(mdf.indexOf("java_programs")+14,mdf.length());
			   			System.out.println(nc);
			   		    FileUtils.copyFile(new File(mdf),new File(solLocation+"/src/java_programs/"+nc));
			   			solutions = main1.getEngine().getSolutions();
			   		
				    }
					
				     if(SMC) {  	       
			      			nb++;
			      			 out = new File(ConfigurationProperties.getProperty("workingDirectory")+nb);
			     			if (out.exists()) 
			     			out.delete();
			     			command.command.put("-location",new File(solLocation).getAbsolutePath());
							command.command.put("-out",out.getAbsolutePath());
							
				}
				if(AC|| SMC) break;
			}
		
		
		}
		
		}
		
		while(!AC && !timeout && ! mxg && ! exh);
		
			assertEquals(1, solutions.size());
	

		}
@Ignore
	public void testfind2() throws Exception {
		//test case where we combine fault
		int nb=0;
		String mxt="180";
		String mxgn="5000";
	    String nbmodp="8";
	    String fl="";
		List<ProgramVariant> solutions=null ;
	    //file where we save smc node
	    String solLocation="./examples/solution";
	    File sol = new File(solLocation);
	    if(sol.exists()) sol.delete();
	    FileUtils.copyDirectory(new File("./examples/quixbugs/find2"), new File(solLocation));
		AstorMain main1 = new AstorMain();
		String dep = new File("./examples/libs/junit-4.4.jar").getAbsolutePath();
		File out = new File(ConfigurationProperties.getProperty("workingDirectory"));
		if (out.exists()) 
		out.delete();	
		CommandSummary command = (getQuixBugsCommand("find2"));
			
		boolean AC;
		boolean SMC;
		boolean timeout;
		boolean mxg;
		boolean exh;
		
		command.command.put("-loglevel", "INFO");
		command.command.put("-mode", "custom");
		command.command.put("-customengine",
				RCFixCr.class.getCanonicalName());
		command.command.put("-validation", RCValidator.class.getCanonicalName());
		command.command.put("-parameters","logtestexecution:TRUE:"
				+ "disablelog:TRUE:maxtime:120:autocompile:true:gzoltarpackagetonotinstrument:com.google.gson_engine"
				+ GZoltarFaultLocalization.PACKAGE_SEPARATOR + "java_programs_test:maxnumbersolutions:1:maxmodificationpoints:"+""+nbmodp);
		
		do {
					
		
		main1.execute(command.flat());
		
		//get result from the previous iteration
		AC= ((RCFixCr)main1.getEngine()).AC;
		SMC= ((RCFixCr)main1.getEngine()).SMC;
		timeout= ((RCFixCr)main1.getEngine()).timeout;
		mxg= ((RCFixCr)main1.getEngine()).MAxGen;
		exh=((RCFixCr)main1.getEngine()).exhausted;
		fl=((RCFixCr)main1.getEngine()).failt;
		String cps=((RCFixCr)main1.getEngine()).cpy;
		int nbop=((RCFixCr)main1.getEngine()).operatorExecuted;
		int g= Integer.parseInt(mxgn)-nbop;
		    if(AC || SMC)
		    {
		    	//find the modified File and update src and bin of the original project & save solution:
            	
	           	 Stats s=main1.getEngine().getCurrentStat();
	           	 PatchStat patchstats = main1.getEngine().getPatchInfo().get(0);

	   			Stats stats = Stats.getCurrentStat();

	   			assertNotNull(stats);

	   			assertEquals(1, main1.getEngine().getPatchInfo().size());

	   			List<PatchHunkStats> hunksApi = (List<PatchHunkStats>) patchstats.getStats().get(PatchStatEnum.HUNKS);

	   			assertNotNull(hunksApi);

	   			PatchHunkStats hunkStats = hunksApi.get(0);
	   			String mdf = hunkStats.getStats().get(HunkStatEnum.MODIFIED_FILE_PATH).toString();
	   			System.out.println("+++++++++++"+mdf+"########");
	   			String nc =mdf.substring(mdf.indexOf("java_programs")+14,mdf.length());
	   		    FileUtils.copyFile(new File(mdf),new File(solLocation+"/src/java_programs/"+nc));
	   			solutions = main1.getEngine().getSolutions();
	   		
	   			
		    }
		    if(AC) break;
			if(SMC) { 
			{ nb++;
			out = new File("round"+nb);
			if (out.exists()) 
			out.delete();
		    int  t=(int) ((Integer.parseInt(mxt))-((RCFixCr)main1.getEngine()).time);		    
		    command.command.put("-location",new File(solLocation).getAbsolutePath());
			command.command.put("-out",out.getAbsolutePath());}}
		   
		
		else {//multiple mutation for each saved variant
			String tab[][]= ((RCFixCr)main1.getEngine()).tabvm;
			int n=((RCFixCr)main1.getEngine()).nbM;	
			System.out.println("round "+n);
			for( int k=160;k<n;k++)
			{				
				  //file where we save new node
			    String solLocation1="./examples/solution"+k;
			    File sol1 = new File(solLocation1);
			    if(sol1.exists()) sol1.delete();
			    FileUtils.copyDirectory(new File(solLocation), new File(solLocation1));
			    //data for the current variant
			    String cpp = tab[k][0];
			    String mpath=tab[k][2];
				String sb2= mpath.substring((mpath.indexOf("=")+1),(mpath.indexOf("line")-1));
				//the line to be modified
				String lin=mpath.substring((mpath.indexOf("line")+6),(mpath.indexOf(",")));
				   System.out.println("path**************"+mpath);
				System.out.println("**"+lin+"**");
				String st=sb2.replace('.','/'); 
	   		    String sb= "/"+st+".java";
	   		    File f1 =new File(cpp+sb);	
	   		    System.out.println(f1.getAbsolutePath());	   		   
	   		    File f2= new File(solLocation1+"/src/"+sb);
	   		    System.out.println(f2.getAbsolutePath());
	   		    String newString=tab[k][1].substring(tab[k][1].indexOf("--> `")+5, tab[k][1].indexOf("` ("));
	   		    System.out.println(newString);
	   		    String oldString=tab[k][1].substring(tab[k][1].indexOf(") `")+3, tab[k][1].indexOf("` -topatch")-1);
	   		    System.out.println(oldString);
	   		    //replace old String by new String not the entire line
	   		    String Op=tab[k][1].substring(tab[k][1].indexOf(":")+2,tab[k][1].indexOf(":("));
	   		    System.out.println("OP"+Op);
	   		  	String oldContent="";
	   		    BufferedReader reader = new BufferedReader(new FileReader(f1));
	   		    String line = reader.readLine();
	   		    int i=0;
	   		    while (line != null)
	   		    {  i++;
	   		    if(i==Integer.parseInt(lin)) 		    
	   		         oldContent = oldContent +line.replaceAll(oldString, newString)+ System.lineSeparator();	   		    
	   		     else	   		      oldContent = oldContent + line + System.lineSeparator();	
	   		      line = reader.readLine();
	   		    }
	   		    //write the new content
	             FileWriter writer = new FileWriter(f2);
	             writer.write(oldContent);
	             reader.close();
	             writer.close(); 
	   		    
				//try to repair this new program at the new location
	   			nb++;
			    //new argument
			     out = new File("round"+nb);
				if (out.exists()) 
					
				  out.delete();
				command.command.put("-location",new File(solLocation1).getAbsolutePath());
				command.command.put("-out",out.getAbsolutePath());
				try {
				main1.execute(command.flat());}
				catch(Exception e) {e.printStackTrace();}
				//check for update
				//get result from the previous iteration
				AC= ((RCFixCr)main1.getEngine()).AC;
				SMC= ((RCFixCr)main1.getEngine()).SMC;
				timeout= ((RCFixCr)main1.getEngine()).timeout;
				mxg= ((RCFixCr)main1.getEngine()).MAxGen;
				exh=((RCFixCr)main1.getEngine()).exhausted;
				fl=((RCFixCr)main1.getEngine()).failt;
				cps=((RCFixCr)main1.getEngine()).cpy;
				nbop=((RCFixCr)main1.getEngine()).operatorExecuted;
				g= Integer.parseInt(mxgn)-nbop;
				    if(AC || SMC)
				    {
				    	//find the modified File and update src and bin of the original project & save solution:
		            	
			           	 Stats s=main1.getEngine().getCurrentStat();
			           	 PatchStat patchstats = main1.getEngine().getPatchInfo().get(0);

			   			Stats stats = Stats.getCurrentStat();

			   			assertNotNull(stats);

			   			assertEquals(1, main1.getEngine().getPatchInfo().size());

			   			List<PatchHunkStats> hunksApi = (List<PatchHunkStats>) patchstats.getStats().get(PatchStatEnum.HUNKS);

			   			assertNotNull(hunksApi);

			   			PatchHunkStats hunkStats = hunksApi.get(0);
			   			String mdf = hunkStats.getStats().get(HunkStatEnum.MODIFIED_FILE_PATH).toString();
			   			String nc =mdf.substring(mdf.indexOf("java_programs")+14,mdf.length());
			   			System.out.println(nc);
			   		    FileUtils.copyFile(new File(mdf),new File(solLocation+"/src/java_programs/"+nc));
			   			solutions = main1.getEngine().getSolutions();
			   		
				    }
					
				     if(SMC) {  	       
			      			nb++;
			      			 out = new File(ConfigurationProperties.getProperty("workingDirectory")+nb);
			     			if (out.exists()) 
			     			out.delete();
			     			command.command.put("-location",new File(solLocation).getAbsolutePath());
							command.command.put("-out",out.getAbsolutePath());
							
				}
				if(AC|| SMC) break;
			}
		
		
		}
		
		}
		
		while(!AC && !timeout && ! mxg && ! exh);
		
			assertEquals(1, solutions.size());
	

		}

@Ignore
	public void testfind3() throws Exception {
		//test case where we combine fault
		int nb=0;
		String mxt="180";
		String mxgn="5000";
	    String nbmodp="8";
	    String fl="";
		List<ProgramVariant> solutions=null ;
	    //file where we save smc node
	    String solLocation="./examples/solution";
	    File sol = new File(solLocation);
	    if(sol.exists()) sol.delete();
	    FileUtils.copyDirectory(new File("./examples/quixbugs/find3"), new File(solLocation));
		AstorMain main1 = new AstorMain();
		String dep = new File("./examples/libs/junit-4.4.jar").getAbsolutePath();
		File out = new File(ConfigurationProperties.getProperty("workingDirectory"));
		if (out.exists()) 
		out.delete();	
		CommandSummary command = (getQuixBugsCommand("find3"));
			
		boolean AC;
		boolean SMC;
		boolean timeout;
		boolean mxg;
		boolean exh;
		
		command.command.put("-loglevel", "INFO");
		command.command.put("-mode", "custom");
		command.command.put("-customengine",
				RCFixCr.class.getCanonicalName());
		command.command.put("-validation", RCValidator.class.getCanonicalName());
		command.command.put("-parameters","logtestexecution:TRUE:"
				+ "disablelog:TRUE:maxtime:120:autocompile:true:gzoltarpackagetonotinstrument:com.google.gson_engine"
				+ GZoltarFaultLocalization.PACKAGE_SEPARATOR + "java_programs_test:maxnumbersolutions:1:maxmodificationpoints:"+""+nbmodp);
		
		do {
					
		
		main1.execute(command.flat());
		
		//get result from the previous iteration
		AC= ((RCFixCr)main1.getEngine()).AC;
		SMC= ((RCFixCr)main1.getEngine()).SMC;
		timeout= ((RCFixCr)main1.getEngine()).timeout;
		mxg= ((RCFixCr)main1.getEngine()).MAxGen;
		exh=((RCFixCr)main1.getEngine()).exhausted;
		fl=((RCFixCr)main1.getEngine()).failt;
		String cps=((RCFixCr)main1.getEngine()).cpy;
		int nbop=((RCFixCr)main1.getEngine()).operatorExecuted;
		int g= Integer.parseInt(mxgn)-nbop;
		    if(AC || SMC)
		    {
		    	//find the modified File and update src and bin of the original project & save solution:
            	
	           	 Stats s=main1.getEngine().getCurrentStat();
	           	 PatchStat patchstats = main1.getEngine().getPatchInfo().get(0);

	   			Stats stats = Stats.getCurrentStat();

	   			assertNotNull(stats);

	   			assertEquals(1, main1.getEngine().getPatchInfo().size());

	   			List<PatchHunkStats> hunksApi = (List<PatchHunkStats>) patchstats.getStats().get(PatchStatEnum.HUNKS);

	   			assertNotNull(hunksApi);

	   			PatchHunkStats hunkStats = hunksApi.get(0);
	   			String mdf = hunkStats.getStats().get(HunkStatEnum.MODIFIED_FILE_PATH).toString();
	   			System.out.println("+++++++++++"+mdf+"########");
	   			String nc =mdf.substring(mdf.indexOf("java_programs")+14,mdf.length());
	   		    FileUtils.copyFile(new File(mdf),new File(solLocation+"/src/java_programs/"+nc));
	   			solutions = main1.getEngine().getSolutions();
	   		
	   			
		    }
		    if(AC) break;
			if(SMC) { 
			{ nb++;
			out = new File("round"+nb);
			if (out.exists()) 
			out.delete();
		    int  t=(int) ((Integer.parseInt(mxt))-((RCFixCr)main1.getEngine()).time);		    
		    command.command.put("-location",new File(solLocation).getAbsolutePath());
			command.command.put("-out",out.getAbsolutePath());}}
		   
		
		else {//multiple mutation for each saved variant
			String tab[][]= ((RCFixCr)main1.getEngine()).tabvm;
			int n=((RCFixCr)main1.getEngine()).nbM;	
			System.out.println("round "+n);
			for( int k=160;k<n;k++)
			{				
				  //file where we save new node
			    String solLocation1="./examples/solution"+k;
			    File sol1 = new File(solLocation1);
			    if(sol1.exists()) sol1.delete();
			    FileUtils.copyDirectory(new File(solLocation), new File(solLocation1));
			    //data for the current variant
			    String cpp = tab[k][0];
			    String mpath=tab[k][2];
				String sb2= mpath.substring((mpath.indexOf("=")+1),(mpath.indexOf("line")-1));
				//the line to be modified
				String lin=mpath.substring((mpath.indexOf("line")+6),(mpath.indexOf(",")));
				   System.out.println("path**************"+mpath);
				System.out.println("**"+lin+"**");
				String st=sb2.replace('.','/'); 
	   		    String sb= "/"+st+".java";
	   		    File f1 =new File(cpp+sb);	
	   		    System.out.println(f1.getAbsolutePath());	   		   
	   		    File f2= new File(solLocation1+"/src/"+sb);
	   		    System.out.println(f2.getAbsolutePath());
	   		    String newString=tab[k][1].substring(tab[k][1].indexOf("--> `")+5, tab[k][1].indexOf("` ("));
	   		    System.out.println(newString);
	   		    String oldString=tab[k][1].substring(tab[k][1].indexOf(") `")+3, tab[k][1].indexOf("` -topatch")-1);
	   		    System.out.println(oldString);
	   		    //replace old String by new String not the entire line
	   		    String Op=tab[k][1].substring(tab[k][1].indexOf(":")+2,tab[k][1].indexOf(":("));
	   		    System.out.println("OP"+Op);
	   		  	String oldContent="";
	   		    BufferedReader reader = new BufferedReader(new FileReader(f1));
	   		    String line = reader.readLine();
	   		    int i=0;
	   		    while (line != null)
	   		    {  i++;
	   		    if(i==Integer.parseInt(lin)) 		    
	   		         oldContent = oldContent +line.replaceAll(oldString, newString)+ System.lineSeparator();	   		    
	   		     else	   		      oldContent = oldContent + line + System.lineSeparator();	
	   		      line = reader.readLine();
	   		    }
	   		    //write the new content
	             FileWriter writer = new FileWriter(f2);
	             writer.write(oldContent);
	             reader.close();
	             writer.close(); 
	   		    
				//try to repair this new program at the new location
	   			nb++;
			    //new argument
			     out = new File("round"+nb);
				if (out.exists()) 
					
				  out.delete();
				command.command.put("-location",new File(solLocation1).getAbsolutePath());
				command.command.put("-out",out.getAbsolutePath());
				try {
				main1.execute(command.flat());}
				catch(Exception e) {e.printStackTrace();}
				//check for update
				//get result from the previous iteration
				AC= ((RCFixCr)main1.getEngine()).AC;
				SMC= ((RCFixCr)main1.getEngine()).SMC;
				timeout= ((RCFixCr)main1.getEngine()).timeout;
				mxg= ((RCFixCr)main1.getEngine()).MAxGen;
				exh=((RCFixCr)main1.getEngine()).exhausted;
				fl=((RCFixCr)main1.getEngine()).failt;
				cps=((RCFixCr)main1.getEngine()).cpy;
				nbop=((RCFixCr)main1.getEngine()).operatorExecuted;
				g= Integer.parseInt(mxgn)-nbop;
				    if(AC || SMC)
				    {
				    	//find the modified File and update src and bin of the original project & save solution:
		            	
			           	 Stats s=main1.getEngine().getCurrentStat();
			           	 PatchStat patchstats = main1.getEngine().getPatchInfo().get(0);

			   			Stats stats = Stats.getCurrentStat();

			   			assertNotNull(stats);

			   			assertEquals(1, main1.getEngine().getPatchInfo().size());

			   			List<PatchHunkStats> hunksApi = (List<PatchHunkStats>) patchstats.getStats().get(PatchStatEnum.HUNKS);

			   			assertNotNull(hunksApi);

			   			PatchHunkStats hunkStats = hunksApi.get(0);
			   			String mdf = hunkStats.getStats().get(HunkStatEnum.MODIFIED_FILE_PATH).toString();
			   			String nc =mdf.substring(mdf.indexOf("java_programs")+14,mdf.length());
			   			System.out.println(nc);
			   		    FileUtils.copyFile(new File(mdf),new File(solLocation+"/src/java_programs/"+nc));
			   			solutions = main1.getEngine().getSolutions();

			   		
				    }
					
				     if(SMC) {  	       
			      			nb++;
			      			 out = new File(ConfigurationProperties.getProperty("workingDirectory")+nb);
			     			if (out.exists()) 
			     			out.delete();
			     			command.command.put("-location",new File(solLocation).getAbsolutePath());
							command.command.put("-out",out.getAbsolutePath());
							
				}
				if(AC|| SMC) break;
			}
		
		
		}
		
		}
		
		while(!AC && !timeout && ! mxg && ! exh);
		
			assertEquals(1, solutions.size());
	

		}

@Ignore
	public void testfind4() throws Exception {
		//test case where we combine fault
		int nb=0;
		String mxt="180";
		String mxgn="5000";
	    String nbmodp="8";
	    String fl="";
		List<ProgramVariant> solutions=null ;
	    //file where we save smc node
	    String solLocation="./examples/solution";
	    File sol = new File(solLocation);
	    if(sol.exists()) sol.delete();
	    FileUtils.copyDirectory(new File("./examples/quixbugs/find4"), new File(solLocation));
		AstorMain main1 = new AstorMain();
		String dep = new File("./examples/libs/junit-4.4.jar").getAbsolutePath();
		File out = new File(ConfigurationProperties.getProperty("workingDirectory"));
		if (out.exists()) 
		out.delete();	
		CommandSummary command = (getQuixBugsCommand("find4"));
			
		boolean AC;
		boolean SMC;
		boolean timeout;
		boolean mxg;
		boolean exh;
		
		command.command.put("-loglevel", "INFO");
		command.command.put("-mode", "custom");
		command.command.put("-customengine",
				RCFixCr.class.getCanonicalName());
		command.command.put("-validation", RCValidator.class.getCanonicalName());
		command.command.put("-parameters","logtestexecution:TRUE:"
				+ "disablelog:TRUE:maxtime:120:autocompile:true:gzoltarpackagetonotinstrument:com.google.gson_engine"
				+ GZoltarFaultLocalization.PACKAGE_SEPARATOR + "java_programs_test:maxnumbersolutions:1:maxmodificationpoints:"+""+nbmodp);
		
		do {
					
		
		main1.execute(command.flat());
		
		//get result from the previous iteration
		AC= ((RCFixCr)main1.getEngine()).AC;
		SMC= ((RCFixCr)main1.getEngine()).SMC;
		timeout= ((RCFixCr)main1.getEngine()).timeout;
		mxg= ((RCFixCr)main1.getEngine()).MAxGen;
		exh=((RCFixCr)main1.getEngine()).exhausted;
		fl=((RCFixCr)main1.getEngine()).failt;
		String cps=((RCFixCr)main1.getEngine()).cpy;
		int nbop=((RCFixCr)main1.getEngine()).operatorExecuted;
		int g= Integer.parseInt(mxgn)-nbop;
		    if(AC || SMC)
		    {
		    	//find the modified File and update src and bin of the original project & save solution:
            	
	           	 Stats s=main1.getEngine().getCurrentStat();
	           	 PatchStat patchstats = main1.getEngine().getPatchInfo().get(0);

	   			Stats stats = Stats.getCurrentStat();

	   			assertNotNull(stats);

	   			assertEquals(1, main1.getEngine().getPatchInfo().size());

	   			List<PatchHunkStats> hunksApi = (List<PatchHunkStats>) patchstats.getStats().get(PatchStatEnum.HUNKS);

	   			assertNotNull(hunksApi);

	   			PatchHunkStats hunkStats = hunksApi.get(0);
	   			String mdf = hunkStats.getStats().get(HunkStatEnum.MODIFIED_FILE_PATH).toString();
	   			System.out.println("+++++++++++"+mdf+"########");
	   			String nc =mdf.substring(mdf.indexOf("java_programs")+14,mdf.length());
	   		    FileUtils.copyFile(new File(mdf),new File(solLocation+"/src/java_programs/"+nc));
	   			solutions = main1.getEngine().getSolutions();
	   		
	   			
		    }
		    if(AC) break;
			if(SMC) { 
			{ nb++;
			out = new File("round"+nb);
			if (out.exists()) 
			out.delete();
		    int  t=(int) ((Integer.parseInt(mxt))-((RCFixCr)main1.getEngine()).time);		    
		    command.command.put("-location",new File(solLocation).getAbsolutePath());
			command.command.put("-out",out.getAbsolutePath());}}
		   
		
		else {//multiple mutation for each saved variant
			String tab[][]= ((RCFixCr)main1.getEngine()).tabvm;
			int n=((RCFixCr)main1.getEngine()).nbM;	
			System.out.println("round "+n);
			for( int k=160;k<n;k++)
			{				
				  //file where we save new node
			    String solLocation1="./examples/solution"+k;
			    File sol1 = new File(solLocation1);
			    if(sol1.exists()) sol1.delete();
			    FileUtils.copyDirectory(new File(solLocation), new File(solLocation1));
			    //data for the current variant
			    String cpp = tab[k][0];
			    String mpath=tab[k][2];
				String sb2= mpath.substring((mpath.indexOf("=")+1),(mpath.indexOf("line")-1));
				//the line to be modified
				String lin=mpath.substring((mpath.indexOf("line")+6),(mpath.indexOf(",")));
				   System.out.println("path**************"+mpath);
				System.out.println("**"+lin+"**");
				String st=sb2.replace('.','/'); 
	   		    String sb= "/"+st+".java";
	   		    File f1 =new File(cpp+sb);	
	   		    System.out.println(f1.getAbsolutePath());	   		   
	   		    File f2= new File(solLocation1+"/src/"+sb);
	   		    System.out.println(f2.getAbsolutePath());
	   		    String newString=tab[k][1].substring(tab[k][1].indexOf("--> `")+5, tab[k][1].indexOf("` ("));
	   		    System.out.println(newString);
	   		    String oldString=tab[k][1].substring(tab[k][1].indexOf(") `")+3, tab[k][1].indexOf("` -topatch")-1);
	   		    System.out.println(oldString);
	   		    //replace old String by new String not the entire line
	   		    String Op=tab[k][1].substring(tab[k][1].indexOf(":")+2,tab[k][1].indexOf(":("));
	   		    System.out.println("OP"+Op);
	   		  	String oldContent="";
	   		    BufferedReader reader = new BufferedReader(new FileReader(f1));
	   		    String line = reader.readLine();
	   		    int i=0;
	   		    while (line != null)
	   		    {  i++;
	   		    if(i==Integer.parseInt(lin)) 		    
	   		         oldContent = oldContent +line.replaceAll(oldString, newString)+ System.lineSeparator();	   		    
	   		     else	   		      oldContent = oldContent + line + System.lineSeparator();	
	   		      line = reader.readLine();
	   		    }
	   		    //write the new content
	             FileWriter writer = new FileWriter(f2);
	             writer.write(oldContent);
	             reader.close();
	             writer.close(); 
	   		    
				//try to repair this new program at the new location
	   			nb++;
			    //new argument
			     out = new File("round"+nb);
				if (out.exists()) 
					
				  out.delete();
				command.command.put("-location",new File(solLocation1).getAbsolutePath());
				command.command.put("-out",out.getAbsolutePath());
				try {
				main1.execute(command.flat());}
				catch(Exception e) {e.printStackTrace();}
				//check for update
				//get result from the previous iteration
				AC= ((RCFixCr)main1.getEngine()).AC;
				SMC= ((RCFixCr)main1.getEngine()).SMC;
				timeout= ((RCFixCr)main1.getEngine()).timeout;
				mxg= ((RCFixCr)main1.getEngine()).MAxGen;
				exh=((RCFixCr)main1.getEngine()).exhausted;
				fl=((RCFixCr)main1.getEngine()).failt;
				cps=((RCFixCr)main1.getEngine()).cpy;
				nbop=((RCFixCr)main1.getEngine()).operatorExecuted;
				g= Integer.parseInt(mxgn)-nbop;
				    if(AC || SMC)
				    {
				    	//find the modified File and update src and bin of the original project & save solution:
		            	
			           	 Stats s=main1.getEngine().getCurrentStat();
			           	 PatchStat patchstats = main1.getEngine().getPatchInfo().get(0);

			   			Stats stats = Stats.getCurrentStat();

			   			assertNotNull(stats);

			   			assertEquals(1, main1.getEngine().getPatchInfo().size());

			   			List<PatchHunkStats> hunksApi = (List<PatchHunkStats>) patchstats.getStats().get(PatchStatEnum.HUNKS);

			   			assertNotNull(hunksApi);

			   			PatchHunkStats hunkStats = hunksApi.get(0);
			   			String mdf = hunkStats.getStats().get(HunkStatEnum.MODIFIED_FILE_PATH).toString();
			   			String nc =mdf.substring(mdf.indexOf("java_programs")+14,mdf.length());
			   			System.out.println(nc);
			   		    FileUtils.copyFile(new File(mdf),new File(solLocation+"/src/java_programs/"+nc));
			   			solutions = main1.getEngine().getSolutions();

			   		
				    }
					
				     if(SMC) {  	       
			      			nb++;
			      			 out = new File(ConfigurationProperties.getProperty("workingDirectory")+nb);
			     			if (out.exists()) 
			     			out.delete();
			     			command.command.put("-location",new File(solLocation).getAbsolutePath());
							command.command.put("-out",out.getAbsolutePath());
							
				}
				if(AC|| SMC) break;
			}
		
		
		}
		
		}
		
		while(!AC && !timeout && ! mxg && ! exh);
		
			assertEquals(1, solutions.size());
	

		}

@Ignore
	public void testfind5() throws Exception {
		//test case where we combine fault
		int nb=0;
		String mxt="180";
		String mxgn="5000";
	    String nbmodp="8";
	    String fl="";
		List<ProgramVariant> solutions=null ;
	    //file where we save smc node
	    String solLocation="./examples/solution";
	    File sol = new File(solLocation);
	    if(sol.exists()) sol.delete();
	    FileUtils.copyDirectory(new File("./examples/quixbugs/find5"), new File(solLocation));
		AstorMain main1 = new AstorMain();
		String dep = new File("./examples/libs/junit-4.4.jar").getAbsolutePath();
		File out = new File(ConfigurationProperties.getProperty("workingDirectory"));
		if (out.exists()) 
		out.delete();	
		CommandSummary command = (getQuixBugsCommand("find5"));
			
		boolean AC;
		boolean SMC;
		boolean timeout;
		boolean mxg;
		boolean exh;
		
		command.command.put("-loglevel", "INFO");
		command.command.put("-mode", "custom");
		command.command.put("-customengine",
				RCFixCr.class.getCanonicalName());
		command.command.put("-validation", RCValidator.class.getCanonicalName());
		command.command.put("-parameters","logtestexecution:TRUE:"
				+ "disablelog:TRUE:maxtime:120:autocompile:true:gzoltarpackagetonotinstrument:com.google.gson_engine"
				+ GZoltarFaultLocalization.PACKAGE_SEPARATOR + "java_programs_test:maxnumbersolutions:1:maxmodificationpoints:"+""+nbmodp);
		
		do {
					
		
		main1.execute(command.flat());
		
		//get result from the previous iteration
		AC= ((RCFixCr)main1.getEngine()).AC;
		SMC= ((RCFixCr)main1.getEngine()).SMC;
		timeout= ((RCFixCr)main1.getEngine()).timeout;
		mxg= ((RCFixCr)main1.getEngine()).MAxGen;
		exh=((RCFixCr)main1.getEngine()).exhausted;
		fl=((RCFixCr)main1.getEngine()).failt;
		String cps=((RCFixCr)main1.getEngine()).cpy;
		int nbop=((RCFixCr)main1.getEngine()).operatorExecuted;
		int g= Integer.parseInt(mxgn)-nbop;
		    if(AC || SMC)
		    {
		    	//find the modified File and update src and bin of the original project & save solution:
            	
	           	 Stats s=main1.getEngine().getCurrentStat();
	           	 PatchStat patchstats = main1.getEngine().getPatchInfo().get(0);

	   			Stats stats = Stats.getCurrentStat();

	   			assertNotNull(stats);

	   			assertEquals(1, main1.getEngine().getPatchInfo().size());

	   			List<PatchHunkStats> hunksApi = (List<PatchHunkStats>) patchstats.getStats().get(PatchStatEnum.HUNKS);

	   			assertNotNull(hunksApi);

	   			PatchHunkStats hunkStats = hunksApi.get(0);
	   			String mdf = hunkStats.getStats().get(HunkStatEnum.MODIFIED_FILE_PATH).toString();
	   			System.out.println("+++++++++++"+mdf+"########");
	   			String nc =mdf.substring(mdf.indexOf("java_programs")+14,mdf.length());
	   		    FileUtils.copyFile(new File(mdf),new File(solLocation+"/src/java_programs/"+nc));
	   			solutions = main1.getEngine().getSolutions();
	   		
	   			
		    }
		    if(AC) break;
			if(SMC) { 
			{ nb++;
			out = new File("round"+nb);
			if (out.exists()) 
			out.delete();
		    int  t=(int) ((Integer.parseInt(mxt))-((RCFixCr)main1.getEngine()).time);		    
		    command.command.put("-location",new File(solLocation).getAbsolutePath());
			command.command.put("-out",out.getAbsolutePath());}}
		   
		
		else {//multiple mutation for each saved variant
			String tab[][]= ((RCFixCr)main1.getEngine()).tabvm;
			int n=((RCFixCr)main1.getEngine()).nbM;	
			System.out.println("round "+n);
			for( int k=160;k<n;k++)
			{				
				  //file where we save new node
			    String solLocation1="./examples/solution"+k;
			    File sol1 = new File(solLocation1);
			    if(sol1.exists()) sol1.delete();
			    FileUtils.copyDirectory(new File(solLocation), new File(solLocation1));
			    //data for the current variant
			    String cpp = tab[k][0];
			    String mpath=tab[k][2];
				String sb2= mpath.substring((mpath.indexOf("=")+1),(mpath.indexOf("line")-1));
				//the line to be modified
				String lin=mpath.substring((mpath.indexOf("line")+6),(mpath.indexOf(",")));
				   System.out.println("path**************"+mpath);
				System.out.println("**"+lin+"**");
				String st=sb2.replace('.','/'); 
	   		    String sb= "/"+st+".java";
	   		    File f1 =new File(cpp+sb);	
	   		    System.out.println(f1.getAbsolutePath());	   		   
	   		    File f2= new File(solLocation1+"/src/"+sb);
	   		    System.out.println(f2.getAbsolutePath());
	   		    String newString=tab[k][1].substring(tab[k][1].indexOf("--> `")+5, tab[k][1].indexOf("` ("));
	   		    System.out.println(newString);
	   		    String oldString=tab[k][1].substring(tab[k][1].indexOf(") `")+3, tab[k][1].indexOf("` -topatch")-1);
	   		    System.out.println(oldString);
	   		    //replace old String by new String not the entire line
	   		    String Op=tab[k][1].substring(tab[k][1].indexOf(":")+2,tab[k][1].indexOf(":("));
	   		    System.out.println("OP"+Op);
	   		  	String oldContent="";
	   		    BufferedReader reader = new BufferedReader(new FileReader(f1));
	   		    String line = reader.readLine();
	   		    int i=0;
	   		    while (line != null)
	   		    {  i++;
	   		    if(i==Integer.parseInt(lin)) 		    
	   		         oldContent = oldContent +line.replaceAll(oldString, newString)+ System.lineSeparator();	   		    
	   		     else	   		      oldContent = oldContent + line + System.lineSeparator();	
	   		      line = reader.readLine();
	   		    }
	   		    //write the new content
	             FileWriter writer = new FileWriter(f2);
	             writer.write(oldContent);
	             reader.close();
	             writer.close(); 
	   		    
				//try to repair this new program at the new location
	   			nb++;
			    //new argument
			     out = new File("round"+nb);
				if (out.exists()) 
					
				  out.delete();
				command.command.put("-location",new File(solLocation1).getAbsolutePath());
				command.command.put("-out",out.getAbsolutePath());
				try {
				main1.execute(command.flat());}
				catch(Exception e) {e.printStackTrace();}
				//check for update
				//get result from the previous iteration
				AC= ((RCFixCr)main1.getEngine()).AC;
				SMC= ((RCFixCr)main1.getEngine()).SMC;
				timeout= ((RCFixCr)main1.getEngine()).timeout;
				mxg= ((RCFixCr)main1.getEngine()).MAxGen;
				exh=((RCFixCr)main1.getEngine()).exhausted;
				fl=((RCFixCr)main1.getEngine()).failt;
				cps=((RCFixCr)main1.getEngine()).cpy;
				nbop=((RCFixCr)main1.getEngine()).operatorExecuted;
				g= Integer.parseInt(mxgn)-nbop;
				    if(AC || SMC)
				    {
				    	//find the modified File and update src and bin of the original project & save solution:
		            	
			           	 Stats s=main1.getEngine().getCurrentStat();
			           	 PatchStat patchstats = main1.getEngine().getPatchInfo().get(0);

			   			Stats stats = Stats.getCurrentStat();

			   			assertNotNull(stats);

			   			assertEquals(1, main1.getEngine().getPatchInfo().size());

			   			List<PatchHunkStats> hunksApi = (List<PatchHunkStats>) patchstats.getStats().get(PatchStatEnum.HUNKS);

			   			assertNotNull(hunksApi);

			   			PatchHunkStats hunkStats = hunksApi.get(0);
			   			String mdf = hunkStats.getStats().get(HunkStatEnum.MODIFIED_FILE_PATH).toString();
			   			String nc =mdf.substring(mdf.indexOf("java_programs")+14,mdf.length());
			   			System.out.println(nc);
			   		    FileUtils.copyFile(new File(mdf),new File(solLocation+"/src/java_programs/"+nc));
			   			solutions = main1.getEngine().getSolutions();

			   		
				    }
					
				     if(SMC) {  	       
			      			nb++;
			      			 out = new File(ConfigurationProperties.getProperty("workingDirectory")+nb);
			     			if (out.exists()) 
			     			out.delete();
			     			command.command.put("-location",new File(solLocation).getAbsolutePath());
							command.command.put("-out",out.getAbsolutePath());
							
				}
				if(AC|| SMC) break;
			}
		
		
		}
		
		}
		
		while(!AC && !timeout && ! mxg && ! exh);
		
			assertEquals(1, solutions.size());
	

		}

@Test
	public void testfind6() throws Exception {
		//test case where we combine fault
		int nb=0;
		String mxt="180";
		String mxgn="5000";
	    String nbmodp="8";
	    String fl="";
		List<ProgramVariant> solutions=null ;
	    //file where we save smc node
	    String solLocation="./examples/solution";
	    File sol = new File(solLocation);
	    if(sol.exists()) sol.delete();
	    FileUtils.copyDirectory(new File("./examples/quixbugs/find6"), new File(solLocation));
		AstorMain main1 = new AstorMain();
		String dep = new File("./examples/libs/junit-4.4.jar").getAbsolutePath();
		File out = new File(ConfigurationProperties.getProperty("workingDirectory"));
		if (out.exists()) 
		out.delete();	
		CommandSummary command = (getQuixBugsCommand("find6"));
			
		boolean AC;
		boolean SMC;
		boolean timeout;
		boolean mxg;
		boolean exh;
		
		command.command.put("-loglevel", "INFO");
		command.command.put("-mode", "custom");
		command.command.put("-customengine",
				RCFixCr.class.getCanonicalName());
		command.command.put("-validation", RCValidator.class.getCanonicalName());
		command.command.put("-parameters","logtestexecution:TRUE:"
				+ "disablelog:TRUE:maxtime:120:autocompile:true:gzoltarpackagetonotinstrument:com.google.gson_engine"
				+ GZoltarFaultLocalization.PACKAGE_SEPARATOR + "java_programs_test:maxnumbersolutions:1:maxmodificationpoints:"+""+nbmodp);
		
		do {
					
		
		main1.execute(command.flat());
		
		//get result from the previous iteration
		AC= ((RCFixCr)main1.getEngine()).AC;
		SMC= ((RCFixCr)main1.getEngine()).SMC;
		timeout= ((RCFixCr)main1.getEngine()).timeout;
		mxg= ((RCFixCr)main1.getEngine()).MAxGen;
		exh=((RCFixCr)main1.getEngine()).exhausted;
		fl=((RCFixCr)main1.getEngine()).failt;
		String cps=((RCFixCr)main1.getEngine()).cpy;
		int nbop=((RCFixCr)main1.getEngine()).operatorExecuted;
		int g= Integer.parseInt(mxgn)-nbop;
		    if(AC || SMC)
		    {
		    	//find the modified File and update src and bin of the original project & save solution:
            	
	           	 Stats s=main1.getEngine().getCurrentStat();
	           	 PatchStat patchstats = main1.getEngine().getPatchInfo().get(0);

	   			Stats stats = Stats.getCurrentStat();

	   			assertNotNull(stats);

	   			assertEquals(1, main1.getEngine().getPatchInfo().size());

	   			List<PatchHunkStats> hunksApi = (List<PatchHunkStats>) patchstats.getStats().get(PatchStatEnum.HUNKS);

	   			assertNotNull(hunksApi);

	   			PatchHunkStats hunkStats = hunksApi.get(0);
	   			String mdf = hunkStats.getStats().get(HunkStatEnum.MODIFIED_FILE_PATH).toString();
	   			System.out.println("+++++++++++"+mdf+"########");
	   			String nc =mdf.substring(mdf.indexOf("java_programs")+14,mdf.length());
	   		    FileUtils.copyFile(new File(mdf),new File(solLocation+"/src/java_programs/"+nc));
	   			solutions = main1.getEngine().getSolutions();
	   		
	   			
		    }
		    if(AC) break;
			if(SMC) { 
			{ nb++;
			out = new File("round"+nb);
			if (out.exists()) 
			out.delete();
		    int  t=(int) ((Integer.parseInt(mxt))-((RCFixCr)main1.getEngine()).time);		    
		    command.command.put("-location",new File(solLocation).getAbsolutePath());
			command.command.put("-out",out.getAbsolutePath());}}
		   
		
		else {//multiple mutation for each saved variant
			String tab[][]= ((RCFixCr)main1.getEngine()).tabvm;
			int n=((RCFixCr)main1.getEngine()).nbM;	
			System.out.println("round "+n);
			for( int k=160;k<n;k++)
			{				
				  //file where we save new node
			    String solLocation1="./examples/solution"+k;
			    File sol1 = new File(solLocation1);
			    if(sol1.exists()) sol1.delete();
			    FileUtils.copyDirectory(new File(solLocation), new File(solLocation1));
			    //data for the current variant
			    String cpp = tab[k][0];
			    String mpath=tab[k][2];
				String sb2= mpath.substring((mpath.indexOf("=")+1),(mpath.indexOf("line")-1));
				//the line to be modified
				String lin=mpath.substring((mpath.indexOf("line")+6),(mpath.indexOf(",")));
				   System.out.println("path**************"+mpath);
				System.out.println("**"+lin+"**");
				String st=sb2.replace('.','/'); 
	   		    String sb= "/"+st+".java";
	   		    File f1 =new File(cpp+sb);	
	   		    System.out.println(f1.getAbsolutePath());	   		   
	   		    File f2= new File(solLocation1+"/src/"+sb);
	   		    System.out.println(f2.getAbsolutePath());
	   		    String newString=tab[k][1].substring(tab[k][1].indexOf("--> `")+5, tab[k][1].indexOf("` ("));
	   		    System.out.println(newString);
	   		    String oldString=tab[k][1].substring(tab[k][1].indexOf(") `")+3, tab[k][1].indexOf("` -topatch")-1);
	   		    System.out.println(oldString);
	   		    //replace old String by new String not the entire line
	   		    String Op=tab[k][1].substring(tab[k][1].indexOf(":")+2,tab[k][1].indexOf(":("));
	   		    System.out.println("OP"+Op);
	   		  	String oldContent="";
	   		    BufferedReader reader = new BufferedReader(new FileReader(f1));
	   		    String line = reader.readLine();
	   		    int i=0;
	   		    while (line != null)
	   		    {  i++;
	   		    if(i==Integer.parseInt(lin)) 		    
	   		         oldContent = oldContent +line.replaceAll(oldString, newString)+ System.lineSeparator();	   		    
	   		     else	   		      oldContent = oldContent + line + System.lineSeparator();	
	   		      line = reader.readLine();
	   		    }
	   		    //write the new content
	             FileWriter writer = new FileWriter(f2);
	             writer.write(oldContent);
	             reader.close();
	             writer.close(); 
	   		    
				//try to repair this new program at the new location
	   			nb++;
			    //new argument
			     out = new File("round"+nb);
				if (out.exists()) 
					
				  out.delete();
				command.command.put("-location",new File(solLocation1).getAbsolutePath());
				command.command.put("-out",out.getAbsolutePath());
				try {
				main1.execute(command.flat());}
				catch(Exception e) {e.printStackTrace();}
				//check for update
				//get result from the previous iteration
				AC= ((RCFixCr)main1.getEngine()).AC;
				SMC= ((RCFixCr)main1.getEngine()).SMC;
				timeout= ((RCFixCr)main1.getEngine()).timeout;
				mxg= ((RCFixCr)main1.getEngine()).MAxGen;
				exh=((RCFixCr)main1.getEngine()).exhausted;
				fl=((RCFixCr)main1.getEngine()).failt;
				cps=((RCFixCr)main1.getEngine()).cpy;
				nbop=((RCFixCr)main1.getEngine()).operatorExecuted;
				g= Integer.parseInt(mxgn)-nbop;
				    if(AC || SMC)
				    {
				    	//find the modified File and update src and bin of the original project & save solution:
		            	
			           	 Stats s=main1.getEngine().getCurrentStat();
			           	 PatchStat patchstats = main1.getEngine().getPatchInfo().get(0);

			   			Stats stats = Stats.getCurrentStat();

			   			assertNotNull(stats);

			   			assertEquals(1, main1.getEngine().getPatchInfo().size());

			   			List<PatchHunkStats> hunksApi = (List<PatchHunkStats>) patchstats.getStats().get(PatchStatEnum.HUNKS);

			   			assertNotNull(hunksApi);

			   			PatchHunkStats hunkStats = hunksApi.get(0);
			   			String mdf = hunkStats.getStats().get(HunkStatEnum.MODIFIED_FILE_PATH).toString();
			   			String nc =mdf.substring(mdf.indexOf("java_programs")+14,mdf.length());
			   			System.out.println(nc);
			   		    FileUtils.copyFile(new File(mdf),new File(solLocation+"/src/java_programs/"+nc));
			   			solutions = main1.getEngine().getSolutions();

			   		
				    }
					
				     if(SMC) {  	       
			      			nb++;
			      			 out = new File(ConfigurationProperties.getProperty("workingDirectory")+nb);
			     			if (out.exists()) 
			     			out.delete();
			     			command.command.put("-location",new File(solLocation).getAbsolutePath());
							command.command.put("-out",out.getAbsolutePath());
							
				}
				if(AC|| SMC) break;
			}
		
		
		}
		
		}
		
		while(!AC && !timeout && ! mxg && ! exh);
		
			assertEquals(1, solutions.size());
	

		}



}

