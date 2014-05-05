package org.openerproject.targetproperties.svector.documents;

import java.util.List;

public interface DocumentPreprocessor {

	/*
	 * Aquí el input debería ser un "documento"? o su contenido?
	 */
	
	/**
	 * Reads the text content of a document (the actual content, not any metadata) and performs any preprocessing on it.
	 * The preprocessing could be analyzing (obtain KAF), and using the analysis info to obtain a newly arranged content.
	 * The result could be more than one (a List), because a single document may be broken into pieces (sentences, etc.)
	 * @param content
	 * @param language
	 * @return
	 */
	public List<String> preprocessDocument(String content, String language,boolean isKaf);
	
}
