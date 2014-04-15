package org.vicomtech.opener.targetProperties.data;

import ixa.kaflib.KAFDocument;
import ixa.kaflib.Opinion;
import ixa.kaflib.Opinion.OpinionTarget;
import ixa.kaflib.Term;

import java.io.StringReader;
import java.util.List;

import org.vicomtech.opener.targetProperties.model.TargetAndContext;
import org.vicomtech.opener.targetProperties.model.WordInfo;

import com.google.common.collect.Lists;

public class KafTargetAndContextExtractor implements TargetAndContextExtractor{

	//default value 3
	private int contextWindowSize=3;
	private KAFDocument kafDoc;
	
	@Override
	public synchronized List<TargetAndContext> getTargetsAndContext(String documentContent) {
		try{
			kafDoc=KAFDocument.createFromStream(new StringReader(documentContent));
			
			List<Opinion> opinions = kafDoc.getOpinions();
			List<TargetAndContext>targetsAndContext=Lists.newArrayList();
			for(Opinion opinion:opinions){
				TargetAndContext targetAndContext=new TargetAndContext();
				OpinionTarget opinionTarget = opinion.getOpinionTarget();
				if(opinionTarget!=null){
					List<Term> terms = opinionTarget.getTerms();
					WordInfo wordInfo=WordInfo.createWordInfo(terms);
					targetAndContext.setTarget(wordInfo);
					targetAndContext.setPreviousWords(getPreviousWords(opinionTarget));
					targetAndContext.setAfterWords(getAfterWords(opinionTarget));
					targetsAndContext.add(targetAndContext);
				}
			}
			return targetsAndContext;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}

	//public WordInfo 
	
	public List<WordInfo>getPreviousWords(OpinionTarget opinionTarget){
		List<Term> opinionTargetTerms = opinionTarget.getTerms();
		Term firstTerm=opinionTargetTerms.get(0);
		List<Term>docTerms=kafDoc.getTerms();
		
		for (int i = 0; i < docTerms.size(); i++) {
			Term currentTerm = docTerms.get(i);
			if (currentTerm.getId().equalsIgnoreCase(firstTerm.getId())) {
				List<WordInfo> beforeWords = Lists.newArrayList();
				for (int j = Math.max(0, i - contextWindowSize); j < i; j++) {
					beforeWords.add(WordInfo.createWordInfo(docTerms.get(j)));
				}
				return beforeWords;
			}
		}
		return null;
	}
	
	public List<WordInfo>getAfterWords(OpinionTarget opinionTarget){
		List<Term> opinionTargetTerms = opinionTarget.getTerms();
		Term lastTerm=opinionTargetTerms.get(opinionTargetTerms.size()-1);
		List<Term>docTerms=kafDoc.getTerms();
		
		for (int i = 0; i < docTerms.size(); i++) {
			Term currentTerm = docTerms.get(i);
			if (currentTerm.getId().equalsIgnoreCase(lastTerm.getId())) {
				List<WordInfo> afterWords = Lists.newArrayList();
				for (int j = i; j < Math.min(docTerms.size(), i + contextWindowSize); j++) {
					afterWords.add(WordInfo.createWordInfo(docTerms.get(j)));
				}
				return afterWords;
			}
		}
		return null;
	}
	
}
