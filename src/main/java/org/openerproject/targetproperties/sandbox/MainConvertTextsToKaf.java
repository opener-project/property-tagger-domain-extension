package org.openerproject.targetproperties.sandbox;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openerproject.targetproperties.data.PlainTextCorpusReader;
import org.openerproject.targetproperties.kaf.AnalysisTypes;
import org.openerproject.targetproperties.kaf.CustomWebServicesKafAnalyzer;
import org.openerproject.targetproperties.utils.CorpusHandlingUtils;

public class MainConvertTextsToKaf {

	private static final String LANGUAGE_SHORT = "de";
	public static final String CORPUS_DIR="D:\\stuff_from_the_laptop_itself\\REVIEWS_DATA\\"+LANGUAGE_SHORT.toUpperCase()+"_REVIEWS_TEXT";
	//public static final String LANG="en";
	public static final String OUTPUT_DIR="D:\\stuff_from_the_laptop_itself\\REVIEWS_DATA\\"+LANGUAGE_SHORT.toUpperCase()+"_REVIEWS_KAF";
	
	public static void main(String[] args) throws IOException {
		CustomWebServicesKafAnalyzer customWebServicesKafAnalyzer=new CustomWebServicesKafAnalyzer();
		customWebServicesKafAnalyzer.setAnalysisTypes(new AnalysisTypes[]{AnalysisTypes.TOKEN,AnalysisTypes.POSTAG});
		customWebServicesKafAnalyzer.setEndpointURL("http://192.168.241.131:9999/ws/opener?wsdl");
		
		PlainTextCorpusReader plainTextCorpusReader=new PlainTextCorpusReader();
		List<File> files = CorpusHandlingUtils.getFilesFromDir(CORPUS_DIR);
		int fileCount=1;
		int numFiles=files.size();
		for(File corpusFile:files){
			System.out.println("Processing file "+fileCount+" of "+numFiles+"...");
			fileCount++;
			String content=plainTextCorpusReader.readCorpusFileContent(corpusFile);
			String kaf=customWebServicesKafAnalyzer.analyzeText(content,LANGUAGE_SHORT);
			File outfile=new File(OUTPUT_DIR+File.separator+corpusFile.getName()+".kaf");
			FileUtils.write(outfile, kaf,"UTF-8");
		}
		
	}

}
