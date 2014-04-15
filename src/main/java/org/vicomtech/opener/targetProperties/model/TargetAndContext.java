package org.vicomtech.opener.targetProperties.model;

import java.util.List;

public class TargetAndContext {

	private WordInfo target;
	private List<WordInfo> previousWords;
	private List<WordInfo> afterWords;

	public WordInfo getTarget() {
		return target;
	}

	public void setTarget(WordInfo target) {
		this.target = target;
	}

	public List<WordInfo> getPreviousWords() {
		return previousWords;
	}

	public void setPreviousWords(List<WordInfo> previousWords) {
		this.previousWords = previousWords;
	}

	public List<WordInfo> getAfterWords() {
		return afterWords;
	}

	public void setAfterWords(List<WordInfo> afterWords) {
		this.afterWords = afterWords;
	}

}
