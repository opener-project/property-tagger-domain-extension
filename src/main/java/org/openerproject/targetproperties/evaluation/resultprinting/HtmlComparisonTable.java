package org.openerproject.targetproperties.evaluation.resultprinting;

import java.util.List;

import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.openerproject.targetproperties.classification.TargetToClassify;

public class HtmlComparisonTable {

//	private static final String HEADER_ROW="header_marker";
//	private static final String ROW_NAME_COLUMN="row_name_marker";
	private static final String TABLE_TAG="table";
	private static final String TR_TAG="tr";
	private static final String TD_TAG="td";
	private static final String TH_TAG="th";
	
	private static final String STYLE_TAG="style";
	
	private static final String COLOR_FOR_CORRECTS="green";
	private static final String COLOR_FOR_INCORRECTS="orange";
	private static final String COLOR_FOR_OUTOFDICT="grey";
	
	private static final String CORRECTLY_CLASSIFIED_CSS_CLASS="correct";
	private static final String INCORRECTLY_CLASSIFIED_CSS_CLASS="incorrect";
	private static final String OUT_OF_DICTIONARY_CSS_CLASS="outofdict";
	
	public String getHtmlTable(List<TargetToClassify>classifiedTargets){
		Element tableAndStyle=new Element("div");
		Element tableElem=new Element(TABLE_TAG);
		tableElem.addContent(generateTableHeader());
		for(TargetToClassify targetToClassify:classifiedTargets){
			Element rowElem=generateTableRow(targetToClassify);
			tableElem.addContent(rowElem);
		}
		Element styleElem=generateStyleElemet();
		tableAndStyle.addContent(styleElem);
		tableAndStyle.addContent(tableElem);
		XMLOutputter xmlOutputter=new XMLOutputter(Format.getPrettyFormat());
		String htmlString=xmlOutputter.outputString(tableAndStyle);
		return htmlString;
	}
	
	private Element generateStyleElemet(){
		Element styleElem=new Element(STYLE_TAG);
		StringBuffer sb=new StringBuffer();
		sb.append("."+CORRECTLY_CLASSIFIED_CSS_CLASS+"{");
		sb.append("background-color:"+COLOR_FOR_CORRECTS);
		sb.append(";");
		sb.append("}");
		sb.append("."+INCORRECTLY_CLASSIFIED_CSS_CLASS+"{");
		sb.append("background-color:"+COLOR_FOR_INCORRECTS);
		sb.append(";");
		sb.append("}");
		sb.append("."+OUT_OF_DICTIONARY_CSS_CLASS+"{");
		sb.append("background-color:"+COLOR_FOR_OUTOFDICT);
		sb.append(";");
		sb.append("}");
		styleElem.setText(sb.toString());
		return styleElem;
	}
	
	private Element generateTableHeader(){
		Element headerRowElem=new Element(TR_TAG);
		Element targetHeader=new Element(TH_TAG);
		targetHeader.setText("OPINION TARGET");
		headerRowElem.addContent(targetHeader);
		Element predictedCategoryHeader=new Element(TH_TAG);
		predictedCategoryHeader.setText("PREDICTED CATEGORY");
		headerRowElem.addContent(predictedCategoryHeader);
		Element goldCategoryElem=new Element(TH_TAG);
		goldCategoryElem.setText("GOLD CATEGORY");
		headerRowElem.addContent(goldCategoryElem);
		return headerRowElem;
	}
	
	protected Element generateTableRow(TargetToClassify targetToClassify){
		Element rowElem=new Element(TR_TAG);
		Element targetCellElem=new Element(TD_TAG);
		targetCellElem.setText(targetToClassify.getOpinionTarget());
		Element predictedCategoryCellElem=new Element(TD_TAG);
		if(targetToClassify.isOutOfDictionaryTerm()){
			predictedCategoryCellElem.setText("OUT OF DICTIONARY");
		}else{
			predictedCategoryCellElem.setText(targetToClassify.getAssignedCategory());
		}
		Element goldCategoryCellElem=new Element(TD_TAG);
		goldCategoryCellElem.setText(targetToClassify.getGoldCategory());
		
		rowElem.addContent(targetCellElem);
		rowElem.addContent(predictedCategoryCellElem);
		rowElem.addContent(goldCategoryCellElem);
		if(targetToClassify.isOutOfDictionaryTerm()){
			rowElem.setAttribute("class", OUT_OF_DICTIONARY_CSS_CLASS);
		}
		else if(targetToClassify.correctlyClassified()){
			rowElem.setAttribute("class", CORRECTLY_CLASSIFIED_CSS_CLASS);
		}else{
			rowElem.setAttribute("class", INCORRECTLY_CLASSIFIED_CSS_CLASS);
		}
		return rowElem;
	}
	
}
