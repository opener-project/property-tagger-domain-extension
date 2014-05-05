package org.openerproject.targetproperties.data;

import java.io.File;
import java.util.List;

/**
 * Interface devoted to the read of a dataset to convert in a List of DatasetDocument objects
 * @author yo
 *
 */
public interface DatasetManager {

	public List<DatasetDocument> readDataset(List<File>datasetFiles);
	
	public List<OpinionTarget> readTargetAndPropertyList(File file);
	
	
	
}
