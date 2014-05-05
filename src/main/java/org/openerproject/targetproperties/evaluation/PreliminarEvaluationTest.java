package org.openerproject.targetproperties.evaluation;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.openerproject.targetproperties.svector.clustering.CosineSimSearcher;

import pitt.search.semanticvectors.vectors.ZeroVectorException;

import com.google.common.collect.Lists;

public class PreliminarEvaluationTest {

	public static final String GOLD_ASPECTS_FILE_PATH="GOLD_TARGET_PROPERTIES/en_aspects.txt";
	
	public static void main(String[] args) throws IOException, ZeroVectorException {

		//Read the Gold aspects file
		List<String>goldAspects=FileUtils.readLines(new File(GOLD_ASPECTS_FILE_PATH), "UTF-8");
		//Split into "train" a "test"
		List<List<String>>trainAndTest=splitInTrainTest(goldAspects, 0.25f);
		List<String>train=trainAndTest.get(0);
		List<String>test=trainAndTest.get(1);
		//Use "train" as reference, and assign to the "test" targets the category of the closest train-target
		
		//VectorSearcher vsear=new VectorSearcherCosine();
		CosineSimSearcher cosineSimSearcher=new CosineSimSearcher("MAIN_OUTPUT/lucene_index", "MAIN_OUTPUT/termvectors4.bin");
		
		Map<String,String>targetToCat=new HashMap<String,String>();
		
		Map<String,String>testTargetToCategory=new HashMap<String,String>();
		int numTestTerms=test.size();
		int textTermCount=1;
		for(String testLine:test){
			System.err.println("Processing test term "+textTermCount+" of "+numTestTerms);
			String testTarget=testLine.split(" ")[0];
			String testCategory=testLine.split(" ")[2];
			testTargetToCategory.put(testTarget, testCategory);
			Map<String,Double>categoryDistances=new HashMap<String,Double>();
			Map<String,Integer>categorySumsCount=new HashMap<String,Integer>();
			for(String trainLine:train){
				String trainTarget=trainLine.split(" ")[0];
				String trainCategory=trainLine.split(" ")[2];
				double distance = cosineSimSearcher.getCosineDistance(testTarget, trainTarget);
				Double prevDistance=categoryDistances.get(trainCategory);
				if(prevDistance==null){
					prevDistance=0.0;
					categorySumsCount.put(trainCategory, 0);
				}
				categoryDistances.put(trainCategory, prevDistance+distance);
				int categorySums=categorySumsCount.get(trainCategory);
				categorySumsCount.put(trainCategory, categorySums+1);
			}
			String candidateCat="";
			double candidateCatDistance=Double.MAX_VALUE;
			for(String category:categoryDistances.keySet()){
				double catDist=categoryDistances.get(category);
				catDist=catDist/(double)categorySumsCount.get(category);
				if(catDist<candidateCatDistance){
					candidateCat=category;
					candidateCatDistance=catDist;
				}
			}
			targetToCat.put(testTarget, candidateCat);
		}
		
		int correctCount=0;
		int errorCount=0;
		for(String testTarget:targetToCat.keySet()){
			String predictedCat=targetToCat.get(testTarget);
			String goldCat=testTargetToCategory.get(testTarget);
			if(predictedCat.equalsIgnoreCase(goldCat)){
				correctCount++;
			}else{
				errorCount++;
			}
		}
		System.out.println("CORRECTS: "+correctCount+"   INCORRECTS: "+errorCount);
//		double score=cosineSimSearcher.getCosineDistance("towel", "toilet");
//		System.out.println("Score: "+score);
	}
	
	public static List<List<String>> splitInTrainTest(List<String> data, float trainPercent){
		List<String>auxData=Lists.newArrayList(data);
		//Collections.copy(auxData, data);
		Collections.shuffle(auxData);
		int splitIndex=(int)((float)auxData.size()*trainPercent);
		List<String> train = auxData.subList(0, splitIndex);
		List<String> test = auxData.subList(splitIndex, auxData.size()-1);
		List<List<String>>result=Lists.newArrayList();
		result.add(train);
		result.add(test);
		return result;
	}

}
