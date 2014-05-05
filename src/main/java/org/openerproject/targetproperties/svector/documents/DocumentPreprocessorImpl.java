package org.openerproject.targetproperties.svector.documents;

import ixa.kaflib.KAFDocument;
import ixa.kaflib.Term;
import ixa.kaflib.WF;

import java.io.StringReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openerproject.targetproperties.kaf.Analyzer;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

public class DocumentPreprocessorImpl implements DocumentPreprocessor {

	private Analyzer analyzer;

	private Multimap<String, List<String>> multiwordMap=ArrayListMultimap.create();// =loadMultiwordMap(null);

	@Override
	public List<String> preprocessDocument(String content, String language, boolean isKaf) {
		//loadMultiwords(multiwords);
		String kaf = null;
		if (isKaf) {
			kaf = content;
		} else {
			kaf = getKAF(content, language);
		}

		List<String> preprocessedContent = getPreprocessedContent(kaf);
		return preprocessedContent;
	}

	public String getKAF(String documentContent, String language) {
		String kaf = null;
		if (language != null && language.length() != 0) {
			kaf = analyzer.analyzeText(documentContent, language);
		} else {
			kaf = analyzer.analyzeText(documentContent);
		}
		return kaf;
	}

	public List<String> getPreprocessedContent(String kaf) {
		try {
			List<String> results = Lists.newArrayList();
			KAFDocument kafdoc = KAFDocument.createFromStream(new StringReader(kaf));
			List<List<WF>> sentences = kafdoc.getSentences();
			for (List<WF> sentence : sentences) {
				int sentenceNumber = sentence.get(0).getSent();
				List<Term> sentenceTerms = kafdoc.getSentenceTerms(sentenceNumber);
				String composedTerms = composeTerms(sentenceTerms);
				results.add(composedTerms);
			}
			return results;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	protected String composeTerms(List<Term> sentenceTerms) {
		StringBuffer composition = new StringBuffer();
		for (int currentIndex = 0; currentIndex < sentenceTerms.size(); currentIndex++) {
			List<String> subwords = checkMultiword(sentenceTerms, currentIndex);
			if (subwords.size() > 1) {
				composition.append(StringUtils.join(subwords, "_").trim());
				currentIndex += subwords.size() - 1;
				composition.append(" ");
			} else {
				Term currentTerm = sentenceTerms.get(currentIndex);
				if (isValidPostag(currentTerm.getPos())) {
					composition.append(currentTerm.getLemma());
					composition.append(" ");
				}
			}
		}
		return composition.toString().trim();
	}

	protected List<String> checkMultiword(List<Term> sentenceTerms, int currentIndex) {
		Term currentTerm = sentenceTerms.get(currentIndex);
		String currentTermLemma = currentTerm.getLemma();
		Collection<List<String>> multiwordCandidates = multiwordMap.get(currentTermLemma);
		Iterator<List<String>> multiWordCandidatesIterator = multiwordCandidates.iterator();
		while (multiWordCandidatesIterator.hasNext()) {
			List<String> multiwordComponents = multiWordCandidatesIterator.next();
			List<Term> followingWordsInSentence = sentenceTerms.subList(currentIndex,
					Math.min(currentIndex + multiwordComponents.size(), sentenceTerms.size()));
			boolean multiwordMatches = check(multiwordComponents, followingWordsInSentence);
			if (multiwordMatches) {
				return multiwordComponents;
			}
		}
		return Lists.newArrayList(currentTermLemma);
	}

	private boolean check(List<String> multiwordComponents, List<Term> followingWordsInSentence) {
		if (multiwordComponents.size() != followingWordsInSentence.size()) {
			return false;
		}
		for (int i = 0; i < multiwordComponents.size(); i++) {
			if (!multiwordComponents.get(i).equalsIgnoreCase(followingWordsInSentence.get(i).getLemma())) {
				return false;
			}
		}
		return true;
	}

	public boolean isValidPostag(String postag) {
		boolean isValid = postag.startsWith("N") || postag.startsWith("V") || postag.startsWith("A")
				|| postag.startsWith("G");
		return isValid;
	}

	public void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}

	//
	public void loadMultiwords(List<String> multiwords) {
		Multimap<String, List<String>> multiwordMap = ArrayListMultimap.create();
		for (String multiword : multiwords) {
			List<String> components = Arrays.asList(multiword.split(" "));
			String firstComp = components.get(0);
			multiwordMap.put(firstComp, components);
		}
		this.multiwordMap = multiwordMap;
	}

}
