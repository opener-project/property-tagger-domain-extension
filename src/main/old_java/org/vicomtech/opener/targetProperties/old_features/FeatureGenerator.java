package org.vicomtech.opener.targetProperties.old_features;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.vicomtech.opener.targetProperties.old_data.KafTargetAndContextExtractor;
import org.vicomtech.opener.targetProperties.old_data.TargetAndContextExtractor;
import org.vicomtech.opener.targetProperties.old_model.TargetAndContext;
import org.vicomtech.opener.targetProperties.old_model.WordInfo;

public class FeatureGenerator {

	public LinkedHashSet<String> getVocabulary(List<File>files) throws IOException{
		LinkedHashSet<String>vocabulary=new LinkedHashSet<String>();
		for (File f : files) {
			String content = FileUtils.readFileToString(f, "UTF-8");
			TargetAndContextExtractor targetAndContextExtractor = new KafTargetAndContextExtractor();
			List<TargetAndContext> targetsAndContexts = targetAndContextExtractor.getTargetsAndContext(content);
			for(TargetAndContext targetAndContext:targetsAndContexts){
				vocabulary.addAll(getValidLemmas(targetAndContext.getTarget()));
				for(WordInfo previousWord:targetAndContext.getPreviousWords()){
					vocabulary.addAll(getValidLemmas(previousWord));
				}
				for(WordInfo afterWord:targetAndContext.getAfterWords()){
					vocabulary.addAll(getValidLemmas(afterWord));
				}
			}
		}
		return vocabulary;
	}
	
	
	protected static List<String> getValidLemmas(WordInfo wordInfo) {
		StringBuffer wordKeySB = new StringBuffer();
		for (WordInfo composingWord : wordInfo.getComposingWords()) {
			if (composingWord.isAdj() || composingWord.isAdverb() || composingWord.isNoun() || composingWord.isVerb()) {
				wordKeySB.append(composingWord.getLemma());
				wordKeySB.append(" ");
			}
		}
		String wordKey = wordKeySB.toString().trim();
		System.out.println("Word key for (" + wordInfo.getWordform() + "): " + wordKey);
		return Arrays.asList(wordKey.split(" "));
	}
	
}
