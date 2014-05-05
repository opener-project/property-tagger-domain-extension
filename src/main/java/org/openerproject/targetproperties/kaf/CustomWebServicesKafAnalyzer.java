package org.openerproject.targetproperties.kaf;

import java.util.Set;

import javax.xml.ws.BindingProvider;

import org.openerproject.ws.client.OpenerService;
import org.openerproject.ws.client.OpenerServiceImplService;

import com.google.common.collect.Sets;

public class CustomWebServicesKafAnalyzer implements Analyzer {

	private AnalysisTypes[] analysisTypes={AnalysisTypes.TOKEN,AnalysisTypes.POSTAG};
	private String endpointURL;
	private OpenerService openerService;

	@Override
	public void setAnalysisTypes(AnalysisTypes[] analysisTypes) {
		this.analysisTypes = analysisTypes;
	}

	@Override
	public String analyzeText(String rawText) {
		String lang = openerService.identifyLanguage(rawText);
		return analyzeText(rawText, lang);
	}

	@Override
	public String analyzeText(String rawText, String lang) {
		// This is a mess... but it is just for this... better implementations
		// could be provided after
		Set<AnalysisTypes> analysisTypesSet = Sets.newHashSet(analysisTypes);
		String kaf = null;
		if (analysisTypesSet.contains(AnalysisTypes.TOKEN)) {
			kaf = openerService.tokenize(rawText, lang);
		}
		if (analysisTypesSet.contains(AnalysisTypes.TOKEN) && analysisTypesSet.contains(AnalysisTypes.POSTAG)) {
			kaf = openerService.postag(kaf, lang);
		}
		return kaf;
	}

	protected OpenerService getOpenerService(String endpointURL) {
		OpenerServiceImplService serviceImpl = new OpenerServiceImplService();
		OpenerService service = serviceImpl.getOpenerServiceImplPort();
		// String endpointURL = "http://192.168.17.128:9999/ws/opener?wsdl";
		BindingProvider bp = (BindingProvider) service;
		bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointURL);
		bp.getRequestContext().put("com.sun.xml.internal.ws.request.timeout", 60000);
		return service;
	}

	public String getEndpointURL() {
		return endpointURL;
	}

	public void setEndpointURL(String endpointURL) {
		this.openerService = getOpenerService(endpointURL);
	}

}
