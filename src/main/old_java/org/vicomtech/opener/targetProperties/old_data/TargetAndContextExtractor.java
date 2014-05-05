package org.vicomtech.opener.targetProperties.old_data;

import java.util.List;

import org.vicomtech.opener.targetProperties.old_model.TargetAndContext;

public interface TargetAndContextExtractor {

	public List<TargetAndContext>getTargetsAndContext(String documentContent);
	
}
