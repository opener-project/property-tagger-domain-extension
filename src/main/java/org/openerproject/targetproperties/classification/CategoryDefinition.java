package org.openerproject.targetproperties.classification;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

public class CategoryDefinition {

	private String categoryName;
	private List<String> representativeTargets;

	public CategoryDefinition(String categoryName, List<String> representativeTargets) {
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

	/**
	 * Parses the file indicated by the input path and gets a list of CategoryDefinition objects, containing the category names and their representative targets. 
	 * Format of the file consists of each category name and its representative targets in a line, separated by tabs:
	 *  CATEGORY_NAME TAB TARGET1 TAB TARGET2 ... TAB TARGETN
	 *  
	 *  NOTE: If the representatives contain white spaces, those are replace with underscores '_'
	 * @param categoryDefinitionsPath
	 * @return
	 */
	public static List<CategoryDefinition>readCategoryDefinitions(String categoryDefinitionsPath){
		try {
			List<CategoryDefinition>categoryDefinitions=Lists.newArrayList();
			List<String> lines=FileUtils.readLines(new File(categoryDefinitionsPath),"UTF-8");
			for(String line:lines){
				String[] parts=line.split("\t");
				String categoryName=parts[0];
				String[] representativeTargets=Arrays.copyOfRange(parts, 1, parts.length);
				List<String> representatives = Lists.transform(Lists.newArrayList(representativeTargets), new Function<String,String>() {
					@Override
					public String apply(String input) {
						return input.replace(" ", "_");
					}
				});
				CategoryDefinition categoryDefinition=new CategoryDefinition(categoryName, representatives);
				categoryDefinitions.add(categoryDefinition);
			}
			return categoryDefinitions;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
}
