package enshud.s4.compiler;

import java.util.LinkedList;

public class Function {
	public String name;
	public LinkedList<Variable> parameters;
	public int number;
	
	public Function(String name_, LinkedList<Variable> parameters, int functionNumber_) {
		this.name = name_;
		this.parameters = parameters;
		this.number = functionNumber_;
	}
}
