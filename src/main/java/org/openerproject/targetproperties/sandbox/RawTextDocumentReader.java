package org.openerproject.targetproperties.sandbox;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.Lists;

@Deprecated
public class RawTextDocumentReader implements DocumentReader{

	@Override
	public List<DatasetDocument> readDocument(File documentFile) {
		try {
			List<DatasetDocument> dataset=Lists.newArrayList();
			List<String> lines=FileUtils.readLines(documentFile,"UTF-8");
			//treat each line as a document? I think it does not matter...
			for(String line:lines){
				DatasetDocument datasetDocument=new DatasetDocument();
				datasetDocument.setRawText(line);
				datasetDocument.setOpinionTargets(Collections.<OpinionTarget>emptyList());
				
				dataset.add(datasetDocument);
			}
			return dataset;
		} catch (IOException e) {
			e.printStackTrace();
			return Collections.<DatasetDocument>emptyList();
		}
	}

}
