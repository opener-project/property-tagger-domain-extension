package org.openerproject.targetproperties.main;

import static org.openerproject.targetproperties.main.GlobalVariables.*;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.log4j.Logger;
import org.openerproject.targetproperties.classification.ClassificationResultPrinter;
import org.openerproject.targetproperties.classification.TargetClassifier;
import org.openerproject.targetproperties.classification.TargetToClassify;

import cern.colt.Arrays;

public class MainOpinionTargetClassifier {

	private static Logger log=Logger.getLogger(MainOpinionTargetClassifier.class);
	
	private Options options;
	
	private String categoryDefinitionsFilePath = null;
	private String targetsToClassifyFilePath = null;
	private String luceneIndexPath = null;
	private String termVectorsFilePath = null;
	private boolean evaluateClassifiedTargets=false;
	private String outputResultFile=null;
	private String outputHtmlEvalFile=null;

	@SuppressWarnings("static-access")
	private Options buildOptions(){
		options=new Options();
		Option luceneIndexPath=OptionBuilder.withDescription("Path to Lucene index").hasArg(true).isRequired(true).create(GlobalVariables.LUCENE_INDEX_PATH_OPT);
		Option targetsToClassifyFilePath=OptionBuilder.withDescription("Path to file with the targets you want to classify").hasArg(true).isRequired(true).create(TARGETS_TO_CLASSIFY_PATH_OPT);
		Option categoryDefinitionsFilePath=OptionBuilder.withDescription("Path to file with the category definitions (i.e with example targets)").hasArg(true).isRequired(true).create(CATEGORY_DEFINITIONS_PATH_OPT);
		Option termVectorsFilePath=OptionBuilder.withDescription("Path to SemanticVectors term vectors file").hasArg(true).isRequired(true).create(TERM_VECTORS_PATH_OPT);
		Option evaluateClassifiedTargets=OptionBuilder.withDescription("Evaluate the results (comparison table and confusion matrix)").hasArg(false).isRequired(false).create(EVALUATE_CLASSIFIED_TARGETS_OPT);
		Option outputResultFile=OptionBuilder.withDescription("Output file with the opinion-target <-> propterty pairs").hasArg(true).isRequired(true).create(OUTPUT_RESULT_PATH_OPT);
		Option outputHtmlEvalFile=OptionBuilder.withDescription("Path to file html file that will contain the evaluation results").hasArg(true).isRequired(false).create(HTML_EVAL_OUTPUT_PATH_OPT);
		
		options.addOption(luceneIndexPath);
		options.addOption(targetsToClassifyFilePath);
		options.addOption(categoryDefinitionsFilePath);
		options.addOption(termVectorsFilePath);
		options.addOption(evaluateClassifiedTargets);
		options.addOption(outputResultFile);
		options.addOption(outputHtmlEvalFile);
		return options;
	}
	
	public MainOpinionTargetClassifier(){
		options=buildOptions();
	}
	
	public void processParametersAndExecute(String[] args) {
		CommandLineParser parser = new BasicParser();
		try {
			log.debug("Parsing: "+Arrays.toString(args));
	        // parse the command line arguments
	        CommandLine line = parser.parse( options, args );
	        categoryDefinitionsFilePath=line.getOptionValue(CATEGORY_DEFINITIONS_PATH_OPT);
	        targetsToClassifyFilePath=line.getOptionValue(TARGETS_TO_CLASSIFY_PATH_OPT);
	        luceneIndexPath=line.getOptionValue(LUCENE_INDEX_PATH_OPT);
	        termVectorsFilePath=line.getOptionValue(TERM_VECTORS_PATH_OPT);
	        evaluateClassifiedTargets=line.hasOption(EVALUATE_CLASSIFIED_TARGETS_OPT);
	        outputResultFile=line.getOptionValue(OUTPUT_RESULT_PATH_OPT);
	        outputHtmlEvalFile=line.getOptionValue(HTML_EVAL_OUTPUT_PATH_OPT);
	        
			TargetClassifier targetClassifier = new TargetClassifier();
			targetClassifier
					.setCategoryDefinitionsFilePath(categoryDefinitionsFilePath);
			targetClassifier
					.setTargetsToClassifyFilePath(targetsToClassifyFilePath);
			targetClassifier.setLuceneIndexPath(luceneIndexPath);
			targetClassifier.setTermVectorsFilePath(termVectorsFilePath);
			
			List<TargetToClassify> classifiedTargets = targetClassifier.classifyTargets();
			ClassificationResultPrinter classificationResultPrinter=new ClassificationResultPrinter();
			classificationResultPrinter.printClassifiedTargets(classifiedTargets, new FileOutputStream(new File(outputResultFile)));
	        if(evaluateClassifiedTargets){
	        	classificationResultPrinter.printHTMLEvaluation(classifiedTargets, new FileOutputStream(new File(outputHtmlEvalFile)));
	        }
		}catch(Exception e){
			log.error(e);
			HelpFormatter formatter = new HelpFormatter();
        	formatter.printHelp( "java -jar [NAME_OF_THE_JAR] "+CLASSIFY_TARGETS_OPT+" [OPTIONS]", options );
		}

	}
	
//	protected String returnUserValueOrDefault(CommandLine line,String param,String defaultValue){
//		if(line.hasOption(param)){
//			return line.getOptionValue(param);
//		}else{
//			return defaultValue;
//		}
//	}

}
