package org.vicomtech.opener.targetProperties.old_data;

import ixa.kaflib.KAFDocument;
import ixa.kaflib.Opinion;
import ixa.kaflib.Opinion.OpinionTarget;
import ixa.kaflib.Term;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.vicomtech.opener.targetProperties.old_model.TargetAndContext;
import org.vicomtech.opener.targetProperties.old_model.WordInfo;

import com.google.common.collect.Lists;

public class KafTargetAndContextExtractor implements TargetAndContextExtractor {

	// default value 3
	private int contextWindowSize = 3;
	private KAFDocument kafDoc;
	private Set<String> alreadyProcessedTargets;

	@Override
	public synchronized List<TargetAndContext> getTargetsAndContext(String documentContent) {
		try {
			kafDoc = parseKAF(documentContent);

			alreadyProcessedTargets = new HashSet<String>();
			List<Opinion> opinions = kafDoc.getOpinions();
			List<TargetAndContext> targetsAndContext = Lists.newArrayList();
			for (Opinion opinion : opinions) {
				TargetAndContext targetAndContext = new TargetAndContext();
				OpinionTarget opinionTarget = opinion.getOpinionTarget();
				if (opinionTarget != null && !checkAndSetAlreadyProcessedTarget(opinionTarget)) {
					List<Term> terms = opinionTarget.getTerms();
					WordInfo wordInfo = WordInfo.createWordInfo(terms);
					targetAndContext.setTarget(wordInfo);
					targetAndContext.setPreviousWords(getPreviousWords(opinionTarget));
					targetAndContext.setAfterWords(getAfterWords(opinionTarget));
					targetsAndContext.add(targetAndContext);
				}
			}
			return targetsAndContext;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected boolean checkAndSetAlreadyProcessedTarget(OpinionTarget opinionTarget) {
		try {
			List<Term> terms = opinionTarget.getTerms();
			Term firstTerm = terms.get(0);
			String termId = firstTerm.getId();
			if (alreadyProcessedTargets.contains(termId)) {
				return true;
			} else {
				alreadyProcessedTargets.add(termId);
				return false;
			}
		} catch (Exception e) {
			System.err.println("Some error: " + e.getMessage());
			return false;
		}
	}

	public List<WordInfo> getPreviousWords(OpinionTarget opinionTarget) {
		try {
			List<Term> opinionTargetTerms = opinionTarget.getTerms();
			Term firstTerm = opinionTargetTerms.get(0);
			List<Term> docTerms = kafDoc.getTerms();

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
		} catch (Exception e) {
			System.err.println("Some error: " + e.getMessage());
			return Collections.<WordInfo> emptyList();
		}
	}

	public List<WordInfo> getAfterWords(OpinionTarget opinionTarget) {
		try {
			List<Term> opinionTargetTerms = opinionTarget.getTerms();
			Term lastTerm = opinionTargetTerms.get(opinionTargetTerms.size() - 1);
			List<Term> docTerms = kafDoc.getTerms();

			for (int i = 0; i < docTerms.size(); i++) {
				Term currentTerm = docTerms.get(i);
				if (currentTerm.getId().equalsIgnoreCase(lastTerm.getId())) {
					List<WordInfo> afterWords = Lists.newArrayList();
					for (int j = i + 1; j < Math.min(docTerms.size(), i + contextWindowSize + 1); j++) {
						afterWords.add(WordInfo.createWordInfo(docTerms.get(j)));
					}
					return afterWords;
				}
			}
			return null;
		} catch (Exception e) {
			System.err.println("Some error: " + e.getMessage());
			return Collections.<WordInfo> emptyList();
		}
	}

	public static KAFDocument parseKAF(String s) throws IOException {
		Pattern p = Pattern.compile("oid=\"\\d+_\\d++\"");
		Matcher m = p.matcher(s);
		int count = 1;
		while (m.find()) {
			// System.out.println("En FIND --> " + m.group() + " --> to: " +
			// "oid=\"o" + count + "\"");
			s = m.replaceFirst("oid=\"o" + count + "\"");
			count++;
			m = p.matcher(s);
		}
		ByteArrayInputStream bis = new ByteArrayInputStream(s.getBytes());
		return KAFDocument.createFromStream(new InputStreamReader(bis));
	}

}
