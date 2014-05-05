package org.openerproject.targetproperties.data;

public class OpinionTarget {

	private String wordform;
	private String lemma;
	private String postag;
	private TextSpan span;
	private String category;

	public OpinionTarget() {
		super();
	}

	public OpinionTarget(String wordform, String lemma, String postag) {
		super();
		this.wordform = wordform;
		this.lemma = lemma;
		this.postag = postag;
	}

	public String getWordform() {
		return wordform;
	}

	public void setWordform(String wordform) {
		this.wordform = wordform;
	}

	public String getLemma() {
		return lemma;
	}

	public void setLemma(String lemma) {
		this.lemma = lemma;
	}

	public String getPostag() {
		return postag;
	}

	public void setPostag(String postag) {
		this.postag = postag;
	}

	public TextSpan getSpan() {
		return span;
	}

	public void setSpan(TextSpan span) {
		this.span = span;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

}
