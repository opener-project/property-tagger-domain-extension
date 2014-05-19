package org.openerproject.targetproperties.evaluation;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class EvaluationCountsTest {

	private EvaluationCounts evaluationCounts;
	
	@Before
	public void setUp() throws Exception {
		evaluationCounts=new EvaluationCounts();
	}

	@Test
	public void testAddResult() {
		String cat1="cat1";
		String cat2="cat2";
		String cat3="cat3";
		evaluationCounts.addResult(cat1, cat2);
		evaluationCounts.addResult(cat1, cat2);
		int value = evaluationCounts.getValue(cat1, cat2);
		assertEquals(2, value);
		evaluationCounts.addResult(cat2, cat3);
		value = evaluationCounts.getValue(cat2, cat3);
		assertEquals(1, value);
		value = evaluationCounts.getValue(cat3, cat3);
		assertEquals(0, value);
		
	}

	@Test
	public void testGetValue() {
		String cat1="cat1";
		String cat2="cat2";
		String cat3="cat3";
		evaluationCounts.addResult(cat1, cat2);
		evaluationCounts.addResult(cat1, cat2);
		int value = evaluationCounts.getValue(cat1, cat2);
		assertEquals(2, value);
		evaluationCounts.addResult(cat2, cat3);
		value = evaluationCounts.getValue(cat2, cat3);
		assertEquals(1, value);
		value = evaluationCounts.getValue(cat3, cat3);
		assertEquals(0, value);
	}

	@Test
	public void testPrintConfusionMatrix() {
		String cat1="cat1";
		String cat2="cat2";
		String cat3="cat3";
		evaluationCounts.addResult(cat1, cat2);
		evaluationCounts.addResult(cat1, cat1);
		evaluationCounts.addResult(cat2, cat2);
		evaluationCounts.addResult(cat3, cat2);
		evaluationCounts.addResult(cat2, cat3);
		evaluationCounts.addResult(cat2, cat1);
		evaluationCounts.printConfusionMatrix();
	}

}
