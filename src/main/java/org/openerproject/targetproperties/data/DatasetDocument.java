package org.openerproject.targetproperties.data;

import java.util.List;

/**
 * This interface represents an abstract document of a dataset
 * 
 * @author yo
 * 
 */
public class DatasetDocument {

	private String rawText;
	private List<OpinionTarget> opinionTargets;

	/**
	 * Returns the original text
	 * 
	 * @return
	 */
	public String getRawText() {
		return rawText;
	}

	// /**
	// * Returns the text suitable to feed sspace algorithm (tokens, which are
	// lemmas, separated by whitespace)
	// * @return
	// */
	// public String getProcessedText();

	public void setRawText(String rawText) {
		this.rawText = rawText;
	}

	public void setOpinionTargets(List<OpinionTarget> opinionTargets) {
		this.opinionTargets = opinionTargets;
	}

	/**
	 * The "gold" opinion targets if any (the spans, etc., will be relative to
	 * the original text)
	 * 
	 * @return
	 */
	public List<OpinionTarget> getOpinionTargets() {
		return opinionTargets;
	}

}
