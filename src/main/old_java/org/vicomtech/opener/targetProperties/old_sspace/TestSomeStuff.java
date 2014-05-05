package org.vicomtech.opener.targetProperties.old_sspace;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.Lists;

import edu.ucla.sspace.text.IteratorFactory;

public class TestSomeStuff {

	public static void main(String[] args) throws IOException {
		Properties props=new Properties();
		
		List<String>compoundNames=Lists.newArrayList("white house","battery life");
		File tempfile=File.createTempFile("tempfile", ".temp");
		FileUtils.writeLines(tempfile, compoundNames);
		props.setProperty(IteratorFactory.COMPOUND_TOKENS_FILE_PROPERTY, tempfile.getAbsolutePath());
		
		//El código del SSPACE para el tokenfiltering (una caca) hace internamente un split por ":", así que casca porque parte el string en C: (sólo vale para linux...)
//		List<String>stopwords=Lists.newArrayList("thw");
//		File stopwordsFile = File.createTempFile("sspace_stopwords", ".temp");
//		FileUtils.writeLines(stopwordsFile, stopwords);
//		props.setProperty(IteratorFactory.TOKEN_FILTER_PROPERTY, "exclude="+stopwordsFile.getAbsolutePath().replace("\\", "/"));
		
		
		IteratorFactory.setProperties(props);
		Iterator<String> tokens = IteratorFactory.tokenize("This, is-a T.E.S.T!!! thw white house and battery life Look?");
		while(tokens.hasNext()){
			System.out.println(tokens.next());
		}
	}

}
