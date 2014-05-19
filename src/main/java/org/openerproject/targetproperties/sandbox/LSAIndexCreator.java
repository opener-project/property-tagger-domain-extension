package org.openerproject.targetproperties.sandbox;

import java.io.IOException;

import pitt.search.semanticvectors.LSA;

public class LSAIndexCreator {

	public static final String LUCENE_INDEX_PATH="MAIN_OUTPUT/lucene_index";
	
	public static void main(String[] args) throws IllegalArgumentException, IOException {

		execute(LUCENE_INDEX_PATH);
//		IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(LUCENE_INDEX_PATH)));
//		Terms tv = reader.getTermVector(12, "contents");
//		System.out.println(tv.size());
	}
	
	public static void execute(String pathToLuceneIndex) throws IllegalArgumentException, IOException{
		String[]args={"-luceneindexpath",pathToLuceneIndex,"-dimension","500","-minfrequency","10"};
		LSA.main(args);
		
	}

}
