package org.vicomtech.opener.targetProperties.data;

import java.util.List;

import org.vicomtech.opener.targetProperties.model.TargetAndContext;

public interface TargetAndContextExtractor {

	public List<TargetAndContext>getTargetsAndContext(String documentContent);
	
}
