package org.openerproject.targetproperties.sandbox;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

public class RepresentativeTargetPropertiesSampler {

	//public static final int NUMBER_OF_TARGETS_PER_CATEGORY=3;
	
	public static class TargetAndCategory{
		private String target;
		private String category;
		
		public TargetAndCategory(String target, String category) {
			super();
			this.target = target;
			this.category = category;
		}
		public String getTarget() {
			return target;
		}
		public void setTarget(String target) {
			this.target = target;
		}
		public String getCategory() {
			return category;
		}
		public void setCategory(String category) {
			this.category = category;
		}
	}
	
	public static class CategoryRepresentatives{
		private String categoryName;
		private List<String>representativeTargets;
		
		
		public CategoryRepresentatives() {
			super();
		}
		public CategoryRepresentatives(String categoryName, List<String> representativeTargets) {
			super();
			this.categoryName = categoryName;
			this.representativeTargets = representativeTargets;
		}
		public String getCategoryName() {
			return categoryName;
		}
		public void setCategoryName(String categoryName) {
			this.categoryName = categoryName;
		}
		public List<String> getRepresentativeTargets() {
			return representativeTargets;
		}
		public void setRepresentativeTargets(List<String> representativeTargets) {
			this.representativeTargets = representativeTargets;
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * The String list returned has the format per element: CATEGORY <TAB> TARGET1 <TAB> TARGET2 ...
	 * @param path
	 * @throws IOException
	 */
	public static List<CategoryRepresentatives> getRepresentativesFromGoldFile(String path, int numberOfRepresentatives) throws IOException{
		List<String>lines=FileUtils.readLines(new File(path), "UTF-8");
		Multimap<String,String>categoryToTargetsMap=ArrayListMultimap.create();
		for(String line:lines){
			String[]parts=line.split("\t");
			String targetWord=parts[0];
			//String pos=parts[1];
			String category=parts[2];
			categoryToTargetsMap.put(category, targetWord);
		}
		List<CategoryRepresentatives>selection=Lists.newArrayList();
		for(String key:categoryToTargetsMap.keySet()){
//			StringBuffer sb=new StringBuffer();
//			sb.append(key);
//			sb.append("\t");
			
			int count=0;
			List<String>representatives=Lists.newArrayList();
			for(String value:categoryToTargetsMap.get(key)){
//				sb.append(value.replace(" ", "_"));
//				sb.append("\t");
				representatives.add(value.replace(" ", "_"));
				count++;
				if(count==numberOfRepresentatives){
					break;
				}
			}
			CategoryRepresentatives categoryRepresentatives=new CategoryRepresentatives(key, representatives);
			selection.add(categoryRepresentatives);
		}
		return selection;
	}
	
	public static List<TargetAndCategory>getTargetsToClassify(String path) throws IOException{
		List<String>lines=FileUtils.readLines(new File(path), "UTF-8");
		List<TargetAndCategory>targetsAndCategories=Lists.newArrayList();
		for(String line:lines){
			String[]parts=line.split("\t");
			String targetWord=parts[0];
			//String pos=parts[1];
			String category=parts[2];
			TargetAndCategory targetAndCategory=new TargetAndCategory(targetWord.replace(" ","_"), category);
			targetsAndCategories.add(targetAndCategory);
		}
		return targetsAndCategories;
	}

}
