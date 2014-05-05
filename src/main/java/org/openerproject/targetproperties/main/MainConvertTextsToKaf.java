package org.openerproject.targetproperties.main;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openerproject.targetproperties.data.bis.PlainTextCorpusReader;
import org.openerproject.targetproperties.kaf.AnalysisTypes;
import org.openerproject.targetproperties.kaf.CustomWebServicesKafAnalyzer;
import org.openerproject.targetproperties.utils.CorpusHandlingUtils;

public class MainConvertTextsToKaf {

	public static final String CORPUS_DIR="C:\\Users\\yo\\workspace\\kaf-annotations-cleaner\\REVIEW_CORPUS_BIG";
	public static final String LANG="en";
	public static final String OUTPUT_DIR="EN_REVIEWS_KAF_BIG";
	
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
			String kaf=customWebServicesKafAnalyzer.analyzeText(content,LANG);
			File outfile=new File(OUTPUT_DIR+File.separator+corpusFile.getName()+".kaf");
			FileUtils.write(outfile, kaf,"UTF-8");
		}
		
	}

}
