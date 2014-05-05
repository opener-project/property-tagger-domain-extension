package org.openerproject.targetproperties.svector.clustering;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.Lists;

public class SemanticVectorClustering {

	public static final String GOLD_ASPECTS_FILE_PATH="GOLD_TARGET_PROPERTIES/en_aspects.txt";
	
	public static void main(String[]args) throws IOException{
		SemanticVectorClustering semanticVectorClustering=new SemanticVectorClustering();
		semanticVectorClustering.executeSemanticVectorClustering();
	}
	
	public void executeSemanticVectorClustering() throws IOException{
		
		List<String>goldAspects=FileUtils.readLines(new File(GOLD_ASPECTS_FILE_PATH), "UTF-8");
		List<String>ourTargets=Lists.newArrayList();
		for(String goldLine:goldAspects){
			ourTargets.add(goldLine.split(" ")[0]);
		}
		String[]args=new String[]{"-numclusters", "5", "MAIN_OUTPUT/termvectors3.bin"};
		ClusterVectorStoreTweaked.mainMethod(args,ourTargets);
		
		//System.out.println(LSA.usageMessage);
	}
	
}
