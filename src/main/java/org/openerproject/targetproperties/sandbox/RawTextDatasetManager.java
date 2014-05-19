package org.openerproject.targetproperties.sandbox;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.Lists;

@Deprecated
public class RawTextDatasetManager implements DatasetManager{

	@Override
	public List<DatasetDocument> readDataset(List<File> datasetFiles) {
		List<DatasetDocument>dataset=Lists.newArrayList();
		for(File datasetFile:datasetFiles){
			try {
				
				List<String> lines=FileUtils.readLines(datasetFile,"UTF-8");
				//treat each line as a document? I think it does not matter...
				for(String line:lines){
					DatasetDocument datasetDocument=new DatasetDocument();
					datasetDocument.setRawText(line);
					datasetDocument.setOpinionTargets(Collections.<OpinionTarget>emptyList());
					dataset.add(datasetDocument);
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return dataset;
	}

	@Override
	public List<OpinionTarget> readTargetAndPropertyList(File file) {
		// The raw texts do not have any annotated opinion targets, they are just raw texts
		return Collections.<OpinionTarget>emptyList();
	}



}
