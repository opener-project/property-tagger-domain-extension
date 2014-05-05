package org.openerproject.targetproperties.svector.clustering;

import java.io.IOException;

import pitt.search.semanticvectors.CompareTerms;
import pitt.search.semanticvectors.Search;

public class SemanticVectorSearch {

	public static void main(String[] args) throws IllegalArgumentException, IOException {
		// TODO Auto-generated method stub
		
		System.out.println(Search.usageMessage);
		args=new String[]{"staff"};
		Search.main(args);
		
		args=new String[]{"-queryvectorfile", "termvectors.bin","room","staff"};
		CompareTerms.main(args);
		
	}

}
