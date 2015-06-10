/**
 * 
 */
package com.generate.code.model;

/**
 * @author Administrator
 *
 */
public class TableFieldInfo {
	
	private String name;
	private String type;
	private String length;
	private String note;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getLength() {
		return length;
	}
	public void setLength(String length) {
		this.length = length;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	@Override
	public String toString() {
		return "TableFieldInfo [name=" + name + ", type=" + type + ", length=" + length + ", note=" + note + "]";
	}
	

}
