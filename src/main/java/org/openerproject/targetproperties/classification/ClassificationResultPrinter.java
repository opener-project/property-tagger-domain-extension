package org.openerproject.targetproperties.classification;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.google.common.collect.Lists;

public class ClassificationResultPrinter {

	public void printClassifiedTargets(List<TargetToClassify>classifiedTargets,OutputStream os){
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
		
	}
	
}
