package org.openerproject.targetproperties.sandbox;

import org.openerproject.targetproperties.svector.indexing.LuceneIndexCreator;

public class MainLuceneIndexTest {

	public static void main(String[] args) {
		LuceneIndexCreator luceneIndexCreator=new LuceneIndexCreator();
		
		luceneIndexCreator.createLuceneIndex("CORPUS", "MYINDEX");
	}

}
