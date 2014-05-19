package org.openerproject.targetproperties.evaluation;

import java.util.Set;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

public class EvaluationCounts {

	private Table<String,String,Integer>confusionMatrix=HashBasedTable.create();
	
	public void addResult(String goldCategory, String predictedCategory){
		int value = getValue(goldCategory, predictedCategory);
		confusionMatrix.put(goldCategory, predictedCategory, value+1);
	}
	
	public int getValue(String goldCategory,String predictedCategory){
		Integer value=confusionMatrix.get(goldCategory, predictedCategory);
		if(value==null){
			value=0;
		}
		return value;
	}
	
	public void printConfusionMatrix(){
		Set<String> categories = confusionMatrix.columnMap().keySet();
		System.out.print("G\\P\t");
		for(String goldCategory:categories){
			System.out.print(goldCategory+"\t");
		}
		System.out.println();
		for(String goldCategory:categories){
			System.out.print(goldCategory+"\t");
			for(String predictedCategory:categories){
				System.out.print(getValue(goldCategory, predictedCategory)+"\t");
			}
			System.out.println();
		}
	}
	
}
