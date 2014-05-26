package org.openerproject.targetproperties.classification;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.google.common.collect.Lists;

public class TargetToClassify implements Comparable<TargetToClassify>{

	private static Logger log=Logger.getLogger(TargetToClassify.class);
	
	private String opinionTarget;
	private String goldCategory;
	private String assignedCategory;
	private boolean outOfDictionaryTerm;
	
	public TargetToClassify(String opinionTarget) {
		super();
		this.opinionTarget = opinionTarget;
	}

	public String getOpinionTarget() {
		return opinionTarget;
	}

	public void setOpinionTarget(String opinionTarget) {
		this.opinionTarget = opinionTarget;
	}

	public String getGoldCategory() {
		return goldCategory;
	}

	public void setGoldCategory(String goldCategory) {
		this.goldCategory = goldCategory;
	}

	public String getAssignedCategory() {
		return assignedCategory;
	}

	public void setAssignedCategory(String assignedCategory) {
		this.assignedCategory = assignedCategory;
	}
	
	/**
	 * Parses the file in the input path to obtain the targets to classify. 
	 * The format of the file content should be one target per line, optionally accompanied with its gold category separated with a tab: 
	 *  TARGET1 [ TAB GOLD_CATEGORY] NEW_LINE
	 *  TARGET2 [ TAB GOLD_CATEGORY] NEW_LINE ...
	 * @param targetsToClassifyFilePath
	 * @return
	 */
	public static List<TargetToClassify> readTargetsToClassify(String targetsToClassifyFilePath){
		try {
			List<TargetToClassify>parsedTargetsToClassify=Lists.newArrayList();
			List<String>lines=FileUtils.readLines(new File(targetsToClassifyFilePath),"UTF-8");
			for(String line:lines){
				String[]parts=line.split("\t");
				String target=parts[0];
				TargetToClassify targetToClassify=new TargetToClassify(target);
				if(parts.length>1){
					String goldCategory=parts[1];
					targetToClassify.setGoldCategory(goldCategory);
				}
				parsedTargetsToClassify.add(targetToClassify);
			}
			return parsedTargetsToClassify;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public boolean correctlyClassified(){
		if(goldCategory==null || assignedCategory==null){
			log.warn("Asking for classification result on a target without gold category or without assigned category (returning false)");
			return false;
		}else{
			return goldCategory.equals(assignedCategory);
		}
	}

	public boolean isOutOfDictionaryTerm() {
		return outOfDictionaryTerm;
	}

	public void setOutOfDictionaryTerm(boolean outOfDictionaryTerm) {
		this.outOfDictionaryTerm = outOfDictionaryTerm;
	}

	@Override
	public int compareTo(TargetToClassify anotherTargetToClassify) {
		if(anotherTargetToClassify.goldCategory==null){
			return 1;
		}else if(this.goldCategory==null){
			return -1;
		}else{
			//none of them are null
			int res= anotherTargetToClassify.goldCategory.compareTo(this.goldCategory);
			if(res==0){
				return anotherTargetToClassify.opinionTarget.compareTo(this.opinionTarget);
			}else{
				return res;
			}
		}
	}

}
