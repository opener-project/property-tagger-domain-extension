package org.openerproject.targetproperties.sandbox;

import java.io.IOException;

import org.openerproject.targetproperties.svector.indexing.SemanticVectorIndexBuilder;

public class MainSVectorIndexBuildTest {

	public static void main(String[] args) throws IllegalArgumentException, IOException {

		SemanticVectorIndexBuilder semanticVectorIndexBuilder=new SemanticVectorIndexBuilder();
		semanticVectorIndexBuilder.buildIndex("MYINDEX","MYVECTORS",200,3);

	}

}
