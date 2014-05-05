package org.openerproject.targetproperties.main;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.log4j.Logger;
import org.openerproject.targetproperties.svector.SemanticVectorProcess;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.google.common.collect.Lists;

public class Main {

	private static Logger log=Logger.getLogger(Main.class);
	
	private static ApplicationContext appContext;
	
	private static Options options;
	private static Set<String>validLangs=new HashSet<String>(Lists.newArrayList("en","es","fr","it","nl","de"));

	static{
		options = new Options();
		options.addOption("h", "help",false,"Prints this message");
		options.addOption("semvecs","semantic-vectors",false,"Preprocess corpus and generate semantic vectors");
		options.addOption("kaf", "already-in-kaf", false, "Flag to state if the input corpus is already in kaf or not (i.e. plain text)");
		options.addOption("d","corpus-dir",true, "Path to the directory containing the corpus files");
		options.addOption("lang", "language", true, "Language of the corpus files content, to use when preprocessing them");
		options.addOption("out", "output-folder", true, "Folder in which all the intermediate and final results will be stored");
	}
	
	public static void main(String[]args){
		if(System.console()==null){
			//if we are not launching from the console (e.g. launching from Eclipse)
			//then we can simulate the arguments
			args=new String[]{"-semvecs","-d","EN_REVIEWS_KAF_BIG","-lang","en","-out","MAIN_OUTPUT","-kaf"};
		}
		execute(args);
	}
	
	public static void execute(String[]args){
		CommandLineParser parser = new BasicParser();
		try {
	        // parse the command line arguments
	        CommandLine line = parser.parse( options, args );
	        if(line.hasOption("semvecs")){
	        	boolean isKaf=line.hasOption("kaf");
	        	String pathToCorpusDir=line.getOptionValue("d");
	        	String outputPath=line.getOptionValue("out");
	        	if(pathToCorpusDir==null || pathToCorpusDir.length()==0){
	        		throw new RuntimeException("Path to folder with the corpus is missing!");
	        	}
	        	if(outputPath==null || outputPath.length()==0){
	        		log.info("No output path defined, defaulting to current directory");
	        		outputPath=".";//throw new RuntimeException("Path to folder with the corpus is missing!");
	        	}
	        	String lang=line.getOptionValue("lang");
	        	if(!validLangs.contains(lang)){
	        		throw new RuntimeException("Invalid language: "+lang+"\nAllowed languages: "+validLangs.toString());
	        	}
	        	SemanticVectorProcess semanticVectorProcess=(SemanticVectorProcess) getBeanFromContainer("SemanticVectorProcess");
	        	log.info("Launching semanticVectorProcess with params: corpus-dir="+pathToCorpusDir+" ; lang="+lang+" ; alreadyInKaf="+isKaf+" ; output-folder"+outputPath);
	        	semanticVectorProcess.execute(pathToCorpusDir, lang, isKaf, outputPath);
	        	
	        }else{
	        	HelpFormatter formatter = new HelpFormatter();
	        	formatter.printHelp( "java -jar [NAME_OF_THE_JAR] [OPTION]", options );
	        }
	        
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	protected static Object getBeanFromContainer(String beanName){
		if(appContext==null){
			appContext=new ClassPathXmlApplicationContext("spring-config.xml");
		}
		return appContext.getBean(beanName);
	}
	
}
