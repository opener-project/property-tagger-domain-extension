package org.openerproject.targetproperties.svector.documents;

import static org.junit.Assert.*;
import ixa.kaflib.KAFDocument;
import ixa.kaflib.Term;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
//import org.openerproject.targetproperties.kaf.CustomWebServicesKafAnalyzer;
import org.openerproject.targetproperties.svector.documents.DocumentPreprocessorImpl;

import com.google.common.collect.Lists;

public class DocumentPreprocessorImplTest {

	private DocumentPreprocessorImpl documentPreprocessor;
	
	@Before
	public void setUp() throws Exception {
		documentPreprocessor=new DocumentPreprocessorImpl();
		//documentPreprocessor.setAnalyzer(new CustomWebServicesKafAnalyzer());
	}

	@Test
	public void testComposeTerms() throws IOException {
		
		List<String>multiwords=Lists.newArrayList("air conditioning","value for money");
		documentPreprocessor.loadMultiwords(multiwords);
		
		
		KAFDocument kafDoc=KAFDocument.createFromFile(new File("src/test/resources/fakeKaf.kaf"));
		Term t1=kafDoc.createTerm("closed", "air", "N", Lists.newArrayList(kafDoc.createWF("air")));
		Term t2=kafDoc.createTerm("closed", "conditioning", "N", Lists.newArrayList(kafDoc.createWF("conditioning")));
		Term t3=kafDoc.createTerm("closed", "be", "V", Lists.newArrayList(kafDoc.createWF("is")));
		Term t4=kafDoc.createTerm("closed", "a", "D", Lists.newArrayList(kafDoc.createWF("a")));
		Term t5=kafDoc.createTerm("closed", "shit", "N", Lists.newArrayList(kafDoc.createWF("shit")));
		Term t6=kafDoc.createTerm("closed", "but", "C", Lists.newArrayList(kafDoc.createWF("but")));
		Term t7=kafDoc.createTerm("closed", "the", "D", Lists.newArrayList(kafDoc.createWF("the")));
		Term t8=kafDoc.createTerm("closed", "value", "N", Lists.newArrayList(kafDoc.createWF("value")));
		Term t9=kafDoc.createTerm("closed", "for", "P", Lists.newArrayList(kafDoc.createWF("for")));
		Term t10=kafDoc.createTerm("closed", "money", "N", Lists.newArrayList(kafDoc.createWF("money")));
		List<Term>sentenceTerms=Lists.newArrayList(t1,t2,t3,t4,t5,t6,t7,t8,t9,t10);
		
		String s = documentPreprocessor.composeTerms(sentenceTerms);
		System.out.println(s);
		assertEquals("air_conditioning be shit value_for_money", s);
	}

}
