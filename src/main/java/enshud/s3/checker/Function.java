package enshud.s3.checker;

import java.util.LinkedList;

public class Function {
	public String name;
	public LinkedList<Variable> parameters;
	public Boolean isSubstituted = false;
	public Boolean isReferenced = false; 
	
	public Function(String name_, LinkedList<Variable> parameters) {
		this.name = name_;
		this.parameters = parameters;
	}
}
