package org.openerproject.targetproperties.main;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import org.apache.log4j.Logger;
import org.openerproject.targetproperties.sspace.AlgorithmExecutor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import edu.ucla.sspace.basis.StringBasisMapping;
import edu.ucla.sspace.lsa.LatentSemanticAnalysis;
import edu.ucla.sspace.matrix.LogEntropyTransform;
import edu.ucla.sspace.matrix.MatrixFactorization;
import edu.ucla.sspace.matrix.SVD;
import edu.ucla.sspace.matrix.SVD.Algorithm;
import edu.ucla.sspace.matrix.Transform;
import edu.ucla.sspace.matrix.factorization.SingularValueDecomposition;

public class Main_sspace {

	private static Logger log=Logger.getLogger(Main_sspace.class);
	
	private AlgorithmExecutor algorithmExecutor;
	//This is quick test, use CLI for the definitive version
	
	public static void main(String[] args) throws IOException {
		
		String dirWithCorpusFiles="CORPUS";
		String algorithm="LSA";
		String language="en";
		String outputfilePath="SSPACE_OUTPUT_FILE_TEST.ssp";
		
		ApplicationContext context =
			    new ClassPathXmlApplicationContext(new String[] {"spring-config.xml"});
		
		Main_sspace main=(Main_sspace) context.getBean("main");
		main.computeVectorSpaceOnCorpus(dirWithCorpusFiles, algorithm, language, outputfilePath);
		
		//Probably there is no need of this, but to get rid of the warning about resource leaking...
		((ClassPathXmlApplicationContext)context).close();
	}
	
	
	public void computeVectorSpaceOnCorpus(String dirWithCorpusFiles, String algorithm, String language, String outputfilePath) throws IOException{
		log.info("Starting the process of compunting vector space");
		File corpusDir=new File(dirWithCorpusFiles);
		File[]corpusFiles=corpusDir.listFiles();

//		File[]shortOne=new File[10];
//		shortOne=Arrays.copyOfRange(corpusFiles, 0, 10);
		
		File outputfile=new File(outputfilePath);
		
		
		int dimensions = 300;//argOptions.getIntOption("dimensions", 300);
        Transform transform = new LogEntropyTransform();

        String algName = "ANY";//argOptions.getStringOption("svdAlgorithm", "ANY");
        MatrixFactorization factorization = SVD.getFactorization(
                Algorithm.valueOf(algName.toUpperCase()));
        StringBasisMapping basis = new StringBasisMapping();

        try {
			Class.forName("Jama.Matrix");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        LatentSemanticAnalysis lsa = new LatentSemanticAnalysis( false, dimensions, transform, (SingularValueDecomposition) SVD.getFactorization(Algorithm.JAMA), false, basis);
		
		algorithmExecutor.setAlgorithmAndParams(lsa, Collections.<String>emptyList());
		algorithmExecutor.execute(Arrays.asList(corpusFiles), language, outputfile);
		log.info("Process completed, vector space dumped to file: "+outputfile.getAbsolutePath());
	}


	public AlgorithmExecutor getAlgorithmExecutor() {
		return algorithmExecutor;
	}


	public void setAlgorithmExecutor(AlgorithmExecutor algorithmExecutor) {
		this.algorithmExecutor = algorithmExecutor;
	}

}
