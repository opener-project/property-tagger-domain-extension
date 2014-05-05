package org.openerproject.targetproperties.sspace;

import java.io.File;

import edu.ucla.sspace.common.SemanticSpaceIO;
import edu.ucla.sspace.lsa.LatentSemanticAnalysis;

public class Algorithms {

	public void nothing() throws Exception{
		
		String[]args={};
		edu.ucla.sspace.lsa.LatentSemanticAnalysis lsa= new LatentSemanticAnalysis();
		//lsa.
		SemanticSpaceIO.save(lsa, new File("ADAS"));
	}
	
}
