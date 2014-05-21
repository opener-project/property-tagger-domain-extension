package org.openerproject.targetproperties.evaluation.resultprinting;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;

public class HtmlConfusionMatrix {

	private static final String HEADER_ROW="header_marker";
	private static final String ROW_NAME_COLUMN="row_name_marker";
	private static final String TABLE_TAG="table";
	private static final String TR_TAG="tr";
	private static final String TD_TAG="td";
	private static final String TH_TAG="th";
	
//	private static final String CLASS_0="class_0";
//	private static final String CLASS_25="class_25";
//	private static final String CLASS_50="class_50";
//	private static final String CLASS_75="class_75";
	
	private String title;
	private Table<String,String,String>table;
	private int maxValue;
	private List<String>names;
	
	private HtmlConfusionMatrix(String title){
		super();
		table=HashBasedTable.create();
		this.title=title;
		this.maxValue=0;
	}
	
	public static HtmlConfusionMatrix createNew(String title){
		return new HtmlConfusionMatrix(title);
	}
	
	public void addHeaderCell(String column,String colName){
		table.put(HEADER_ROW,column, colName);
	}
	
	public void addRowName(String row, String rowName){
		table.put(row,ROW_NAME_COLUMN, rowName);
	}
	
	public void addValue(String row,String col,String value){
		table.put(row, col, value);
	}
	
	public void addValue(String row,String col,Integer value){
		table.put(row, col, value+"");
		//System.err.println("Value: "+value+"  Max value: "+maxValue+ "  Value>maxvalue:"+(value>maxValue));
		if(value>maxValue){
			maxValue=value;
		}
	}
	
	public void incrementValue(String row,String col){
		String valueStr = table.get(row, col);
		if(valueStr==null){
			valueStr="0";
		}
		Integer value=Integer.parseInt(valueStr);
		value=value+1;
		table.put(row, col, value+"");
		//System.err.println("Value: "+value+"  Max value: "+maxValue+ "  Value>maxvalue:"+(value>maxValue));
		if(value>maxValue){
			maxValue=value;
		}
	}
	
	public void printHtml(){
		System.out.println(generateHtml());
	}
	
	public void printHtml(OutputStream os){
		try {
			IOUtils.write(generateHtml(), os,"UTF-8");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public String generateHtml(){
		Element tableAndStyle=new Element("div");
		Element style=new Element("style");
		style.setText("table, tr, td, th {border:solid;border-width:1px;font-size:small} th{width:5%}"
				+ " .class_0{background-color:#FFFFFF}"
				+ " .class_25{background-color:#FFBA8D}"
				+ " .class_50{background-color:#FF944D}"
				+ " .class_75{background-color:#FF6600}");
		tableAndStyle.addContent(style);
		names=Lists.newArrayList();
		Element tableElem=new Element(TABLE_TAG);
		tableElem.setAttribute("style", "border:solid");
		tableElem.addContent(generateTitleRow());
		for(String name:table.rowKeySet()){
			if(name.length()==0 || name.equals(HEADER_ROW) || name.equals(ROW_NAME_COLUMN)){
				continue;
			}
			names.add(name);
		}
		tableElem.addContent(generateHeaderRow());
		for(String row:names){
			if(row.equals(HEADER_ROW) || row.length()==0){
				continue;
			}
			//System.err.println("Adding row: "+row);
			tableElem.addContent(generateDataRow(row));
		}
		tableAndStyle.addContent(tableElem);
		XMLOutputter outputter=new XMLOutputter(Format.getPrettyFormat());
		String htmlString=outputter.outputString(tableAndStyle);
		return htmlString;
	}
	
	protected Element generateTitleRow(){
		Element rowElem=new Element(TR_TAG);
		Element headingElement=new Element(TH_TAG);
		headingElement.setAttribute("colspan", table.columnKeySet().size()+"");
		headingElement.setText(title);
		rowElem.addContent(headingElement);
		return rowElem;
	}
	
	protected Element generateHeaderRow(){
		Element rowElem=new Element(TR_TAG);
		Element blankCol=new Element(TH_TAG);
		rowElem.addContent(blankCol);
		for(String col:names){
//			if(col.equalsIgnoreCase(ROW_NAME_COLUMN) || col.equalsIgnoreCase(HEADER_ROW)){
//				continue;
//			}
			Element headerElem=new Element(TH_TAG);
			headerElem.setText(col);
			rowElem.addContent(headerElem);
		}
		return rowElem;
	}
	
	protected Element generateDataRow(String row){
		Element rowElem=new Element(TR_TAG);
		Element rowNameCol=new Element(TH_TAG);
		rowNameCol.setText(table.get(row, ROW_NAME_COLUMN));
		rowElem.addContent(rowNameCol);
		for(String col:names){
//			if(col.equalsIgnoreCase(ROW_NAME_COLUMN)){
//				continue;
//			}
			Element cellElem=new Element(TD_TAG);
			String value = table.get(row, col);
			if(value==null || value.length()==0){
				value="0";
			}
			cellElem.setText(value);
//			cellElem.setAttribute("class", chooseClass(value));
			if(row.equals(col)){
				cellElem.setAttribute("style", "border-width:2px;background-color:#"+chooseColor(value));
			}else{
				cellElem.setAttribute("style", "background-color:#"+chooseColor(value));
			}
			
			rowElem.addContent(cellElem);
		}
		return rowElem;
	}

//	private String chooseClass(String value) {
//	//	System.err.println("Value: "+value+"   Max value: "+maxValue);
//		int intValue=Integer.parseInt(value);
//		float ratio=(float)intValue/(float)maxValue;
//		if(ratio<0.25){
//			return CLASS_0;
//		}else if(ratio<0.50){
//			return CLASS_25;
//		}else if(ratio<0.75){
//			return CLASS_50;
//		}else{
//			return CLASS_75;
//		}
//	}
	
	private String chooseColor(String value){
		int intValue=Integer.parseInt(value);
		Double ratio=(double)intValue/(double)maxValue;
		Double f=Math.pow(2,8)-1;
		Double val = f-(f*ratio);
		//System.err.println("For ratio "+ratio+" returning "+"FF"+Integer.toHexString(val.intValue()));
		String otherColorsVal = Integer.toHexString(val.intValue());
		if(otherColorsVal.length()==1){
			otherColorsVal="0"+otherColorsVal;
		}
		return "FF"+otherColorsVal+otherColorsVal;
	}
	
}
