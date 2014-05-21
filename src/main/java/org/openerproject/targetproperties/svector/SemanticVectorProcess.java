package org.openerproject.targetproperties.svector;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openerproject.targetproperties.data.CorpusReader;
import org.openerproject.targetproperties.svector.documents.DocumentPreprocessor;
import org.openerproject.targetproperties.svector.indexing.LuceneIndexCreator;
import org.openerproject.targetproperties.svector.indexing.SemanticVectorIndexBuilder;
import org.openerproject.targetproperties.utils.CorpusHandlingUtils;

import com.google.common.collect.Lists;

public class SemanticVectorProcess {
	
	private Logger log=Logger.getLogger(SemanticVectorProcess.class);
	
	public static final String PREPROCESSED_FILES_SUBFOLDER="preprocessed_files";
	public static final String LUCENE_INDEX_SUBFOLER="lucene_index";
	public static final String SEMANTIC_VECTORS_OUTPUT_SUBFOLDER="semantic_vectors";
	
	private CorpusReader corpusReader;
	private DocumentPreprocessor documentPreprocessor;
	private LuceneIndexCreator luceneIndexCreator;
	private SemanticVectorIndexBuilder semanticVectorIndexBuilder;
	
	
	public void execute(String corpusDir,String multiwordsFile,int numDimensions,int numCycles,String outputFolder){
		List<String>multiwords=Lists.newArrayList();
		try {
			multiwords = FileUtils.readLines(new File(multiwordsFile), "UTF-8");
			documentPreprocessor.loadMultiwords(multiwords);
		} catch (IOException e) {
			log.warn("Error reading multiword file: " + e.getMessage());
		}
		execute(corpusDir, numDimensions, numCycles, outputFolder);
	}
	
	/*
	 * ATTENTION: The semantic vectors index creation does not allow (from the main method) to set an output folder. 
	 * Thus, the indexes will be created in the current folder, and there is no need of the "outputRootFolder" parameter since the intermediate stuff can be created also relative to the current folder
	 */
	
	public void execute(String corpusDir, int numDimensions, int numCycles, String outputRootFolder){		
		//Read the docs from the folder passed in a parameter
		log.info("Loading files from corpus folder...");
		List<File> corpusFiles = CorpusHandlingUtils.getFilesFromDir(corpusDir);
		log.info("Loaded "+corpusFiles.size()+" files");
		log.info("Starting files preprocessing...");
		String preprocessOutputFolder=outputRootFolder+File.separator+PREPROCESSED_FILES_SUBFOLDER;
		
		int fileCount=1;
		int totalFileNumber=corpusFiles.size();
		for(File corpusFile:corpusFiles){
			log.info("Reading content from file "+fileCount+" of "+totalFileNumber);
			fileCount++;
			///////////////
//			if(fileCount>10){
//				break;
//			}
			//////////////
			try{
			String fileContent=corpusReader.readCorpusFileContent(corpusFile);
			//Preprocess the content (if they are KAF or RAW text, stated in a parameter)
			log.info("Preprocessing content...");
			List<String> textsResultingFromPreprocess = documentPreprocessor.preprocessDocument(fileContent);
			log.info("After the preprocessing the content has been split in "+textsResultingFromPreprocess.size()+" documents. Writing to their respective files...");
			//Store the "preprocessed" output (input from the next part) in a predefined subfolder, relative to the stated output root folder
			CorpusHandlingUtils.writeTextsToFiles(textsResultingFromPreprocess, preprocessOutputFolder);
			}catch(Exception e){
				log.warn("Error processing the "+fileCount+"th document\n"+e);
			}
		}
				
		//build lucene index from the preprocessed corpus, storing it in a fixed subfloder relative to the output root
		String pathToLuceneIndex=outputRootFolder+File.separator+LUCENE_INDEX_SUBFOLER;
		log.info("Creating Lucene index from the preprocessed documents in "+pathToLuceneIndex+" ...");
		luceneIndexCreator.createLuceneIndex(preprocessOutputFolder, pathToLuceneIndex);
				
		//build semantic vector index from the generated lucene index, inside a fixed subfolder relative to the output root
		//String pathToSemanticVectorsIndex=outputRootFolder+File.separator+SEMANTIC_VECTORS_OUTPUT_SUBFOLDER;
		log.info("Creating SemanticVectors index...");
		semanticVectorIndexBuilder.buildIndex(pathToLuceneIndex, outputRootFolder, numDimensions,numCycles);
		log.info("Process done");
	}

	public void setCorpusReader(CorpusReader corpusReader) {
		this.corpusReader = corpusReader;
	}

	public void setDocumentPreprocessor(DocumentPreprocessor documentPreprocessor) {
		this.documentPreprocessor = documentPreprocessor;
	}

	public void setLuceneIndexCreator(LuceneIndexCreator luceneIndexCreator) {
		this.luceneIndexCreator = luceneIndexCreator;
	}

	public void setSemanticVectorIndexBuilder(SemanticVectorIndexBuilder semanticVectorIndexBuilder) {
		this.semanticVectorIndexBuilder = semanticVectorIndexBuilder;
	}
	
}
