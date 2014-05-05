package org.openerproject.targetproperties.svector.documents;

import ixa.kaflib.KAFDocument;
import ixa.kaflib.Term;
import ixa.kaflib.WF;

import java.io.StringReader;
import java.util.List;

import org.openerproject.targetproperties.kaf.Analyzer;

import com.google.common.collect.Lists;

public class DocumentPreprocessorImpl implements DocumentPreprocessor{

	private Analyzer analyzer;
	
	@Override
	public List<String> preprocessDocument(String content,String language, boolean isKaf) {
		String kaf=null;
		if(isKaf){
			kaf=content;
		}else{
			kaf = getKAF(content, language);
		}
		
		List<String> preprocessedContent=getPreprocessedContent(kaf);
		return preprocessedContent;
	}

	public String getKAF(String documentContent,String language) {
		String kaf = null;
		if (language != null && language.length() != 0) {
			kaf = analyzer.analyzeText(documentContent, language);
		} else {
			kaf = analyzer.analyzeText(documentContent);
		}
		return kaf;
	}
	
	public List<String> getPreprocessedContent(String kaf){
		try{
			List<String>results=Lists.newArrayList();
			KAFDocument kafdoc=KAFDocument.createFromStream(new StringReader(kaf));
			List<List<WF>> sentences = kafdoc.getSentences();
			for(List<WF> sentence:sentences){
				StringBuffer sb=new StringBuffer();
				int sentenceNumber=sentence.get(0).getSent();
				List<Term> sentenceTerms = kafdoc.getSentenceTerms(sentenceNumber);
				for(Term term:sentenceTerms){
					if(isValidPostag(term.getPos())){
						sb.append(term.getLemma());
						sb.append(" ");
					}
				}
				results.add(sb.toString().trim());
			}
			return results;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
		
	}
	
	public boolean isValidPostag(String postag){
		boolean isValid=postag.startsWith("N") || postag.startsWith("V") || postag.startsWith("A") || postag.startsWith("G");
		return isValid;
	}

	public void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}

	
}
