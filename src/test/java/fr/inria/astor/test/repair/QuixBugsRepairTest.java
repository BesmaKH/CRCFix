package fr.inria.astor.test.repair;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;
import java.util.Optional;

import org.junit.Ignore;
import org.junit.Test;

import fr.inria.astor.approaches.cardumen.CardumenApproach;
import fr.inria.astor.approaches.cardumen.CardumenExhaustiveApproach;
import fr.inria.astor.approaches.cardumen.ExpressionReplaceOperator;
import fr.inria.astor.approaches.cardumholes.Cardumen1HApproach;
import fr.inria.astor.approaches.jgenprog.extension.TibraApproach;
import fr.inria.astor.core.entities.Ingredient;
import fr.inria.astor.core.entities.ModificationPoint;
import fr.inria.astor.core.entities.OperatorInstance;
import fr.inria.astor.core.entities.SuspiciousModificationPoint;
import fr.inria.astor.core.faultlocalization.gzoltar.GZoltarFaultLocalization;
import fr.inria.astor.core.ingredientbased.IngredientBasedApproach;
import fr.inria.astor.core.setup.ConfigurationProperties;
import fr.inria.astor.core.solutionsearch.AstorCoreEngine;
import fr.inria.astor.core.solutionsearch.spaces.ingredients.IngredientPoolLocationType;
import fr.inria.astor.core.solutionsearch.spaces.ingredients.transformations.NoIngredientTransformation;
import fr.inria.astor.core.stats.PatchHunkStats;
import fr.inria.astor.core.stats.PatchStat;
import fr.inria.astor.core.stats.PatchStat.HunkStatEnum;
import fr.inria.astor.core.stats.PatchStat.PatchStatEnum;
import fr.inria.main.AstorOutputStatus;
import fr.inria.main.CommandSummary;
import fr.inria.main.ExecutionMode;
import fr.inria.main.evolution.AstorMain;
import fr.inria.main.evolution.ExtensionPoints;

/**
 * Testing all repaired bugs by Astor from Quixbugs from our experiment
 * 
 * @author Matias Martinez
 *
 */
public class QuixBugsRepairTest {

	public static CommandSummary getQuixBugsCommand(String name) {

		CommandSummary cs = new CommandSummary();
		String dep = new File("./examples/libs/junit-4.4.jar").getAbsolutePath();
		cs.command.put("-javacompliancelevel", "8");
		cs.command.put("-seed", "1");
		cs.command.put("-package", "java_programs");
		cs.command.put("-dependencies", dep);
		cs.command.put("-scope", "package");
		cs.command.put("-mode", "jgenprog");
		cs.command.put("-id", name);
		cs.command.put("-population", "1");
		cs.command.put("-srcjavafolder", "/src");
		cs.command.put("-srctestfolder", "/src");
		cs.command.put("-binjavafolder", "/bin");
		cs.command.put("-bintestfolder", "/bin");
		cs.command.put("-flthreshold", "0.0");
		cs.command.put("-loglevel", "DEBUG");
		cs.command.put("-stopfirst", "TRUE");
		cs.command.put("-parameters", "logtestexecution:TRUE:"
				+ "disablelog:TRUE:maxtime:120:autocompile:true:gzoltarpackagetonotinstrument:com.google.gson_engine"
				+ GZoltarFaultLocalization.PACKAGE_SEPARATOR + "java_programs_test");
		cs.command.put("-location", new File("./examples/quixbugs/" + name).getAbsolutePath());

		return cs;
	}

	/**
	 * Repaired in paper
	 * 
	 * @throws Exception
	 */
	@Ignore
	public void test_shortest_path_lengthsRepair_statement() throws Exception {
		AstorMain main1 = new AstorMain();

		CommandSummary command = (getQuixBugsCommand("shortest_path_lengths"));
		command.command.put("-maxgen", "500");
		main1.execute(command.flat());

		assertTrue("No solution", main1.getEngine().getSolutions().size() > 0);

	}

	/**
	 * Repaired in paper
	 * 
	 * @throws Exception
	 */
	@Ignore
	public void test_depth_first_searchRepair_statement() throws Exception {
		AstorMain main1 = new AstorMain();

		CommandSummary command = (getQuixBugsCommand("depth_first_search"));
		command.command.put("-maxgen", "500");
		main1.execute(command.flat());

		assertTrue("No solution", main1.getEngine().getSolutions().size() > 0);

	}

	/**
	 * Repaired in paper
	 * 
	 * @throws Exception
	 */
	

	/**
	 * Repaired in paper
	 * 
	 * @throws Exception
	 */
	@Ignore
	public void test_depth_first_searchRepair_jkali() throws Exception {
		AstorMain main1 = new AstorMain();

		CommandSummary command = (getQuixBugsCommand("depth_first_search"));
		command.command.put("-maxgen", "500");
		command.command.put("-mode", "jkali");
		main1.execute(command.flat());

		assertTrue("No solution", main1.getEngine().getSolutions().size() > 0);

	}

	/**
	 * Repaired in paper
	 * 
	 * @throws Exception
	 */
	@Ignore
	public void test_1() throws Exception {
		
		AstorMain main1 = new AstorMain();

		CommandSummary command = (getQuixBugsCommand("find_first_in_sorted1"));
		command.command.put("-mode", "cardumen");
		command.command.put("-maxgen", "500");
		main1.execute(command.flat());

		assertTrue("solution", main1.getEngine().getSolutions().size() > 0);

	}
@Ignore
	public void test_2() throws Exception {
		
		AstorMain main1 = new AstorMain();

		CommandSummary command = (getQuixBugsCommand("find_first_in_sorted2"));
		command.command.put("-mode", "cardumen");
		command.command.put("-maxgen", "500");
		main1.execute(command.flat());

		assertTrue("solution", main1.getEngine().getSolutions().size() > 0);

	}
@Ignore
	public void test_3() throws Exception {
		
		AstorMain main1 = new AstorMain();

		CommandSummary command = (getQuixBugsCommand("find_first_in_sorted3"));
		command.command.put("-mode", "cardumen");
		command.command.put("-maxgen", "500");
		main1.execute(command.flat());

		assertTrue("solution", main1.getEngine().getSolutions().size() > 0);

	}
@Ignore
	public void test_4() throws Exception {
		
		AstorMain main1 = new AstorMain();

		CommandSummary command = (getQuixBugsCommand("find_first_in_sorted4"));
		command.command.put("-mode", "cardumen");
		command.command.put("-maxgen", "500");
		main1.execute(command.flat());

		assertTrue("solution", main1.getEngine().getSolutions().size() > 0);

	}
@Ignore
	public void test_5() throws Exception {
		
		AstorMain main1 = new AstorMain();

		CommandSummary command = (getQuixBugsCommand("find_first_in_sorted5"));
		command.command.put("-mode", "cardumen");
		command.command.put("-maxgen", "700");
		main1.execute(command.flat());

		assertTrue("solution", main1.getEngine().getSolutions().size() > 0);

	}
@Test
public void test_6() throws Exception {
	
	AstorMain main1 = new AstorMain();

	CommandSummary command = (getQuixBugsCommand("find_first_in_sorted6"));
	command.command.put("-mode", "cardumen");
	command.command.put("-maxgen", "700");
	main1.execute(command.flat());

	assertTrue("solution", main1.getEngine().getSolutions().size() > 0);

}
	//RCFix junit Tests on find first in sorted program
}
