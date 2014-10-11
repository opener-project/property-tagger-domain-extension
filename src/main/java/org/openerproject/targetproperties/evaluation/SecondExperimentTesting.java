package org.openerproject.targetproperties.evaluation;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.openerproject.targetproperties.evaluation.RepresentativeTargetPropertiesSampler.CategoryRepresentatives;
import org.openerproject.targetproperties.evaluation.RepresentativeTargetPropertiesSampler.TargetAndCategory;
import org.openerproject.targetproperties.svector.clustering.CosineSimSearcher;

import com.google.common.collect.Lists;

import pitt.search.semanticvectors.vectors.ZeroVectorException;

public class SecondExperimentTesting {

	public static final String LANG="it";
	public static final String OUTPUT_DIR="MAIN_OUTPUT_"+LANG.toUpperCase();
	public static final String GOLD_FILE_PATH = "GOLD_TARGET_PROPERTIES/"+LANG+"_aspects.txt";
	public static final String LUCENE_INDEX_PATH =OUTPUT_DIR+"/lucene_index";
	public static final String TERM_VECTORS_FILE_PATH = OUTPUT_DIR+"/termvectors3.bin";
	
	public static final int NUMBER_OF_REPRESENTATIVES=10;
	
	public static final String RESULTS_FILE=OUTPUT_DIR+"/RESULTS/"+LANG+"_result.txt";
	
	private static CosineSimSearcher cosineSimSearcher = new CosineSimSearcher(LUCENE_INDEX_PATH, TERM_VECTORS_FILE_PATH);

	public static void main(String[] args) throws IOException, ZeroVectorException {

		//VerbatimLogger.log(Level.OFF, "ASDDSAD");
		
		List<CategoryRepresentatives> representatives = RepresentativeTargetPropertiesSampler
				.getRepresentativesFromGoldFile(GOLD_FILE_PATH,NUMBER_OF_REPRESENTATIVES);

		System.out.println("Num categories in total: "+representatives.size());
		
		List<TargetAndCategory> targetsToClassify = RepresentativeTargetPropertiesSampler
				.getTargetsToClassify(GOLD_FILE_PATH);
		int correct = 0;
		int incorrect = 0;
		int outOfDictionary=0;
		List<String>results=Lists.newArrayList();
		for (TargetAndCategory targetAndCategory : targetsToClassify) {
			String targetToClassify = targetAndCategory.getTarget();
			boolean targetIsOutOfDict=cosineSimSearcher.isInDictionary(targetToClassify);
			if(!targetIsOutOfDict){
				outOfDictionary++;
				continue;
			}
			String candidateCategory = "";
			double maxScore = 0;
			for (CategoryRepresentatives categoryRepresentatives : representatives) {
				String currentCategory = categoryRepresentatives.getCategoryName();
				List<String> currentCategoryRepresentatives = categoryRepresentatives.getRepresentativeTargets();
				double avgScore = getAverageScore(targetToClassify, currentCategoryRepresentatives);
				if (avgScore > maxScore) {
					candidateCategory = currentCategory;
					maxScore = avgScore;
				}
			}
			results.add("TARGET: "+targetToClassify+" classified as "+candidateCategory+" (expected: "+targetAndCategory.getCategory()+")");
			if (candidateCategory.equalsIgnoreCase(targetAndCategory.getCategory())) {
				correct++;
			} else {
				incorrect++;
			}
		}
		for(String result:results){
			System.out.println(result);
		}
		
		String resultValues="Correct: " + correct + "\nIncorrect: " + incorrect+"\nPrecision="+(float)correct/((float)incorrect+(float)correct)+"\nOut of dictionary targets: "+outOfDictionary;
		System.out.println(resultValues);
		results.add(resultValues);
		FileUtils.writeLines(new File(RESULTS_FILE), results);
	}

	public static double getAverageScore(String target, List<String> representatives) throws IOException,
			ZeroVectorException {
		
//		double acumScore = Double.MAX_VALUE;
//		int numWordsInDict=0;
//		for (String representative : representatives) {
//			double score = cosineSimSearcher.getCosineDistance(target, representative);
//			if(!Double.isNaN(score)){
//				acumScore += score;
//				numWordsInDict++;
//			}
//			
//		}
//		double avgScore = acumScore / (double) numWordsInDict;
		target=StringUtils.join(target.split("/|-")," ");
			double avgScore=cosineSimSearcher.getCosineDistance(target, StringUtils.join(representatives," "));
		
		
//		double maxScore = 0;
//		//int numWordsInDict=0;
//		for (String representative : representatives) {
//			double score = cosineSimSearcher.getCosineDistance(target, representative);
//			if(!Double.isNaN(score) && score>maxScore){
//				maxScore = score;
//				//numWordsInDict++;
//			}
//			
//		}
//		double avgScore = maxScore ;/// (double) numWordsInDict;
		return avgScore;
		
	}

}
