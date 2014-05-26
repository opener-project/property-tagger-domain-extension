package org.openerproject.targetproperties.classification;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openerproject.targetproperties.svector.clustering.CosineSimSearcher;

import pitt.search.semanticvectors.vectors.ZeroVectorException;

public class TargetClassifier {

	private String categoryDefinitionsFilePath;
	private String targetsToClassifyFilePath;
	private String luceneIndexPath;
	private String termVectorsFilePath;

	public String getTermVectorsFilePath() {
		return termVectorsFilePath;
	}

	public void setTermVectorsFilePath(String termVectorsFilePath) {
		this.termVectorsFilePath = termVectorsFilePath;
	}

	private CosineSimSearcher cosineSimSearcher;

	public List<TargetToClassify> classifyTargets() {
		List<TargetToClassify> targetsToClassifyList = TargetToClassify.readTargetsToClassify(targetsToClassifyFilePath);
		List<CategoryDefinition> categoryDefinitions = CategoryDefinition.readCategoryDefinitions(categoryDefinitionsFilePath);
		cosineSimSearcher = new CosineSimSearcher(luceneIndexPath, termVectorsFilePath);
		for(TargetToClassify targetToClassify:targetsToClassifyList){
			if(cosineSimSearcher.isInDictionary(targetToClassify.getOpinionTarget())){
				String mostSimilarCategory=getMostSimilarCategory(targetToClassify.getOpinionTarget(), categoryDefinitions);
				targetToClassify.setAssignedCategory(mostSimilarCategory);
			}else{
				targetToClassify.setOutOfDictionaryTerm(true);
			}
		}
		return targetsToClassifyList;
	}
	
	protected String getMostSimilarCategory(String target, List<CategoryDefinition>categoryDefinitions){
		String candidateCategory="";
		double maxScore=0;
		for(CategoryDefinition categoryDefinition:categoryDefinitions){
			double currentScore=getScore(target, categoryDefinition.getRepresentativeTargets());
			if(currentScore>maxScore){
				maxScore=currentScore;
				candidateCategory=categoryDefinition.getCategoryName();
			}
		}
		return candidateCategory;
	}
	
	private double getScore(String target, List<String> representatives) {
		try {
			double score = cosineSimSearcher.getCosineDistance(target, StringUtils.join(representatives, " "));
			return score;
		} catch (IOException | ZeroVectorException e) {
			throw new RuntimeException(e);
		}
	}

	public String getCategoryDefinitionsFilePath() {
		return categoryDefinitionsFilePath;
	}

	public void setCategoryDefinitionsFilePath(String categoryDefinitionsFilePath) {
		this.categoryDefinitionsFilePath = categoryDefinitionsFilePath;
	}

	public String getTargetsToClassifyFilePath() {
		return targetsToClassifyFilePath;
	}

	public void setTargetsToClassifyFilePath(String targetsToClassifyFilePath) {
		this.targetsToClassifyFilePath = targetsToClassifyFilePath;
	}

	public String getLuceneIndexPath() {
		return luceneIndexPath;
	}

	public void setLuceneIndexPath(String luceneIndexPath) {
		this.luceneIndexPath = luceneIndexPath;
	}

}
