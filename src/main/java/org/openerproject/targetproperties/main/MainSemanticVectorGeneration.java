package org.openerproject.targetproperties.main;

import static org.openerproject.targetproperties.main.GlobalVariables.*;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.log4j.Logger;
import org.openerproject.targetproperties.svector.SemanticVectorProcess;
import org.springframework.context.ApplicationContext;

import cern.colt.Arrays;

public class MainSemanticVectorGeneration {

	private static Logger log=Logger.getLogger(MainSemanticVectorGeneration.class);
	
	private Options options;
	private ApplicationContext appContext;
	
	private String inputPath;
	private String outputPath;
	private String multiwordFile;
	private int numCycles;
	private int numDimensions;
	
	@SuppressWarnings("static-access")
	private Options buildOptions(){
		options=new Options();
		Option inputDir=OptionBuilder.withDescription("Input directory with KAF documents to process").hasArg(true).isRequired(true).create(GlobalVariables.INPUT_DIR_OPT);
		Option outputDir=OptionBuilder.withDescription("Output directory to store the results").hasArg(true).isRequired(false).create(OUTPUT_DIR_OPT);
		Option multiwordFile=OptionBuilder.withDescription("File with multiword terms, used during the processing").hasArg(true).isRequired(false).create(MULTIWORD_FILE_OPT);
		Option numDimensions=OptionBuilder.withDescription("Number of dimension of the vectors generated by SemanticVectors process").hasArg(true).isRequired(false).create(NUM_DIM_OPT);
		Option numCycles=OptionBuilder.withDescription("Number of cycles used in the SemanticVectors process (see documentation)").hasArg(true).isRequired(false).create(NUM_CYCLES_OPT);
		
		options.addOption(inputDir);
		options.addOption(outputDir);
		options.addOption(multiwordFile);
		options.addOption(numDimensions);
		options.addOption(numCycles);
		return options;
	}
	
	public MainSemanticVectorGeneration(ApplicationContext appContext){
		options=buildOptions();
		this.appContext=appContext;
	}
	
	public void processParametersAndExecute(String[]args){
		CommandLineParser parser = new BasicParser();
		try {
			log.debug("Parsing: "+Arrays.toString(args));
	        // parse the command line arguments
	        CommandLine line = parser.parse( options, args );
	        inputPath=line.getOptionValue(INPUT_DIR_OPT);
	        if(inputPath==null){
	        	throw new RuntimeException("INPUT IS NULL!, parse opt: "+INPUT_DIR_OPT);
	        }
	        outputPath=returnUserValueOrDefault(line, OUTPUT_DIR_OPT, OUTPUT_DIR_DEFAULT);
	        multiwordFile=line.getOptionValue(MULTIWORD_FILE_OPT);
	        numCycles=returnUserValueOrDefault(line, NUM_CYCLES_OPT, NUM_CYCLES_DEFAULT);
	        numDimensions=returnUserValueOrDefault(line, NUM_DIM_OPT, NUM_DIMENSIONS_DEFAULT);
	        SemanticVectorProcess semanticVectorProcess=(SemanticVectorProcess) appContext.getBean("SemanticVectorProcess");
	        //IMPORTANT: Language and isKAF parameter no longer used, defaulted to "en" and true, to avoid changing function signature for now
	        //NORE: Because of this, the input files must be already in KAF. The analysis of plain text is no longer resposibility of this module
	        if(multiwordFile!=null){
	        	semanticVectorProcess.execute(inputPath, multiwordFile, numDimensions, numCycles, outputPath);
	        }else{
	        	semanticVectorProcess.execute(inputPath, numDimensions, numCycles, outputPath);
	        }
	        
	        
		}catch(Exception e){
			log.error(e);
			HelpFormatter formatter = new HelpFormatter();
        	formatter.printHelp( "java -jar [NAME_OF_THE_JAR] "+SEMANTIC_VECTOR_OPT+" [OPTIONS]", options );
		}
	}
	
	protected Integer returnUserValueOrDefault(CommandLine line,String param,Integer defaultValue){
		if(line.hasOption(param)){
			return Integer.parseInt(line.getOptionValue(param));
		}else{
			return defaultValue;
		}
	}
	
	protected String returnUserValueOrDefault(CommandLine line,String param,String defaultValue){
		if(line.hasOption(param)){
			return line.getOptionValue(param);
		}else{
			return defaultValue;
		}
	}
	
	
}
