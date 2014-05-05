package org.openerproject.targetproperties.sspace;

import ixa.kaflib.KAFDocument;
import ixa.kaflib.Term;

import java.io.BufferedReader;
//import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.apache.commons.io.IOUtils;
//import org.apache.commons.lang.StringUtils;
import org.openerproject.targetproperties.data.DatasetDocument;
import org.openerproject.targetproperties.kaf.Analyzer;

public class AlgorithmExecutorWithKAF extends AlgorithmExecutor {

	private Analyzer kafAnalyzer;
	private String language;

	@Override
	public BufferedReader preprocessDocument(DatasetDocument document, String language) {
		this.language=language;
		String documentContent = document.getRawText();//getDocumentContent(document);
		String kaf = getKAF(documentContent);
		String preprocessedContent=getPreprocessedContent(kaf);
		return IOUtils.toBufferedReader(new StringReader(preprocessedContent));
	}

//	public String getDocumentContent(BufferedReader document) {
//		try {
//			List<String> lines = IOUtils.readLines(document);
//			String documentContent = StringUtils.join(lines, "\n");
//			return documentContent;
//		} catch (IOException e) {
//			throw new RuntimeException(e);
//		}
//	}

	public String getKAF(String documentContent) {
		String kaf = null;
		if (language != null && language.length() != 0) {
			kaf = kafAnalyzer.analyzeText(documentContent, language);
		} else {
			kaf = kafAnalyzer.analyzeText(documentContent);
		}
		return kaf;
	}
	
	public String getPreprocessedContent(String kaf){
		try{
			KAFDocument kafdoc=KAFDocument.createFromStream(new StringReader(kaf));
			StringBuffer sb=new StringBuffer();
//			List<List<WF>> sentences = kafdoc.getSentences();
//			for(List<WF> sentence:sentences){
//				int sentenceNumber=sentence.get(0).getSent();
//				List<Term> sentenceTerms = kafdoc.getSentenceTerms(sentenceNumber);
//				for(Term term:sentenceTerms){
//					if(isValidPostag(term.getPos())){
//						sb.append(term.getLemma());
//						sb.append(" ");
//					}
//				}
//			}
			
			List<Term> terms = kafdoc.getTerms();
			for(Term term:terms){
				if(isValidPostag(term.getPos())){
					sb.append(term.getLemma());
					sb.append(" ");
				}
			}
			return sb.toString().trim();
			
		}catch(Exception e){
			throw new RuntimeException(e);
		}
		
	}
	
	public boolean isValidPostag(String postag){
		boolean isValid=postag.startsWith("N") || postag.startsWith("V") || postag.startsWith("A") || postag.startsWith("G");
		return isValid;
	}

	public void setKafAnalyzer(Analyzer kafAnalyzer) {
		this.kafAnalyzer = kafAnalyzer;
	}

}
