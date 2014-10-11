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

public class SecondExperimentTestingReworked {

//	public static final String LANG = "it";
//	public static final String OUTPUT_DIR = "MAIN_OUTPUT_" + LANG.toUpperCase();
//	public static final String GOLD_FILE_PATH = "GOLD_TARGET_PROPERTIES/" + LANG + "_aspects.txt";
//	public static final String LUCENE_INDEX_PATH = OUTPUT_DIR + "/lucene_index";
//	public static final String TERM_VECTORS_FILE_PATH = OUTPUT_DIR + "/termvectors3.bin";

//	public static final int NUMBER_OF_REPRESENTATIVES = 10;
//
//	public static final String RESULTS_FILE = OUTPUT_DIR + "/RESULTS/" + LANG + "_result.txt";

	
	private String outputDir;
	private String goldFilePath;
	private String luceneIndexPath;
	private String termVectorsFilePath;
	private int correctPredictions;
	private int incorrectPredictions;
	private int outOfDictionaryTargets;
	private String resultsFile;
	private CosineSimSearcher cosineSimSearcher;
	
	public static void main(String[]args){
		String[]langs={"fr"};
		int[]numRepresentativesArray={3,6,9,12};
		int numCyclesWhenTraining=3;
		
		SecondExperimentTestingReworked secondExperimentTestingReworked=new SecondExperimentTestingReworked();
		
		for(String lang:langs){
			for(int numRepresentatives:numRepresentativesArray){
				try {
					secondExperimentTestingReworked.executeExperiment(lang, numCyclesWhenTraining, numRepresentatives);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void executeExperiment(String lang, int numCyclesWhenTraining, int numRepresentatives) throws IOException,
			ZeroVectorException {
		outputDir = "MAIN_OUTPUT_" + lang.toUpperCase();
		goldFilePath = "GOLD_TARGET_PROPERTIES/" + lang + "_aspects.txt";
		luceneIndexPath = outputDir + "/lucene_index";
		termVectorsFilePath = outputDir + "/termvectors" + numCyclesWhenTraining + ".bin";
		resultsFile = outputDir + "/RESULTS/" + lang + "_ncycles-" + numCyclesWhenTraining + "_nRep-"
				+ numRepresentatives + ".html";

		cosineSimSearcher = new CosineSimSearcher(luceneIndexPath, termVectorsFilePath);

		List<CategoryRepresentatives> categoryRepresentativesList = RepresentativeTargetPropertiesSampler
				.getRepresentativesFromGoldFile(goldFilePath, numRepresentatives);
		List<TargetAndCategory> targetsToClassify = RepresentativeTargetPropertiesSampler
				.getTargetsToClassify(goldFilePath);

		correctPredictions = 0;
		incorrectPredictions = 0;
		outOfDictionaryTargets = 0;

		List<String> htmlTableRows = Lists.newArrayList();
		for (TargetAndCategory targetAndCategory : targetsToClassify) {
			String targetToClassify = targetAndCategory.getTarget();
			boolean targetIsOutOfDict = cosineSimSearcher.isInDictionary(targetToClassify);
			if (!targetIsOutOfDict) {
				outOfDictionaryTargets++;
				continue;
			}
			String candidateCategory = "";
			double maxScore = 0;
			for (CategoryRepresentatives categoryRepresentatives : categoryRepresentativesList) {
				String currentCategory = categoryRepresentatives.getCategoryName();
				List<String> currentCategoryRepresentatives = categoryRepresentatives.getRepresentativeTargets();
				double avgScore = getScore(targetToClassify, currentCategoryRepresentatives);
				if (avgScore > maxScore) {
					candidateCategory = currentCategory;
					maxScore = avgScore;
				}
			}
			if (candidateCategory.equalsIgnoreCase(targetAndCategory.getCategory())) {
				correctPredictions++;
			} else {
				incorrectPredictions++;
			}
			htmlTableRows.add(getHTMLTableRow(targetToClassify, candidateCategory, targetAndCategory.getCategory()));
		}
		String html = generateHTMLPageWithResultsTable(htmlTableRows, correctPredictions, incorrectPredictions,
				outOfDictionaryTargets);
		FileUtils.write(new File(resultsFile), html);
	}

	private String getHTMLTableRow(String targetToClassify, String predictedCategory, String expectedCategory) {
		StringBuffer sb = new StringBuffer();
		sb.append("<tr style=\"background-color:"
				+ (predictedCategory.equalsIgnoreCase(expectedCategory) ? "green" : "orange") + ";\">");
		sb.append("<td>" + targetToClassify + "</td>");
		sb.append("<td>" + predictedCategory + "</td>");
		sb.append("<td>" + expectedCategory + "</td>");
		sb.append("</tr>");
		return sb.toString();
	}

	private String generateHTMLPageWithResultsTable(List<String> htmlTableRows, int correctPredictions,
			int incorrectPredictions, int outOfDictionaryTargets) {
		StringBuffer sb = new StringBuffer();
		sb.append("<html><head><title>RESULTS</title></head><body><table><tr><th>OPINION TARGET</th><th>PREDICTED CATEGORY</th><th>GOLD CATEGORY</th></tr>");
		for (String htmlTableRow : htmlTableRows) {
			sb.append(htmlTableRow);
		}
		sb.append("</table><table>" + "<tr><th>Correct predictions</th><td>" + correctPredictions + "</td></tr>"
				+ "<tr><th>Incorrect predictions</th><td>" + incorrectPredictions + "</td></tr>"
				+ "<tr><th>% corrects predictions</th><td>" + (float) correctPredictions
				/ ((float) incorrectPredictions + (float) correctPredictions) + "</td></tr>"
				+ "<tr><th>Out of dictionary terms</th><td>" + outOfDictionaryTargets + "</td></tr>" + "</body></html>");
		return sb.toString();
	}

	public double getScore(String target, List<String> representatives) throws IOException, ZeroVectorException {
		target = StringUtils.join(target.split("/|-"), " ");
		double score = cosineSimSearcher.getCosineDistance(target, StringUtils.join(representatives, " "));
		return score;
	}

}
