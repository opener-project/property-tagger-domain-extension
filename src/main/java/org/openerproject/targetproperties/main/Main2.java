package org.openerproject.targetproperties.main;

import static org.openerproject.targetproperties.main.GlobalVariables.*;

import java.util.Arrays;

import org.apache.log4j.Logger;
//import org.apache.commons.cli.BasicParser;
//import org.apache.commons.cli.CommandLine;
//import org.apache.commons.cli.CommandLineParser;
//import org.apache.commons.cli.Option;
//import org.apache.commons.cli.OptionBuilder;
//import org.apache.commons.cli.OptionGroup;
//import org.apache.commons.cli.Options;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main2 {

	private static Logger log=Logger.getLogger(Main2.class);
	
	// private static Options options;
	private static ApplicationContext appContext = new ClassPathXmlApplicationContext(
			"spring-config.xml");

	// @SuppressWarnings("static-access")
	// private static Options buildOptions(){
	// Options options=new Options();
	// OptionGroup optionGroup=new OptionGroup();
	// Option
	// semvecs=OptionBuilder.withDescription("Create semantic vector space").create(SEMANTIC_VECTOR_OPT);
	// Option
	// classify=OptionBuilder.withDescription("Classify input targets into categories").create(CLASSIFY_TARGETS_OPT);
	// optionGroup.addOption(semvecs);
	// optionGroup.addOption(classify);
	// optionGroup.setRequired(true);
	// options.addOptionGroup(optionGroup);
	// return options;
	// }

	public static void main(String[] args) {
		if (System.console() == null) {
			String LANGUAGE_SHORT = "es";
			String input = "C:\\Users\\agarciap\\Data\\REVIEWS_DATA\\"
					+ LANGUAGE_SHORT.toUpperCase() + "_REVIEWS_KAF";
			String output = "C:\\Users\\agarciap\\Data\\STORING_RESULTS\\MORE_RESULTS\\"+LANGUAGE_SHORT.toUpperCase();
			String multiwordFile = "";

			args = new String[] { SEMANTIC_VECTOR_OPT, "-" + INPUT_DIR_OPT,
					input, "-" + OUTPUT_DIR_OPT, output, "-" + NUM_CYCLES_OPT,
					"5" };

			// ///////////////////////////////
			String luceneIndexPath = output+"/"+LUCENE_INDEX_FOLDER;
			String termVectorsPath = output+"/termvectors5.bin";
			String targetsToClassify = "GOLD_TARGET_PROPERTIES/"+LANGUAGE_SHORT+"_aspects_to_classify.txt";
			String categoryDefinitions = "GOLD_TARGET_PROPERTIES/"+LANGUAGE_SHORT+"_category_definitions.txt";
			String outputResultPath=output+"/"+LANGUAGE_SHORT+"_classified_targets.txt";
			String htmlEvalOutputPath=output+"/"+LANGUAGE_SHORT+"_evaluation_info.html";
			args = new String[] { CLASSIFY_TARGETS_OPT,
					"-" + LUCENE_INDEX_PATH_OPT, luceneIndexPath,
					"-" + TERM_VECTORS_PATH_OPT, termVectorsPath,
					"-" + TARGETS_TO_CLASSIFY_PATH_OPT, targetsToClassify,
					"-" + CATEGORY_DEFINITIONS_PATH_OPT, categoryDefinitions,
					"-" + OUTPUT_RESULT_PATH_OPT, outputResultPath,
					"-" + HTML_EVAL_OUTPUT_PATH_OPT, htmlEvalOutputPath,
					"-" + EVALUATE_CLASSIFIED_TARGETS_OPT};

		}
		execute(args);
	}

	public static void execute(String[] args) {
		if (args.length == 0) {
			throw new RuntimeException("Wrong parameters");
		} else {
			String operation = args[0];
			String[] rest = Arrays.copyOfRange(args, 1, args.length);
			if (operation.equalsIgnoreCase(SEMANTIC_VECTOR_OPT)) {
				MainSemanticVectorGeneration mainSemanticVectorGeneration = new MainSemanticVectorGeneration(
						appContext);
				mainSemanticVectorGeneration.processParametersAndExecute(rest);
			} else if (operation.equalsIgnoreCase(CLASSIFY_TARGETS_OPT)) {
				MainOpinionTargetClassifier mainOpinionTargetClassifier = new MainOpinionTargetClassifier();
				mainOpinionTargetClassifier.processParametersAndExecute(rest);
			} else {
				log.warn("Wrong main parameter: " + operation+"\nValid params: "+SEMANTIC_VECTOR_OPT+" | "+CLASSIFY_TARGETS_OPT);
			}
		}

	}

}
