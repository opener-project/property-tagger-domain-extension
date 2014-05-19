package org.openerproject.targetproperties.sandbox;

@Deprecated
public class TextSpan {

	private int begin;
	private int end;

	public TextSpan(int begin, int end) {
		super();
		this.begin = begin;
		this.end = end;
	}

	public int getBegin() {
		return begin;
	}

	public void setBegin(int begin) {
		this.begin = begin;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

}
