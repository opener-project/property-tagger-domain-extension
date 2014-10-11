package org.openerproject.targetproperties.evaluation.resultprinting;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class HtmlConfusionMatrixTest {

	private HtmlConfusionMatrix htmlConfusionMatrix;
	
	@Before
	public void setUp() throws Exception {
		htmlConfusionMatrix=HtmlConfusionMatrix.createNew("TITLE");
	}

	@Test
	public void testGenerateHtml() {
		
		List<String>cats=Lists.newArrayList();
		for(int i=0;i<15;i++){
			cats.add("category_"+i);
		}
		for(String category:cats){
			htmlConfusionMatrix.addHeaderCell(category,category);
			htmlConfusionMatrix.addRowName(category,category);
		}
		
		for(String category:cats){
			for(String category2:cats){
				if(category.endsWith(category2)){
					htmlConfusionMatrix.addValue(category,category2, (int)Math.round(100*Math.random()*4));
				}else{
					htmlConfusionMatrix.addValue(category,category2, (int)Math.round(100*Math.random()));
				}
				
			}
		}

		htmlConfusionMatrix.printHtml();
		
		//This test was one-shot test to check some stuff... not a real unit test...
		assertTrue(true);
	}
	


}
