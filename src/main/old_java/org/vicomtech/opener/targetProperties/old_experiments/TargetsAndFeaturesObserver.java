package org.vicomtech.opener.targetProperties.old_experiments;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.vicomtech.opener.targetProperties.old_data.KafTargetAndContextExtractor;
import org.vicomtech.opener.targetProperties.old_data.TargetAndContextExtractor;
import org.vicomtech.opener.targetProperties.old_model.TargetAndContext;
import org.vicomtech.opener.targetProperties.old_model.WordInfo;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Lists;
import com.google.common.collect.Multiset;

public class TargetsAndFeaturesObserver {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String dirPath = GlobalVariables.KAF_FILES_PATH_EN;
		File dir = new File(dirPath);
		List<File> selectedFiles = Lists.newArrayList();
		for (File f : dir.listFiles()) {
			if (f.getName().endsWith(".kaf")) {
				System.out.println("Adding " + f.getName());
				selectedFiles.add(f);
			}
		}
		printTargetsAndFeatures(selectedFiles);
	}

	public static void printTargetsAndFeatures(List<File> files) throws IOException {
		Map<String, Multiset<String>> counts = new HashMap<String, Multiset<String>>();
		for (File f : files) {
			String content = FileUtils.readFileToString(f, "UTF-8");
			TargetAndContextExtractor targetAndContextExtractor = new KafTargetAndContextExtractor();
			List<TargetAndContext> targetsAndContexts = targetAndContextExtractor.getTargetsAndContext(content);
			addToMap(targetsAndContexts, counts);
		}
		for (String targetkey : counts.keySet()) {
			Multiset<String> words = counts.get(targetkey);
			System.out.println("Target: " + targetkey);
			for (String word : words.elementSet()) {
				System.out.println("   >>> " + word + " , " + words.count(word));
			}
		}

	}

	protected static void addToMap(List<TargetAndContext> targetsAndContexts, Map<String, Multiset<String>> counts) {
		for (TargetAndContext targetAndContext : targetsAndContexts) {
			String targetKey = getProcessedWordKey(targetAndContext.getTarget());
			Multiset<String> wordset = counts.get(targetKey);
			if (wordset == null) {
				wordset = HashMultiset.create();
			}
			for (WordInfo previousWord : targetAndContext.getPreviousWords()) {
				String wordkey = getProcessedWordKey(previousWord);
				if (wordkey.length() != 0) {
					wordset.add(wordkey);
				}
			}
			for (WordInfo afterWord : targetAndContext.getAfterWords()) {
				String wordkey = getProcessedWordKey(afterWord);
				if (wordkey.length() != 0) {
					wordset.add(wordkey);
				}

			}
			counts.put(targetKey, wordset);
		}
	}

	protected static String getProcessedWordKey(WordInfo wordInfo) {
		StringBuffer wordKeySB = new StringBuffer();
		for (WordInfo composingWord : wordInfo.getComposingWords()) {
			if (composingWord.isAdj() || composingWord.isAdverb() || composingWord.isNoun() || composingWord.isVerb()) {
				wordKeySB.append(composingWord.getLemma());
				wordKeySB.append(" ");
			}
		}
		String wordKey = wordKeySB.toString().trim();
		System.out.println("Word key for (" + wordInfo.getWordform() + "): " + wordKey);
		return wordKey;
	}

}
