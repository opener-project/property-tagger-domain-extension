package org.openerproject.targetproperties.svector.indexing;


//import java.io.File;



public class SemanticVectorIndexBuilder {

	public static void main(String[]args){
		//args=new String[]{"-dimension","200","-luceneindexpath","MAIN_OUTPUT/lucene_index","outputFolder="+"MAIN_OUTPUT"};
		SemanticVectorIndexBuilder semanticVectorIndexBuilder=new SemanticVectorIndexBuilder();
		semanticVectorIndexBuilder.buildIndex("MAIN_OUTPUT/lucene_index", "MAIN_OUTPUT",200,3);
	}
	
	public void buildIndex(String pathToLuceneIndex,String outputFolderPath,int numDimensions, int numCycles){
		try{
		//File luceneIndex=new File(pathToLuceneIndex);2
		String[]args=new String[]{"-dimension",""+numDimensions,"-luceneindexpath",pathToLuceneIndex,"-trainingcycles",""+numCycles, "-initialtermvectors", "random","outputFolder="+outputFolderPath};
		//System.out.println(BuildIndex.usageMessage);
		BuildIndexTweakedWithOutputPath.main(args);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
}
