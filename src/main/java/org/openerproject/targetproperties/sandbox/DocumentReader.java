package org.openerproject.targetproperties.sandbox;

import java.io.File;
import java.util.List;

public interface DocumentReader {

	public List<DatasetDocument> readDocument(File documentFile);
	
}
