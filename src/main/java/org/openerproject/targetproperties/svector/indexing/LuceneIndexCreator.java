package org.openerproject.targetproperties.svector.indexing;

//import java.io.File;

//import org.apache.lucene.demo.IndexFiles;

public class LuceneIndexCreator {

	public void createLuceneIndex(String pathToCorpusFiles, String pathToOutputIndex){
		
//		File corpusDir=new File(pathToCorpusFiles);
//		File indexFile=new File(pathToOutputIndex);
//		String[]args=new String[]{"-docs",corpusDir.getAbsolutePath(),"-index", indexFile.getAbsolutePath()};
//		IndexFiles.main(args);
		
		
		//String[]args=new String[]{"-docs",pathToCorpusFiles,"-index", pathToOutputIndex};
		CustomLuceneIndexer.main(pathToOutputIndex, pathToCorpusFiles, true);
	}
	
}
