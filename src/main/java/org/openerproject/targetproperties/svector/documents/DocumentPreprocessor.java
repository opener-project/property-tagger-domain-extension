package org.openerproject.targetproperties.svector.documents;

import java.util.List;

public interface DocumentPreprocessor {

	
	/**
	 * Reads a KAF document and performs any preprocessing on it.
	 * The result could be more than one (a List), because a single document may be broken into pieces (sentences, etc.)
	 * @param content
	 * @param language
	 * @return
	 */
	public List<String> preprocessDocument(String content);
	
	
	/**
	 * Load multiwords from a list of multiwords
	 */
	public void loadMultiwords(List<String>multiwords);
	
}
