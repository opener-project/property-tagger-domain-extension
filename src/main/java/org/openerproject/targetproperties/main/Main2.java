package org.openerproject.targetproperties.main;

import static org.openerproject.targetproperties.main.GlobalVariables.*;

import java.util.Arrays;

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

//	private static Options options;
	private static ApplicationContext appContext=new ClassPathXmlApplicationContext("spring-config.xml");
	
//	@SuppressWarnings("static-access")
//	private static Options buildOptions(){
//		Options options=new Options();
//		OptionGroup optionGroup=new OptionGroup();
//		Option semvecs=OptionBuilder.withDescription("Create semantic vector space").create(SEMANTIC_VECTOR_OPT);
//		Option classify=OptionBuilder.withDescription("Classify input targets into categories").create(CLASSIFY_TARGETS_OPT);
//		optionGroup.addOption(semvecs);
//		optionGroup.addOption(classify);
//		optionGroup.setRequired(true);
//		options.addOptionGroup(optionGroup);
//		return options;
//	}
	
	public static void main(String[]args){
		if(System.console()==null){
			String LANGUAGE_SHORT="en";
			String input="D:\\stuff_from_the_laptop_itself\\REVIEWS_DATA\\"+LANGUAGE_SHORT.toUpperCase()+"_REVIEWS_KAF";
			String output="D:\\stuff_from_the_laptop_itself\\STORING_RESULTS\\MORE_RESULTS";
			String multiwordFile="";
			
			args=new String[]{SEMANTIC_VECTOR_OPT,"-"+INPUT_DIR_OPT, input,"-"+OUTPUT_DIR_OPT,output,"-"+NUM_CYCLES_OPT,"5"};
		}
		execute(args);
	}
	
	public static void execute(String[]args){
		if(args.length==0){
			throw new RuntimeException("Wrong parameters");
		}else{
			String operation=args[0];
			String[] rest = Arrays.copyOfRange(args, 1, args.length);
			if(operation.equalsIgnoreCase(SEMANTIC_VECTOR_OPT)){
				MainSemanticVectorGeneration mainSemanticVectorGeneration=new MainSemanticVectorGeneration(appContext);
				mainSemanticVectorGeneration.processParametersAndExecute(rest);
			}else if(operation.equalsIgnoreCase(CLASSIFY_TARGETS_OPT)){
				MainOpinionTargetClassifier mainOpinionTargetClassifier=new MainOpinionTargetClassifier();
				mainOpinionTargetClassifier.processParametersAndExecute(rest);
			}else{
				throw new RuntimeException("Wrong main parameter: "+operation);
			}
		}
		
	}
	
}
