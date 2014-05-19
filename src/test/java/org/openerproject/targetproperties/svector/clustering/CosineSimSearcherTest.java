package org.openerproject.targetproperties.svector.clustering;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import pitt.search.semanticvectors.vectors.ZeroVectorException;

public class CosineSimSearcherTest {

	public static final String LUCENE_INDEX_PATH = "MAIN_OUTPUT/lucene_index";
	public static final String TERM_VECTORS_FILE_PATH = "MAIN_OUTPUT/termvectors3.bin";
	
	private CosineSimSearcher cosineSimSearcher;
	
	@Before
	public void setUp() throws Exception {
		cosineSimSearcher=new CosineSimSearcher(LUCENE_INDEX_PATH, TERM_VECTORS_FILE_PATH);
	}

	@Test
	public void testGetCosineDistance() throws IOException, ZeroVectorException {
		double score = cosineSimSearcher.getCosineDistance("bed", "bed room dfdf FDSFSDF RERER asdasda car bus");
		if(Double.isNaN(score)){
			System.out.println("Te pillé NAN");
		}else{
			System.out.println("Score: "+score);
		}
		assertTrue(true);
		
	}

	@Test
	public void testIsInDictionary(){
		boolean isBed=cosineSimSearcher.isInDictionary("bed");
		System.out.println("Is bed: "+isBed);
		boolean isASDDSA=cosineSimSearcher.isInDictionary("ASDDSA");
		System.out.println("Is ASDDSA: "+isASDDSA);
	}
	
}
