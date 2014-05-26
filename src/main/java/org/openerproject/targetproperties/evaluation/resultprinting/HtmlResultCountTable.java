package org.openerproject.targetproperties.evaluation.resultprinting;

import java.util.List;

import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.openerproject.targetproperties.classification.TargetToClassify;

public class HtmlResultCountTable {
	private static final String TABLE_TAG="table";
	private static final String TR_TAG="tr";
	private static final String TD_TAG="td";
	private static final String TH_TAG="th";
	
	/*
	 * 
	 * sb.append("</table><table>" + "<tr><th>Correct predictions</th><td>" + correctPredictions + "</td></tr>"
				+ "<tr><th>Incorrect predictions</th><td>" + incorrectPredictions + "</td></tr>"
				+ "<tr><th>% corrects predictions</th><td>" + (float) correctPredictions
				/ ((float) incorrectPredictions + (float) correctPredictions) + "</td></tr>"
				+ "<tr><th>Out of dictionary terms</th><td>" + outOfDictionaryTargets + "</td></tr></table>" +confusionMatrix.generateHtml()+ "</body></html>");
	 * 
	 */
	
	public String getHtmlResultCountTable(List<TargetToClassify>classifiedTargets){
		int[] correctIncorrectsOutOfDict = countCorrectAndIncorrectCategories(classifiedTargets);
		Element tableElem=new Element(TABLE_TAG);
		//tableElem.addContent(generateTableHeader());
		Element correctsRow=new Element(TR_TAG);
		Element col1corrects=new Element(TH_TAG);
		col1corrects.setText("Correct predictions");
		Element col2corrects=new Element(TD_TAG);
		col2corrects.setText(correctIncorrectsOutOfDict[0]+"");
		correctsRow.addContent(col1corrects);
		correctsRow.addContent(col2corrects);

		Element incorrectsRow=new Element(TR_TAG);
		Element col1incorrects=new Element(TH_TAG);
		col1incorrects.setText("Incorrect predictions");
		Element col2incorrects=new Element(TD_TAG);
		col2incorrects.setText(correctIncorrectsOutOfDict[1]+"");
		incorrectsRow.addContent(col1incorrects);
		incorrectsRow.addContent(col2incorrects);
		
		Element outOfDictRow=new Element(TR_TAG);
		Element col1outOfDict=new Element(TH_TAG);
		col1outOfDict.setText("Out of dictionary terms");
		Element col2outOfDict=new Element(TD_TAG);
		col2outOfDict.setText(correctIncorrectsOutOfDict[2]+"");
		outOfDictRow.addContent(col1outOfDict);
		outOfDictRow.addContent(col2outOfDict);
		
		tableElem.addContent(correctsRow);
		tableElem.addContent(incorrectsRow);
		tableElem.addContent(outOfDictRow);
		
		XMLOutputter xmlOutputter=new XMLOutputter(Format.getPrettyFormat());
		String tableStr=xmlOutputter.outputString(tableElem);
		return tableStr;
	}
	
	protected int[]countCorrectAndIncorrectCategories(List<TargetToClassify>classifiedTargets){
		int[]correctIncorrectsOutOfDict=new int[3];
		for(TargetToClassify targetToClassify:classifiedTargets){
			if(targetToClassify.isOutOfDictionaryTerm()){
				correctIncorrectsOutOfDict[2]++;
			}else if(targetToClassify.getAssignedCategory().equals(targetToClassify.getGoldCategory())){
				correctIncorrectsOutOfDict[0]++;
			}else{
				correctIncorrectsOutOfDict[1]++;
			}
		}
		return correctIncorrectsOutOfDict;
	}
	
}
