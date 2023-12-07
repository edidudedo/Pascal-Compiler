package enshud.s4.compiler;

public class Variable {
	public String name = "";
	public Type type;
	public int startAddress;
	public int endAddress;
	public Boolean isSubstituted = false;
	public Boolean isReferenced = false; 
	
	public Variable(String name_, Type type_, int startAddress_, int endAddress_) {
		this.name = name_;
		this.type = type_;
		this.startAddress = startAddress_;
		this.endAddress = endAddress_;
	}
}
