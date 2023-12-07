package enshud.s3.checker;

public class Variable {
	public String name = "";
	public String type;
	public Boolean isSubstituted = false;
	public Boolean isReferenced = false; 
	
	public Variable(String name_, String type_) {
		this.name = name_;
		this.type = type_;
	}
}
