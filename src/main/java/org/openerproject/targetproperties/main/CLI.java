package org.openerproject.targetproperties.main;

import static org.openerproject.targetproperties.main.GlobalVariables.CATEGORY_DEFINITIONS_PATH_OPT;
import static org.openerproject.targetproperties.main.GlobalVariables.CLASSIFY_TARGETS_OPT;
import static org.openerproject.targetproperties.main.GlobalVariables.EVALUATE_CLASSIFIED_TARGETS_OPT;
import static org.openerproject.targetproperties.main.GlobalVariables.HTML_EVAL_OUTPUT_PATH_OPT;
import static org.openerproject.targetproperties.main.GlobalVariables.LUCENE_INDEX_FOLDER;
import static org.openerproject.targetproperties.main.GlobalVariables.LUCENE_INDEX_PATH_OPT;
import static org.openerproject.targetproperties.main.GlobalVariables.OUTPUT_RESULT_PATH_OPT;
import static org.openerproject.targetproperties.main.GlobalVariables.SEMANTIC_VECTOR_OPT;
import static org.openerproject.targetproperties.main.GlobalVariables.TARGETS_TO_CLASSIFY_PATH_OPT;
import static org.openerproject.targetproperties.main.GlobalVariables.TERM_VECTORS_PATH_OPT;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class CLI {

	private static Logger log=Logger.getLogger(CLI.class);
	
	// private static Options options;
	private static ApplicationContext appContext = new ClassPathXmlApplicationContext(
			"spring-config.xml");
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (System.console() == null) {
			String LANGUAGE_SHORT = "es";
//			String input = "C:\\Users\\agarciap\\Data\\REVIEWS_DATA\\"
//					+ LANGUAGE_SHORT.toUpperCase() + "_REVIEWS_KAF";
			String output = "C:\\Users\\agarciap\\Data\\STORING_RESULTS\\MORE_RESULTS\\"+LANGUAGE_SHORT.toUpperCase();
			
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
	
	public static void execute(String[]args){
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
