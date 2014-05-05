package org.openerproject.targetproperties.sspace;

import java.io.BufferedReader;
import java.io.File;
//import java.io.FileReader;
import java.io.IOException;
//import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
//import org.apache.commons.io.IOUtils;
import org.openerproject.targetproperties.data.DatasetDocument;
import org.openerproject.targetproperties.data.DocumentReader;

import com.google.common.collect.Lists;

import edu.ucla.sspace.common.SemanticSpace;
import edu.ucla.sspace.common.SemanticSpaceIO;
import edu.ucla.sspace.text.IteratorFactory;

public abstract class AlgorithmExecutor {

	private static Logger log=Logger.getLogger(AlgorithmExecutor.class);
	
	private SemanticSpace algorithm;
	private DocumentReader documentReader;

	/*
	 * Si el input aquí son los documents sin preprocesar, entonces la mitad de
	 * las cosas del paquete data no parecen servir para nada... Pero es mejor
	 * así. Que el preprocesamiento lo haga un preprocessor que se inyecte en la
	 * clase que implementa esta interfaz
	 */

	// public void executeAlgorithm(List<String>documents);

	/**
	 * Performs the preprocessing of a document.
	 * The document is represented by a bufferedreader opened to the file.
	 * The document can be anything. The responsibility of dealing with the content is in the implementation of this method
	 * @param document
	 * @return
	 */
	public abstract BufferedReader preprocessDocument(DatasetDocument datasetDocument, String language);

	public synchronized void execute(List<File> files, String language, File outputfile) {
		
		log.info("Beginning the execution");
		// This performs OUR preprocess, consisting of sending the documents to
		// analyze (to KAF or whatever) and using that to return the document
		// content as we want them to be processed (lemma1 lema2 lemma3... etc.)
		List<BufferedReader> preprocessedDocuments = Lists.newArrayList();
		int fileCount=1;
		for (File file : files) {
			log.info("Preprocessing file "+(fileCount++)+" of "+files.size()+", "+file.getPath());
			try {
				//BufferedReader document = IOUtils.toBufferedReader(new FileReader(file));
				List<DatasetDocument> documents = documentReader.readDocument(file);
				int docCount=1;
				for(DatasetDocument document:documents){
					log.info("Preprocessing document "+(docCount++)+" of "+documents.size());
					BufferedReader processedDocument = preprocessDocument(document,language);
					//document.close();
					preprocessedDocuments.add(processedDocument);
					
					//REMOVE THIS
					if(docCount>10){
						break;
					}
					////////////////
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//This performs the SSPACE processing for each document
		log.info("Starting SSPACE processing");
		for (BufferedReader br : preprocessedDocuments) {
			try {
				algorithm.processDocument(br);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//This performs the SSPACE post-processing, and/or optimizations once the entire corpus has been seen 
		algorithm.processSpace(System.getProperties());
		try {
			//This prints the SSPACE to a file, to reuse later
			SemanticSpaceIO.save(algorithm, outputfile);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	public void setAlgorithmAndParams(SemanticSpace algorithm, List<String> multiwords) {
		try {
			// Set multiwords and stopwords via a temp file
			// (NOTE: this is not thread safe unless the lists are always the
			// same, because the iterator factory set properties is static)
			Properties props = new Properties();
			File multiwordsFile = File.createTempFile("sspace_multiwords", ".temp");
			FileUtils.writeLines(multiwordsFile, multiwords);
			props.setProperty(IteratorFactory.COMPOUND_TOKENS_FILE_PROPERTY, multiwordsFile.getAbsolutePath());
			
			//This cannot work with SSPACE in windows, because internally splits the paths using ":", breaking the path in windows ('C:')
			//See the code in: http://grepcode.com/file/repo1.maven.org/maven2/edu.ucla.sspace/sspace/2.0.3/edu/ucla/sspace/text/TokenFilter.java#TokenFilter.loadFromSpecification%28java.lang.String%2Cedu.ucla.sspace.util.ResourceFinder%29
			//Line 226
//			File stopwordsFile = File.createTempFile("sspace_stopwords", ".temp");
//			FileUtils.writeLines(stopwordsFile, stopwords);
//			props.setProperty(IteratorFactory.TOKEN_FILTER_PROPERTY, stopwordsFile.getAbsolutePath());
			
			IteratorFactory.setProperties(props);
			// Set the algorithm
			this.algorithm = algorithm;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public DocumentReader getDocumentReader() {
		return documentReader;
	}

	public void setDocumentReader(DocumentReader documentReader) {
		this.documentReader = documentReader;
	}
	
//	public abstract void setAlgorithm(SemanticSpace algorithm);
//	
//	public abstract SemanticSpace getAlgorithm();

}
