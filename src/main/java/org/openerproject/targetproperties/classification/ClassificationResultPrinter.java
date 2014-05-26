package org.openerproject.targetproperties.classification;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.openerproject.targetproperties.evaluation.resultprinting.HtmlComparisonTable;
import org.openerproject.targetproperties.evaluation.resultprinting.HtmlConfusionMatrix;
import org.openerproject.targetproperties.evaluation.resultprinting.HtmlResultCountTable;

import com.google.common.collect.Lists;

public class ClassificationResultPrinter {

	public void printClassifiedTargets(List<TargetToClassify>classifiedTargets,OutputStream os){
		Collections.sort(classifiedTargets);
		List<String>lines=Lists.newArrayList();
		for(TargetToClassify targetToClassify:classifiedTargets){
			lines.add(getOutputFormatForClassifiedTarget(targetToClassify));
		}
		try {
			IOUtils.writeLines(lines, System.lineSeparator(), os,"UTF-8");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private String getOutputFormatForClassifiedTarget(TargetToClassify targetToClassify){
		return targetToClassify.getOpinionTarget()+"\t"+targetToClassify.getAssignedCategory();
	}
	
	public void printHTMLEvaluation(List<TargetToClassify>classifiedTargets,OutputStream os){
		Collections.sort(classifiedTargets);
		HtmlComparisonTable htmlComparisonTable=new HtmlComparisonTable();
		String comparisonTableHtmlStr=htmlComparisonTable.getHtmlTable(classifiedTargets);
		HtmlConfusionMatrix htmlConfusionMatrix=HtmlConfusionMatrix.createNew("Confusion matrix");
		List<String>differentCategories=Lists.newArrayList();
		for(TargetToClassify targetToClassify:classifiedTargets){
			if(targetToClassify.isOutOfDictionaryTerm()){
				continue;
			}
			String predictedCategory=targetToClassify.getAssignedCategory();
			String goldCategory=targetToClassify.getGoldCategory();
			htmlConfusionMatrix.incrementValue(predictedCategory, goldCategory);
			if(!differentCategories.contains(goldCategory)){
				differentCategories.add(goldCategory);
				htmlConfusionMatrix.addHeaderCell(goldCategory, goldCategory);
				htmlConfusionMatrix.addRowName(goldCategory, goldCategory);
			}
		}
//		for(String category:differentCategories){
//			htmlConfusionMatrix.addHeaderCell(category, category);
//		}
		String confusionMatrixHtmlStr=htmlConfusionMatrix.generateHtml();
		
		HtmlResultCountTable htmlResultCountTable=new HtmlResultCountTable();
		String resultCountTableString=htmlResultCountTable.getHtmlResultCountTable(classifiedTargets);
		
		StringBuffer sb=new StringBuffer();
		sb.append(getHtmlDocumentHeader());
		sb.append(comparisonTableHtmlStr);
		sb.append(resultCountTableString);
		sb.append(confusionMatrixHtmlStr);
		sb.append(getHtmlDocumentFooter());
		try {
			IOUtils.write(sb.toString(), os,"UTF-8");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	protected String getHtmlDocumentHeader(){
		StringBuffer sb=new StringBuffer();
		sb.append("<html lang=\"en\"><head><title>Results</title></head><body>");
		return sb.toString();
	}
	
	protected String getHtmlDocumentFooter(){
		StringBuffer sb=new StringBuffer();
		sb.append("</body></html>");
		return sb.toString();
	}
	
}
