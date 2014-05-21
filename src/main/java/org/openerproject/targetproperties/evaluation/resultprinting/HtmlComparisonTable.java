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
	
	private static final String CORRECTLY_CLASSIFIED_CSS_CLASS="correct";
	private static final String INCORRECTLY_CLASSIFIED_CSS_CLASS="incorrect";
	
	public String getHtmlTable(List<TargetToClassify>classifiedTargets){
		Element tableElem=new Element(TABLE_TAG);
		tableElem.addContent(generateTableHeader());
		for(TargetToClassify targetToClassify:classifiedTargets){
			Element rowElem=generateTableRow(targetToClassify);
			tableElem.addContent(rowElem);
		}
		XMLOutputter xmlOutputter=new XMLOutputter(Format.getPrettyFormat());
		String htmlString=xmlOutputter.outputString(tableElem);
		return htmlString;
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
		predictedCategoryCellElem.setText(targetToClassify.getAssignedCategory());
		Element goldCategoryCellElem=new Element(TD_TAG);
		goldCategoryCellElem.setText(targetToClassify.getGoldCategory());
		
		rowElem.addContent(targetCellElem);
		rowElem.addContent(predictedCategoryCellElem);
		rowElem.addContent(goldCategoryCellElem);
		if(targetToClassify.correctlyClassified()){
			rowElem.setAttribute("class", CORRECTLY_CLASSIFIED_CSS_CLASS);
		}else{
			rowElem.setAttribute("class", INCORRECTLY_CLASSIFIED_CSS_CLASS);
		}
		return rowElem;
	}
	
}
