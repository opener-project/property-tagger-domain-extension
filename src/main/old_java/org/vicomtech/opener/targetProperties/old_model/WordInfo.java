package org.vicomtech.opener.targetProperties.old_model;

import ixa.kaflib.Term;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

public class WordInfo {

	private String wordform;
	private String lemma;
	private String tag;

	private List<WordInfo> composingWords;

	private WordInfo(String wordForm,String lemma,String tag,List<WordInfo>composingWords){
		super();
		this.wordform=wordForm;
		this.lemma=lemma;
		this.tag=tag;
		if(composingWords==null){
			this.composingWords=Collections.emptyList();
		}else{
			this.composingWords=composingWords;
		}
	}
	
	public static WordInfo createWordInfo(List<Term>terms){
		StringBuffer wordformBuffer=new StringBuffer();
		StringBuffer lemmaBuffer=new StringBuffer();
		String tag="MW";
		List<WordInfo>composingWords=Lists.newArrayList();
		for(Term term:terms){
			wordformBuffer.append(term.getForm());
			wordformBuffer.append(" ");
			lemmaBuffer.append(term.getLemma());
			lemmaBuffer.append(" ");
			composingWords.add(createWordInfo(term));
		}
		WordInfo wordInfo=new WordInfo(wordformBuffer.toString().trim(), lemmaBuffer.toString().trim(), tag, composingWords);
		return wordInfo;
	}
	
	public static WordInfo createWordInfo(Term term){
		WordInfo wordInfo=new WordInfo(term.getForm(), term.getLemma(), term.getPos(), Collections.<WordInfo>emptyList());
		return wordInfo;
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

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public List<WordInfo> getComposingWords() {
		if(this.composingWords.size()==0){
			return Lists.newArrayList(this);
		}
		return composingWords;
	}

	public void setComposingWords(List<WordInfo> composingWords) {
		this.composingWords = composingWords;
	}
	
	public boolean isNoun(){
		return this.tag.equals("N");
	}

	public boolean isAdverb(){
		return this.tag.equals("A");
	}
	
	public boolean isAdj(){
		return this.tag.equals("G");
	}
	
	public boolean isVerb(){
		return this.tag.equals("V");
	}
}
