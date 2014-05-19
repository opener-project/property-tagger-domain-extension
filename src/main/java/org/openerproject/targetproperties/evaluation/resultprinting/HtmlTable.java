package org.openerproject.targetproperties.evaluation.resultprinting;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

public class HtmlTable {

	private static final String HEADER_MARKER="header_marker";
	private static final String ROW_NAME_MARKER="row_name_marker";
	private static final String TABLE_TAG="table";
	private static final String ROW_TAG="tr";
	private static final String CELL_TAG="td";
	private static final String HEADING_TAG="th";
	
	private String title;
	private Table<String,String,String>table;
	
	private HtmlTable(String title){
		super();
		table=HashBasedTable.create();
		this.title=title;
	}
	
	public static HtmlTable createNew(String title){
		return new HtmlTable(title);
	}
	
	public void addHeaderCell(String column,String colName){
		table.put(column,HEADER_MARKER, colName);
	}
	
	public void addRowName(String row, String rowName){
		table.put(ROW_NAME_MARKER, row, rowName);
	}
	
	public void addValue(String row,String col,String value){
		table.put(row, col, value);
	}
	
	public void printHtml(){
		
		
		
	}
	
	public String generateHtml(){
		StringBuffer htmlBuff=new StringBuffer();
		Set<String> rows = table.rowKeySet();
		Set<String> cols = table.columnKeySet();
		
		return null;
	}
	
	protected String generateTableHeader(){
		StringBuffer headerBuff=new StringBuffer();
		Map<String, String> headerRow = table.row(HEADER_MARKER);
		headerBuff.append(getFullSpanRowForTitle());
		headerBuff.append("<tr>");
		for(String col:table.columnKeySet()){
			if(!col.equals(HEADER_MARKER)){
				
			}
		}
		return null;
	}
	
	protected String getFullSpanRowForTitle(){
		return  openTag(ROW_TAG)+openTag(HEADING_TAG)+title+closeTag(HEADING_TAG)+ "<tr><th>"+ "<th colspan="+table.columnKeySet().size()+"\""+">"+title+"</th></tr>";
	}
	
	private String openTag(String name){
		return "<"+name+">";
	}
	
	private String closeTag(String name){
		return "</"+name+">";
	}
}
