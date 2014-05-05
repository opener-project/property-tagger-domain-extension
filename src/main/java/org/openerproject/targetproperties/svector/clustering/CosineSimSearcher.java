package org.openerproject.targetproperties.svector.clustering;

import java.io.IOException;

import pitt.search.semanticvectors.CloseableVectorStore;
import pitt.search.semanticvectors.CompoundVectorBuilder;
import pitt.search.semanticvectors.FlagConfig;
import pitt.search.semanticvectors.LuceneUtils;
//import pitt.search.semanticvectors.Search;
import pitt.search.semanticvectors.VectorStoreReader;
import pitt.search.semanticvectors.VectorSearcher.VectorSearcherCosine;
import pitt.search.semanticvectors.vectors.Vector;
import pitt.search.semanticvectors.vectors.ZeroVectorException;

public class CosineSimSearcher {
	
	private CloseableVectorStore closeableVectorStore;
	private LuceneUtils luceneUtils;
	private FlagConfig flagConfig;
	
	public CosineSimSearcher(String luceneIndexPath, String pathToTermVectorsFile){
		flagConfig = FlagConfig.getFlagConfig(new String[]{"-luceneindexpath",luceneIndexPath});
		try {
			luceneUtils = new LuceneUtils(flagConfig);
			closeableVectorStore= VectorStoreReader.openVectorStore(pathToTermVectorsFile, flagConfig);
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	public double getCosineDistance(String term1,String term2) throws IOException, ZeroVectorException{
		
		//-searchvectorfile search_vector_file
		//String[]args={"-searchvectorfile",""};
		
//		FlagConfig flagConfig = FlagConfig.getFlagConfig(new String[]{"-luceneindexpath",luceneIndexPath});
//		LuceneUtils luceneUtils = new LuceneUtils(flagConfig);
//		CloseableVectorStore vecReader = VectorStoreReader.openVectorStore(pathToTermVectorsFile, flagConfig);
		 Vector vec1 = CompoundVectorBuilder.getQueryVectorFromString(
			        closeableVectorStore, luceneUtils, flagConfig, term1);
		Vector vec2 = CompoundVectorBuilder.getQueryVectorFromString(
			        closeableVectorStore, luceneUtils, flagConfig, term2);
		
		VectorSearcherCosine vcosine=new VectorSearcherCosine(closeableVectorStore, closeableVectorStore, luceneUtils, flagConfig, vec1);
		
		double score = vcosine.getScore(vec2);
		return score;
		
	}
	
}
