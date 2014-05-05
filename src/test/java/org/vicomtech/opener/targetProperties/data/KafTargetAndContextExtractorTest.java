package org.vicomtech.opener.targetProperties.data;

//import static org.junit.Assert.*;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.List;
//
//import org.apache.commons.io.IOUtils;
//import org.junit.Before;
//import org.junit.Test;

public class KafTargetAndContextExtractorTest {

//	private KafTargetAndContextExtractor kafTargetAndContextExtractor;
//	String[]filePaths={
//			"english00001_0123ff23e0d0dc0177f9b71a1928b674.kaf",
//			"english00002_0685261321182f93763efabe4099a840.kaf",
//			"english00003_068b3a3273680d4b271a2819ff1d5b18.kaf",
//	};
//	
//	@Before
//	public void setUp() throws Exception {
//		kafTargetAndContextExtractor=new KafTargetAndContextExtractor();
//	}
//
//	@Test
//	public void testGetTargetsAndContext() {
//		String kaf=readKafToString(filePaths[0]);
//		List<TargetAndContext> targetsAndContexts = kafTargetAndContextExtractor.getTargetsAndContext(kaf);
//		for(TargetAndContext targetAndContext:targetsAndContexts){
//			for(WordInfo previousWord:targetAndContext.getPreviousWords()){
//				System.out.println("  >>> "+previousWord.getWordform());
//			}
//			System.out.println(targetAndContext.getTarget().getWordform());
//			for(WordInfo afterWord:targetAndContext.getAfterWords()){
//				System.out.println("  >>> "+afterWord.getWordform());
//			}
//			System.out.println("=================");
//		}
//	}
//
//	@Test
//	public void testGetPreviousWords() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetAfterWords() {
//		fail("Not yet implemented");
//	}
//	
//	private String readKafToString(String path){
//		InputStream is = this.getClass().getClassLoader().getResourceAsStream(path);
//		try {
//			String kaf=IOUtils.toString(is, "UTF-8");
//			return kaf;
//		} catch (IOException e) {
//			throw new RuntimeException(e);
//		}
//	}

}
