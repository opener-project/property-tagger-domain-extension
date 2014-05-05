package org.openerproject.targetproperties.kaf;

public interface Analyzer {

	/**
	 * Sets the analysis types that will tell which analysis must be invoked
	 * 
	 * @param analysisTypes
	 */
	public void setAnalysisTypes(AnalysisTypes[] analysisTypes);

	/**
	 * Analyzes the input text, first invoking the language identifier to get
	 * the language
	 * 
	 * @param rawText
	 * @return
	 */
	public String analyzeText(String rawText);

	/**
	 * Analyzes the input text using the provided language
	 * 
	 * @param rawText
	 * @param lang
	 * @return
	 */
	public String analyzeText(String rawText, String lang);

}
